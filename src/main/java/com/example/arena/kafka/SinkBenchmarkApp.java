package com.example.arena.kafka;

import com.example.arena.kafka.bench.SyntheticSource;
import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.config.PipelineFactory;
import com.example.arena.kafka.core.*;
import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SinkBenchmarkApp {

    private static final Logger log = LoggerFactory.getLogger(SinkBenchmarkApp.class);

    public static void main(String[] args) {
        log.info("=== Booting Sink Benchmark (Synthetic Source -> Real Postgres) ===");

        // --------------------------------------------------------------------
        // 1. SETUP METRICS & HTTP SERVER (Port 8080)
        // --------------------------------------------------------------------
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        // Run HTTP Server on a daemon thread
        Thread metricsThread = new Thread(() -> {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
                server.createContext("/metrics", httpExchange -> {
                    String response = registry.scrape();
                    byte[] bytes = response.getBytes();
                    httpExchange.getResponseHeaders().set(
                            "Content-Type",
                            "text/plain; version=0.0.4; charset=utf-8"
                    );
                    httpExchange.sendResponseHeaders(200, bytes.length);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(bytes);
                    }
                });
                server.start();
                log.info("📊 Metrics enabled at http://localhost:8080/metrics");
            } catch (Exception e) {
                log.error("Failed to start metrics server", e);
            }
        }, "metrics-http");

        metricsThread.setDaemon(true);
        metricsThread.start();

        // --------------------------------------------------------------------
        // 2. LOAD CONFIG & COMPONENTS
        // --------------------------------------------------------------------
        PipelineConfig config = PipelineConfig.get();

        // Use Synthetic Source (generates fake data in memory)
        SourceConnector<String> source = new SyntheticSource();

        // Initialize Real Sink (Postgres)
        OutputSink<String> actualSink;
        OutputSink<String> monitoredSink;

        try {
            actualSink = PipelineFactory.createSink(config);

            // WRAPPER: Measure actual DB write latency
            monitoredSink = payload -> {
                Timer.Sample sample = Timer.start(registry);
                try {
                    actualSink.write(payload);
                } finally {
                    // This creates the "sink_latency_seconds" metric in Grafana
                    sample.stop(registry.timer("sink.latency", "type", "postgres"));
                }
            };

        } catch (Exception e) {
            log.error("🚨 Fatal Error: Failed to initialize pipeline components.", e);
            System.exit(1);
            return;
        }

        // Helpers
        OutputSink<String> dlq = payload -> log.warn("⚠️ [DLQ] {}", payload.id());
        CacheStrategy<String> cache = PipelineFactory.createCache(config);

        // No-Op Business Logic (we are benchmarking the Sink, not the CPU)
        Transformer<String, String> businessLogic = input -> input.withData(input.data());

        // --------------------------------------------------------------------
        // 3. ORCHESTRATOR
        // --------------------------------------------------------------------
        int parallelism = config.getInt("pipeline.parallelism");

        PipelineOrchestrator<String, String> pipeline =
                new PipelineOrchestrator<>(
                        source,
                        monitoredSink, // Use the wrapped sink
                        dlq,
                        businessLogic,
                        cache,
                        parallelism
                );

        // Graceful Shutdown
        Runtime.getRuntime().addShutdownHook(
                new Thread(pipeline::stop, "sink-bench-shutdown")
        );

        // --------------------------------------------------------------------
        // 4. RUN
        // --------------------------------------------------------------------
        pipeline.start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            pipeline.stop();
        }
    }
}