package com.example.arena.kafka.core;

/**
 * destination for data (Kafka Topic, Database, File, Log).
 * @param <T> Data Type
 */
public interface OutputSink<T> {

    /**
     * Persist the payload.
     */
    void write(PipelinePayload<T> payload);
}