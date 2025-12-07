package com.example.arena.kafka.config;

import com.example.arena.kafka.cache.LocalCacheStrategy;
import com.example.arena.kafka.cache.RedisCacheStrategy;
import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.core.PipelinePayload;
import com.example.arena.kafka.core.SourceConnector;
import com.example.arena.kafka.core.Transformer;
import com.example.arena.kafka.ingestion.KafkaSourceConnector;
import com.example.arena.kafka.ingestion.RestSourceConnector;
import com.example.arena.kafka.output.KafkaSink;

import java.util.Optional;
import java.util.Properties;

/**
 * Central wiring class for Sources, Sinks, Transformers, and CacheStrategies.
 *
 * This is the main “extension point” for adding new connector types.
 * Open-source users can:
 *  - Implement SourceConnector / OutputSink / CacheStrategy
 *  - Add a new case in the switch for source.type / sink.type / cache.type
 *  - Configure via pipeline.properties
 */
public class PipelineFactory {

    // ------------------------------------------------------------------------
    // SOURCE
    // ------------------------------------------------------------------------
    public static SourceConnector<String> createSource(PipelineConfig config) {
        String type = config.getProperty("source.type", "REST").toUpperCase();

        return switch (type) {
            case "REST" -> new RestSourceConnector();

            case "KAFKA" -> {
                String topic = config.getProperty("source.topic", "arena-default");
                Properties props = loadConsumerProps(config);
                yield new KafkaSourceConnector(topic, props);
            }

            default -> throw new IllegalArgumentException("Unknown Source: " + type);
        };
    }

    // ------------------------------------------------------------------------
    // SINK
    // ------------------------------------------------------------------------
    public static OutputSink<String> createSink(PipelineConfig config) {
        String type = config.getProperty("sink.type", "KAFKA").toUpperCase();

        return switch (type) {
            case "KAFKA" -> {
                String topic = config.getProperty("sink.topic", "arena-default");
                Properties props = loadProducerProps(config);

                // default false = async for performance; true = sync for safety
                boolean syncSend = Boolean.parseBoolean(
                        config.getProperty("kafka.sink.sync", "false")
                );

                yield new KafkaSink(topic, props, syncSend);
            }

            case "CONSOLE" -> payload -> {
                // no-op or optional println for debugging / pure CPU benchmarks
                // System.out.println(">> " + payload.data());
            };

            default -> throw new IllegalArgumentException("Unknown Sink: " + type);
        };
    }

    // ------------------------------------------------------------------------
    // TRANSFORMER
    // ------------------------------------------------------------------------
    public static Transformer<String, String> createTransformer(PipelineConfig config) {
        // Simple default business logic.
        // KafkaApp can still override this with a custom transformer.
        return input -> input.withData(input.data().toUpperCase());
    }

    // ------------------------------------------------------------------------
    // CACHE
    // ------------------------------------------------------------------------
    public static CacheStrategy<String> createCache(PipelineConfig config) {
        String type = config.getProperty("cache.type", "LOCAL").toUpperCase();

        return switch (type) {
            case "LOCAL" -> new LocalCacheStrategy<>();

            case "REDIS" -> new RedisCacheStrategy<>();

            default -> new CacheStrategy<>() {
                @Override
                public void put(String key, PipelinePayload<String> value) throws Exception {
                    // no-op
                }

                @Override
                public Optional<PipelinePayload<String>> get(String key) throws Exception {
                    return Optional.empty();
                }

                @Override
                public void remove(String key) throws Exception {
                    // no-op
                }
            };
        };
    }

    // ------------------------------------------------------------------------
    // HELPER METHODS: PRODUCER / CONSUMER PROPS
    // ------------------------------------------------------------------------

    /**
     * Build tuned Kafka Producer properties from pipeline.properties
     * with reasonable throughput-oriented defaults.
     */
    static Properties loadProducerProps(PipelineConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers",
                config.getProperty("kafka.broker", "localhost:9092"));
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        // --- Throughput-oriented defaults (overridable in pipeline.properties) ---
        props.put("acks",
                config.getProperty("kafka.producer.acks", "1")); // "all" for safety, "1" for speed
        props.put("compression.type",
                config.getProperty("kafka.producer.compression", "lz4"));
        props.put("batch.size",
                config.getProperty("kafka.producer.batch.size", "131072")); // 128KB
        props.put("linger.ms",
                config.getProperty("kafka.producer.linger.ms", "5"));
        props.put("buffer.memory",
                config.getProperty("kafka.producer.buffer.memory", "67108864")); // 64MB
        props.put("max.in.flight.requests.per.connection",
                config.getProperty("kafka.producer.max.in.flight", "5"));
        props.put("enable.idempotence",
                config.getProperty("kafka.producer.idempotence", "false"));

        return props;
    }

    /**
     * Build tuned Kafka Consumer properties from pipeline.properties.
     */
    static Properties loadConsumerProps(PipelineConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers",
                config.getProperty("kafka.broker", "localhost:9092"));
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("group.id",
                config.getProperty("kafka.group.id", "arena-consumer-group"));
        props.put("auto.offset.reset",
                config.getProperty("kafka.auto.offset.reset", "earliest"));

        // --- High-throughput defaults ---
        props.put("fetch.min.bytes",
                config.getProperty("kafka.consumer.fetch.min.bytes", "1048576")); // 1MB
        props.put("fetch.max.wait.ms",
                config.getProperty("kafka.consumer.fetch.max.wait.ms", "50"));
        props.put("max.partition.fetch.bytes",
                config.getProperty("kafka.consumer.max.partition.fetch.bytes", "4194304")); // 4MB
        props.put("max.poll.records",
                config.getProperty("kafka.consumer.max.poll.records", "500"));
        props.put("enable.auto.commit",
                config.getProperty("kafka.consumer.enable.auto.commit", "true"));

        return props;
    }
}
