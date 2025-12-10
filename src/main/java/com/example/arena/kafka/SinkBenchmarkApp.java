package com.example.arena.kafka;

import com.example.arena.kafka.bench.SyntheticSource;
import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.config.PipelineFactory;
import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.core.PipelineOrchestrator;
import com.example.arena.kafka.core.SourceConnector;
import com.example.arena.kafka.core.Transformer;
import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Counter;
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
        // 1. METRICS REGISTRY + /metrics HTTP ENDPOINT
        // --------------------------------------------------------------------
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

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
        // 2. METRICS: COUNTERS + TIMERS
        // --------------------------------------------------------------------
        Counter persistedCounter = Counter.builder("arena_sink_records_total")
                .description("Total number of records successfully written to Postgres")
                .tag("stage", "persisted")
                .register(registry);

        Counter failedCounter = Counter.builder("arena_sink_records_total")
                .description("Total number of records that failed to be written to Postgres")
                .tag("stage", "failed")
                .register(registry);

        Timer sinkLatency = Timer.builder("arena_sink_record_latency_seconds")
                .description("End-to-end record latency from sink entry to Postgres commit")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.9, 0.99)
                .tag("sink", "postgres")
                .register(registry);

        // --------------------------------------------------------------------
        // 3. LOAD CONFIG & COMPONENTS
        // --------------------------------------------------------------------
        PipelineConfig config = PipelineConfig.get();

        // Synthetic Source (generates benchmark data)
        SourceConnector<String> source = new SyntheticSource();

        // Real Sink (Postgres) wrapped with metrics
        OutputSink<String> monitoredSink;
        OutputSink<String> actualSink;

        try {
            actualSink = PipelineFactory.createSink(config);

            monitoredSink = payload -> {
                Timer.Sample sample = Timer.start(registry);
                try {
                    actualSink.write(payload);
                    persistedCounter.increment();
                } catch (Exception ex) {
                    failedCounter.increment();
                    throw ex;
                } finally {
                    sample.stop(sinkLatency);
                }
            };

        } catch (Exception e) {
            log.error("🚨 Fatal Error: Failed to initialize pipeline components.", e);
            System.exit(1);
            return;
        }

        // DLQ just logs the payload for now
        OutputSink<String> dlq = payload -> log.warn("⚠️ [DLQ] {}", payload);

        // Cache strategy (could be in-memory, no-op, etc. depending on config)
        CacheStrategy<String> cache = PipelineFactory.createCache(config);

        // No-op business logic: identity transform (we’re benchmarking I/O, not CPU)
        Transformer<String, String> businessLogic = input -> input;

        // --------------------------------------------------------------------
        // 4. ORCHESTRATOR
        // --------------------------------------------------------------------
        int parallelism = config.getInt("pipeline.parallelism");

        PipelineOrchestrator<String, String> pipeline =
                new PipelineOrchestrator<>(
                        source,
                        monitoredSink,
                        dlq,
                        businessLogic,
                        cache,
                        parallelism
                );

        Runtime.getRuntime().addShutdownHook(
                new Thread(pipeline::stop, "sink-bench-shutdown")
        );

        // --------------------------------------------------------------------
        // 5. RUN (BLOCK MAIN THREAD)
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
