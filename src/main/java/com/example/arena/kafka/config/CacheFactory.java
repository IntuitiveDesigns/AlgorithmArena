package com.example.arena.kafka.config;

import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.cache.LocalCacheStrategy;
import com.example.arena.kafka.cache.RedisCacheStrategy;

public class CacheFactory {

    public enum CacheType { LOCAL, REDIS }

    public static <T> CacheStrategy<T> getCache(CacheType type) {
        return switch (type) {
            case LOCAL -> new LocalCacheStrategy<>();
            case REDIS -> new RedisCacheStrategy<>("localhost", 6379);
        };
    }
}