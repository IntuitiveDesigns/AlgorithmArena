package com.example.arena.kafka;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.config.PipelineFactory;
import com.example.arena.kafka.core.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class KafkaApp {

    private static final Logger log = LoggerFactory.getLogger(KafkaApp.class);

    public static void main(String[] args) throws InterruptedException {
        log.info("=== Booting Enterprise Pipeline Agent ===");

        PipelineConfig config = PipelineConfig.get();
        SimpleMeterRegistry registry = new SimpleMeterRegistry();

        // FIX: Use Anonymous Class (not Lambda) because SourceConnector has 3 methods
        SourceConnector<String> source = new SourceConnector<String>() {
            @Override
            public void connect() { log.info("🔌 Mock Source Connected"); }

            @Override
            public void disconnect() { log.info("🔌 Mock Source Disconnected"); }

            @Override
            public PipelinePayload<String> fetch() {
                // Generate random data
                String randomData = "Event-" + UUID.randomUUID().toString().substring(0, 8);
                return PipelinePayload.of(randomData);
            }
        };

        // These can stay as Lambdas because OutputSink/Transformer only have 1 method
        OutputSink<String> primarySink = payload -> {
            // log.info("✅ [SINK] Persisted: {} (ID: {})", payload.data(), payload.id());
        };

        OutputSink<String> dlqSink = payload -> {
            // log.warn("⚠️ [DLQ] Saved Bad Event: {} (ID: {})", payload.data(), payload.id());
        };

        Transformer<String, String> businessLogic = input -> {
            if (input.data().contains("a")) {
                // throw new RuntimeException("Simulated Poison Pill!");
            }
            return input.withData(input.data().toUpperCase());
        };

        // Monitor the transformer
        Transformer<String, String> monitoredTransformer = new MonitoredTransformer<>(
                businessLogic, "pipeline.transform.latency", registry
        );

        CacheStrategy<String> cache = PipelineFactory.createCache(config);

        // Build Pipeline
        PipelineOrchestrator<String, String> pipeline = new PipelineOrchestrator<>(
                source, primarySink, dlqSink, monitoredTransformer, cache
        );

        pipeline.start();

        // Let it run for 5 seconds
        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            System.out.println("📊 Metrics: " + registry.getMetersAsString());
        }

        pipeline.stop();
        log.info("Agent Shutdown.");
    }
}