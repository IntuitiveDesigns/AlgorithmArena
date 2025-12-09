package com.example.arena.kafka.output;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.core.PipelinePayload;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Kafka sink for PipelinePayloads.
 * Supports async (high-throughput) and optional sync mode via configuration.
 */
public class KafkaSink implements OutputSink<String>, AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(KafkaSink.class);

    private final KafkaProducer<String, String> producer;
    private final String topic;
    private final boolean syncSend; // true = .get(), false = async

    /**
     * @param topic Kafka topic to write to
     * @param props Kafka producer properties
     * @param syncSend if true, block for each send; if false, use async fire-and-forget with callback logging
     */
    public KafkaSink(String topic, Properties props, boolean syncSend) {
        this.topic = topic;
        this.producer = new KafkaProducer<>(props);
        this.syncSend = syncSend;
        log.info("KafkaSink created for topic='{}', syncSend={}", topic, syncSend);
    }

    private static Properties buildProducerProps(PipelineConfig config) {
        Properties props = new Properties();
        props.put("bootstrap.servers", config.getProperty("kafka.broker", "localhost:9092"));
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("acks", config.getProperty("kafka.producer.acks", "1"));
        props.put("compression.type", config.getProperty("kafka.producer.compression", "lz4"));
        props.put("batch.size", config.getProperty("kafka.producer.batch.size", "131072"));
        props.put("linger.ms", config.getProperty("kafka.producer.linger.ms", "5"));
        props.put("buffer.memory", config.getProperty("kafka.producer.buffer.memory", "67108864"));
        props.put("max.in.flight.requests.per.connection",
                config.getProperty("kafka.producer.max.in.flight", "5"));
        props.put("enable.idempotence",
                config.getProperty("kafka.producer.idempotence", "false"));

        return props;
    }

    @Override
    public void write(PipelinePayload<String> payload) {
        // Create Record using the Trace ID as the Key (preserves per-key ordering)
        ProducerRecord<String, String> record = new ProducerRecord<>(
                topic,
                payload.id(),   // Key
                payload.data()  // Value
        );

        if (syncSend) {
            // Synchronous (safer, but slower)
            try {
                RecordMetadata metadata = producer.send(record).get();
                // You could log at trace-level if you ever want per-record info.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while sending to Kafka", e);
            } catch (ExecutionException e) {
                throw new RuntimeException("Kafka broker rejected message", e);
            }
        } else {
            // Asynchronous (high throughput, production-style)
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    // In a full system, you might route to DLQ here.
                    log.error("Async Kafka send failed for key={}", payload.id(), exception);
                }
            });
        }
    }

    @Override
    public void close() {
        try {
            producer.flush();
        } finally {
            producer.close();
        }
        log.info("KafkaSink for topic='{}' closed", topic);
    }
}
