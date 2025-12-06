package com.example.arena.kafka.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Enterprise Config Loader.
 * Reads from 'pipeline.properties' on the classpath.
 * Fails fast if required properties are missing.
 */
public class PipelineConfig {

    private static final PipelineConfig INSTANCE = new PipelineConfig();
    private final Properties props = new Properties();

    private PipelineConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("pipeline.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find pipeline.properties");
                return;
            }
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static PipelineConfig get() {
        return INSTANCE;
    }

    private static Properties loadProducerProps(PipelineConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getProperty("kafka.broker", "localhost:9092"));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // --- Throughput-oriented defaults (overridable in pipeline.properties) ---
        props.put("acks", config.getProperty("kafka.producer.acks", "1"));                // "all" for safety, "1" for speed
        props.put("compression.type", config.getProperty("kafka.producer.compression", "lz4"));
        props.put("batch.size", config.getProperty("kafka.producer.batch.size", "131072")); // 128KB
        props.put("linger.ms", config.getProperty("kafka.producer.linger.ms", "5"));        // small delay to fill batches
        props.put("buffer.memory", config.getProperty("kafka.producer.buffer.memory", "67108864")); // 64MB
        props.put("max.in.flight.requests.per.connection",
                config.getProperty("kafka.producer.max.in.flight", "5"));
        props.put("enable.idempotence",
                config.getProperty("kafka.producer.idempotence", "false"));

        return props;
    }

    private static Properties loadConsumerProps(PipelineConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getProperty("kafka.broker", "localhost:9092"));
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", config.getProperty("kafka.group.id", "arena-consumer-group"));
        props.put("auto.offset.reset", config.getProperty("kafka.auto.offset.reset", "earliest"));

        // --- High-throughput consumption ---
        props.put("fetch.min.bytes", config.getProperty("kafka.consumer.fetch.min.bytes", "1048576"));          // 1MB
        props.put("fetch.max.wait.ms", config.getProperty("kafka.consumer.fetch.max.wait.ms", "50"));
        props.put("max.partition.fetch.bytes", config.getProperty("kafka.consumer.max.partition.fetch.bytes", "4194304")); // 4MB
        props.put("max.poll.records", config.getProperty("kafka.consumer.max.poll.records", "500"));
        props.put("enable.auto.commit", config.getProperty("kafka.consumer.enable.auto.commit", "true"));

        return props;
    }

    // --- GENERIC GETTERS ---

    public String getProperty(String key) {
        String val = props.getProperty(key);
        if (val == null) throw new RuntimeException("Missing Configuration: " + key);
        return val;
    }

    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}