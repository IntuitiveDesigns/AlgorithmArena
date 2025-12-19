package com.example.arena.kafka.config;

import com.example.arena.kafka.bench.SyntheticSource;
import com.example.arena.kafka.cache.LocalCacheStrategy;
import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.core.PipelinePayload;
import com.example.arena.kafka.core.SourceConnector;
import com.example.arena.kafka.core.Transformer;
import com.example.arena.kafka.ingestion.KafkaSourceConnector;
import com.example.arena.kafka.ingestion.RestSourceConnector;
import com.example.arena.kafka.metrics.MetricsRuntime;
import com.example.arena.kafka.output.KafkaSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Properties;

/**
 * PIPELINE FACTORY (Enterprise Edition)
 *
 * Goals:
 * - Return real sink instances (KafkaSink) so they can be flushed/closed cleanly.
 * - Allow MetricsRuntime injection without requiring global statics.
 * - Make DLQ configurable (Kafka topic, log, devnull).
 *
 * Backward compatibility:
 * - Existing code can still call createSink(config) / createDlq(config).
 * - New code should call createSink(config, metrics) / createDlq(config, metrics).
 */
public final class PipelineFactory {

    private static final Logger log = LoggerFactory.getLogger(PipelineFactory.class);

    private PipelineFactory() {}

    // ------------------------------------------------------------------------
    // SOURCE
    // ------------------------------------------------------------------------
    public static SourceConnector<String> createSource(PipelineConfig config) {
        String type = config.getProperty("source.type", "REST").toUpperCase();

        return switch (type) {
            case "REST" -> new RestSourceConnector();

            case "KAFKA" -> {
                String topic = config.getProperty("source.topic", "arena-default");
                Properties props = new Properties();
                // NOTE: You likely want to map consumer props here later (group.id, auto.offset.reset, etc.)
                yield new KafkaSourceConnector(topic, props);
            }

            case "SYNTHETIC" -> {
                int payloadSize = Integer.parseInt(config.getProperty("source.synthetic.payload.size", "1024"));
                boolean highEntropy = Boolean.parseBoolean(config.getProperty("source.synthetic.high.entropy", "false"));
                yield new SyntheticSource(payloadSize, highEntropy);
            }

            default -> throw new IllegalArgumentException("Unknown source.type: " + type);
        };
    }

    // ------------------------------------------------------------------------
    // SINK (PRIMARY)
    // ------------------------------------------------------------------------

    /** Backward-compatible: metrics disabled for sink. */
    public static OutputSink<String> createSink(PipelineConfig config) {
        return createSink(config, null);
    }

    /** Preferred: allow sink to register Micrometer metrics via MetricsRuntime. */
    public static OutputSink<String> createSink(PipelineConfig config, MetricsRuntime metrics) {
        String type = config.getProperty("sink.type", "KAFKA").toUpperCase();

        return switch (type) {
            case "KAFKA" -> {
                String topic = config.getProperty("sink.topic", "arena-bench-test");
                yield KafkaSink.fromConfig(config, topic, metrics);
            }

            case "DEVNULL" -> payload -> { /* intentionally no-op */ };

            case "LOG" -> payload ->
                    log.info("[SINK-LOG] id={} dataLen={}", payload.id(),
                            payload.data() == null ? 0 : payload.data().length());

            default -> throw new UnsupportedOperationException("Sink type not implemented: " + type);
        };
    }

    // ------------------------------------------------------------------------
    // DLQ
    // ------------------------------------------------------------------------

    /** Backward-compatible: metrics disabled for DLQ. */
    public static OutputSink<String> createDlq(PipelineConfig config) {
        return createDlq(config, null);
    }

    /**
     * Configurable DLQ:
     * - dlq.type=KAFKA  (recommended) with dlq.topic=...
     * - dlq.type=LOG    (default fallback)
     * - dlq.type=DEVNULL
     */
    public static OutputSink<String> createDlq(PipelineConfig config, MetricsRuntime metrics) {
        String type = config.getProperty("dlq.type", "LOG").toUpperCase();

        return switch (type) {
            case "KAFKA" -> {
                String dlqTopic = config.getProperty("dlq.topic", "arena-dlq");
                yield KafkaSink.fromConfig(config, dlqTopic, metrics);
            }

            case "DEVNULL" -> payload -> { /* intentionally no-op */ };

            case "LOG" -> payload ->
                    log.warn("⚠️ [DLQ] Dropped Event id={} dataLen={}", payload.id(),
                            payload.data() == null ? 0 : payload.data().length());

            default -> throw new UnsupportedOperationException("DLQ type not implemented: " + type);
        };
    }

    // ------------------------------------------------------------------------
    // TRANSFORMER
    // ------------------------------------------------------------------------
    public static Transformer<String, String> createTransformer(PipelineConfig config) {
        String type = config.getProperty("transform.type", "NOOP").toUpperCase();

        return switch (type) {
            case "NOOP" -> input -> input;

            case "UPPER" -> input -> input.withData(input.data() == null ? null : input.data().toUpperCase());

            default -> throw new UnsupportedOperationException("Transformer type not implemented: " + type);
        };
    }

    // ------------------------------------------------------------------------
    // CACHE
    // ------------------------------------------------------------------------
    public static CacheStrategy<String> createCache(PipelineConfig config) {
        String type = config.getProperty("cache.type", "LOCAL").toUpperCase();

        if ("NOOP".equals(type)) {
            return new CacheStrategy<>() {
                @Override public void put(String key, PipelinePayload<String> value) {}
                @Override public Optional<PipelinePayload<String>> get(String key) { return Optional.empty(); }
                @Override public void remove(String key) {}
            };
        }

        // Default = LOCAL
        return new LocalCacheStrategy<>();
    }
}
