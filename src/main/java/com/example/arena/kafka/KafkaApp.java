package com.example.arena.kafka;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.config.PipelineFactory;
import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.core.PipelineOrchestrator;
import com.example.arena.kafka.core.SourceConnector;
import com.example.arena.kafka.core.Transformer;
import com.example.arena.kafka.core.MonitoredTransformer;
import com.sun.net.httpserver.HttpServer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class KafkaApp {

    private static final Logger log = LoggerFactory.getLogger(KafkaApp.class);

    public static void main(String[] args) {
        log.info("=== Booting Enterprise Pipeline Agent ===");

        PipelineConfig config = PipelineConfig.get();

        // --------------------------------------------------------------------
        // 1. METRICS / OBSERVABILITY (TOGGLEABLE)
        // --------------------------------------------------------------------
        boolean metricsEnabled = Boolean.parseBoolean(
                System.getProperty(
                        "arena.metrics.enabled",
                        config.getProperty("metrics.enabled", "true")
                )
        );

        PrometheusMeterRegistry registry = null;
        if (metricsEnabled) {
            registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
            try {
                startMetricsServer(registry, 8080);
                log.info("📊 Metrics enabled at http://localhost:{}/metrics", 8080);
            } catch (IOException e) {
                log.error("Failed to start Metrics Server; continuing without metrics", e);
                registry = null;
                metricsEnabled = false;
            }
        } else {
            log.info("📊 Metrics disabled (benchmark mode)");
        }

        // --------------------------------------------------------------------
        // 2. BUILD PIPELINE COMPONENTS
        // --------------------------------------------------------------------
        SourceConnector<String> source = PipelineFactory.createSource(config);
        OutputSink<String> primarySink = PipelineFactory.createSink(config);
        OutputSink<String> dlqSink =
                payload -> log.warn("⚠️ [DLQ] Saved Bad Event: {}", payload.id());
        CacheStrategy<String> cache = PipelineFactory.createCache(config);

        // BUSINESS LOGIC
        Transformer<String, String> businessLogic =
                input -> input.withData(input.data().toUpperCase());

        // WRAP WITH METRICS IF ENABLED
        Transformer<String, String> transformer =
                (metricsEnabled && registry != null)
                        ? new MonitoredTransformer<>(
                        businessLogic,
                        "pipeline.transform.latency",
                        registry
                )
                        : businessLogic;

        // --------------------------------------------------------------------
        // 3. ORCHESTRATOR (PARALLELISM CONFIG)
        // --------------------------------------------------------------------
        int parallelism;
        try {
            parallelism = config.getInt("pipeline.parallelism");
        } catch (RuntimeException ex) {
            parallelism = Runtime.getRuntime().availableProcessors() * 4;
            log.warn("pipeline.parallelism not set; using default {}", parallelism);
        }

        PipelineOrchestrator<String, String> pipeline =
                new PipelineOrchestrator<>(
                        source,
                        primarySink,
                        dlqSink,
                        transformer,
                        cache,
                        parallelism
                );

        // Graceful shutdown on SIGTERM / Ctrl+C
        Runtime.getRuntime().addShutdownHook(
                new Thread(pipeline::stop, "pipeline-shutdown-hook")
        );

        // --------------------------------------------------------------------
        // 4. RUN PIPELINE (BLOCK MAIN THREAD)
        // --------------------------------------------------------------------
        pipeline.start();
        try {
            // Keep main thread alive; orchestrator manages its own threads.
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Main thread interrupted; stopping pipeline...");
            pipeline.stop();
        }
    }

    // ------------------------------------------------------------------------
    // HELPER: START PROMETHEUS METRICS HTTP SERVER
    // ------------------------------------------------------------------------
    private static void startMetricsServer(PrometheusMeterRegistry registry, int port)
            throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/metrics", httpExchange -> {
            String response = registry.scrape();
            byte[] bytes = response.getBytes();

            httpExchange.getResponseHeaders()
                    .set("Content-Type", "text/plain; version=0.0.4; charset=utf-8");
            httpExchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        // Run on its own (daemon-like) thread
        Thread t = new Thread(server::start, "metrics-http-server");
        t.setDaemon(true);
        t.start();
    }
}
