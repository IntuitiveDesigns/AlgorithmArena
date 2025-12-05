package com.example.arena.kafka.core;

/**
 * Functional interface for processing data steps.
 * @param <I> Input Type
 * @param <O> Output Type
 */
public interface Transformer<I, O> {

    /**
     * Transforms the payload.
     * IMPORTANT: Implementations should use input.withData(newData)
     * to preserve the Tracing ID and Timestamp.
     */
    PipelinePayload<O> transform(PipelinePayload<I> input);
}