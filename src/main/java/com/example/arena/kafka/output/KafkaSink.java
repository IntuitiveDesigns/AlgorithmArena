package com.example.arena.kafka.output;

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
