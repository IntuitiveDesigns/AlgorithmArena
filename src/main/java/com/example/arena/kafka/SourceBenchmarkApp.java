package com.example.arena.kafka;

import com.example.arena.kafka.bench.DevNullSink;
import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.config.PipelineFactory;
import com.example.arena.kafka.core.*;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceBenchmarkApp {

    private static final Logger log = LoggerFactory.getLogger(SourceBenchmarkApp.class);

    public static void main(String[] args) {
        log.info("=== Source Benchmark: Real Source -> DevNullSink ===");

        PipelineConfig config = PipelineConfig.get();

        boolean metricsEnabled = Boolean.parseBoolean(
                config.getProperty("metrics.enabled", "true")
        );
        PrometheusMeterRegistry registry =
                metricsEnabled ? new PrometheusMeterRegistry(PrometheusConfig.DEFAULT) : null;

        SourceConnector<String> source = PipelineFactory.createSource(config);
        OutputSink<String> sink = new DevNullSink<>();
        OutputSink<String> dlq =
                payload -> log.warn("⚠️ [DLQ] {}", payload.id());
        CacheStrategy<String> cache = PipelineFactory.createCache(config);

        Transformer<String, String> businessLogic =
                input -> input.withData(input.data().toUpperCase());

        Transformer<String, String> transformer =
                (metricsEnabled && registry != null)
                        ? new MonitoredTransformer<>(
                        businessLogic,
                        "source.bench.transform.latency",
                        registry
                )
                        : businessLogic;

        int parallelism = config.getInt("pipeline.parallelism");

        PipelineOrchestrator<String, String> pipeline =
                new PipelineOrchestrator<>(
                        source,
                        sink,
                        dlq,
                        transformer,
                        cache,
                        parallelism
                );

        Runtime.getRuntime().addShutdownHook(
                new Thread(pipeline::stop, "source-bench-shutdown")
        );

        pipeline.start();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            pipeline.stop();
        }
    }
}
