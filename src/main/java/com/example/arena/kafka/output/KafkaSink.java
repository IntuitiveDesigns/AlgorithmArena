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

public class KafkaSink implements OutputSink<String> {

    private static final Logger log = LoggerFactory.getLogger(KafkaSink.class);
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public KafkaSink(String topic, Properties props) {
        this.topic = topic;
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void write(PipelinePayload<String> payload) {
        try {
            // 1. Create Record using the Trace ID as the Key (Ensures Ordering)
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic,
                    payload.id(), // Key
                    payload.data() // Value
            );

            // 2. Send and WAIT (Block) for acknowledgement.
            // This ensures if the broker is down, we throw an exception
            // so the Orchestrator can trigger the DLQ.
            RecordMetadata metadata = producer.send(record).get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while sending to Kafka", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Kafka Broker Rejected Message", e);
        }
    }

    // Helper to close resources if needed
    public void close() {
        producer.close();
    }
}