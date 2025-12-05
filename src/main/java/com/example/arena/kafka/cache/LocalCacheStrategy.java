package com.example.arena.kafka.cache;

import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.core.PipelinePayload;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheStrategy<T> implements CacheStrategy<T> {

    private final Map<String, PipelinePayload<T>> cache = new ConcurrentHashMap<>();

    @Override
    public void put(String key, PipelinePayload<T> value) {
        cache.put(key, value);
    }

    @Override
    public Optional<PipelinePayload<T>> get(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }
}