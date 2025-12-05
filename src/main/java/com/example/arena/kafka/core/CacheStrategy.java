package com.example.arena.kafka.core;

import java.util.Optional;

public interface CacheStrategy<T> {

    // Save data to cache
    void put(String key, PipelinePayload<T> value);

    // Retrieve data (Optional to handle misses gracefully)
    Optional<PipelinePayload<T>> get(String key);

    // Remove data
    void remove(String key);
}