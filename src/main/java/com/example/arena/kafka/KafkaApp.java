package com.example.arena.kafka;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.config.PipelineFactory;
import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.core.MonitoredTransformer;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.core.PipelineOrchestrator;
import com.example.arena.kafka.core.SourceConnector;
import com.example.arena.kafka.core.Transformer;
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

        // --------------------------------------------------------------------
        // 0. LOAD CONFIG
        // --------------------------------------------------------------------
        PipelineConfig config = PipelineConfig.get();

        String pipelineName = config.getProperty("pipeline.name", "Unnamed-Pipeline");
        String sourceType = config.getProperty("source.type", "REST");
        String sinkType = config.getProperty("sink.type", "KAFKA");
        String cacheType = config.getProperty("cache.type", "LOCAL");

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
        // 2. PARALLELISM / BACKPRESSURE CONFIG
        // --------------------------------------------------------------------
        int parallelism;
        try {
            parallelism = config.getInt("pipeline.parallelism");
        } catch (RuntimeException ex) {
            parallelism = Runtime.getRuntime().availableProcessors() * 4;
            log.warn("pipeline.parallelism not set; using default {}", parallelism);
        }

        // Log scenario summary up front
        log.info(
                "🔧 Pipeline Config: name='{}', source.type='{}', sink.type='{}', cache.type='{}', parallelism={}",
                pipelineName, sourceType, sinkType, cacheType, parallelism
        );

        // --------------------------------------------------------------------
        // 3. BUILD PIPELINE COMPONENTS
        // --------------------------------------------------------------------
        SourceConnector<String> source;
        OutputSink<String> primarySink;
        CacheStrategy<String> cache;

        try {
            // Factories now throw checked exceptions (e.g., ClassNotFound, IOException)
            source = PipelineFactory.createSource(config);
            primarySink = PipelineFactory.createSink(config);
            cache = PipelineFactory.createCache(config);
        } catch (Exception e) {
            log.error("🚨 Fatal Error: Failed to initialize pipeline components.", e);
            System.err.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            e.printStackTrace(); // FORCE PRINT STACK TRACE
            System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            System.exit(1); // Non-zero exit code to signal failure to Docker/Orchestrator
            return;         // Stop execution
        }

        OutputSink<String> dlqSink =
                payload -> log.warn("⚠️ [DLQ] Saved Bad Event: {}", payload.id());

        // BUSINESS LOGIC (example: uppercase transform)
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
        // 4. ORCHESTRATOR
        // --------------------------------------------------------------------
        PipelineOrchestrator<String, String> pipeline =
                new PipelineOrchestrator<>(
                        source,
                        primarySink,
                        dlqSink,
                        transformer,
                        cache,
                        parallelism // maxInFlight / backpressure control
                );

        // Graceful shutdown on SIGTERM / Ctrl+C
        Runtime.getRuntime().addShutdownHook(
                new Thread(pipeline::stop, "pipeline-shutdown-hook")
        );

        // --------------------------------------------------------------------
        // 5. RUN PIPELINE (BLOCK MAIN THREAD)
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
