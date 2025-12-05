package com.example.arena.kafka.config;

import com.example.arena.kafka.cache.LocalCacheStrategy;
import com.example.arena.kafka.cache.RedisCacheStrategy;
import com.example.arena.kafka.core.*;
import com.example.arena.kafka.ingestion.KafkaSourceConnector;
import com.example.arena.kafka.ingestion.RestSourceConnector;
import com.example.arena.kafka.output.KafkaSink;

import java.util.Optional;
import java.util.Properties;

public class PipelineFactory {

    public static SourceConnector<String> createSource(PipelineConfig config) {
        String type = config.getProperty("source.type", "REST");

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

    public static OutputSink<String> createSink(PipelineConfig config) {
        String type = config.getProperty("sink.type", "KAFKA");

        return switch (type) {
            case "KAFKA" -> {
                String topic = config.getProperty("sink.topic", "arena-default");
                Properties props = loadProducerProps(config);
                yield new KafkaSink(topic, props);
            }
            case "CONSOLE" -> payload ->
                    System.out.println(">> " + payload.data());

            default -> throw new IllegalArgumentException("Unknown Sink: " + type);
        };
    }

    public static Transformer<String, String> createTransformer(PipelineConfig config) {
        // Simple passthrough transformer for now
        return input -> input.withData(input.data().toUpperCase());
    }

    public static CacheStrategy<String> createCache(PipelineConfig config) {
        String type = config.getProperty("cache.type", "LOCAL");

        return switch (type) {
            case "LOCAL" -> new LocalCacheStrategy<>();
            case "REDIS" -> new RedisCacheStrategy<>();
            default -> new CacheStrategy<>() {
                @Override
                public void put(String key, PipelinePayload<String> value) {}
                @Override
                public Optional<PipelinePayload<String>> get(String key) { return Optional.empty(); }
                @Override
                public void remove(String key) {}
            };
        };
    }

    // --- HELPER METHODS TO LOAD PROPS ---

    private static Properties loadProducerProps(PipelineConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getProperty("kafka.broker", "localhost:9092"));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        return props;
    }

    private static Properties loadConsumerProps(PipelineConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getProperty("kafka.broker", "localhost:9092"));
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", config.getProperty("kafka.group.id", "arena-consumer-group"));
        props.put("auto.offset.reset", "earliest");
        return props;
    }
}