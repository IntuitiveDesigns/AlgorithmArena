package com.example.arena.kafka.cache;

import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.core.PipelinePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class RedisCacheStrategy<T> implements CacheStrategy<T> {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheStrategy.class);

    private final String host;
    private final int port;

    // 1. Default Constructor (Used by switch cases with no args)
    public RedisCacheStrategy() {
        this("localhost", 6379);
    }

    // 2. Argument Constructor (Used by CacheFactory)
    public RedisCacheStrategy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void put(String key, PipelinePayload<T> value) {
        // Implementation would go here (Jedis/Lettuce)
        log.info("🔴 [REDIS] SET {} = {}", key, value.id());
    }

    @Override
    public Optional<PipelinePayload<T>> get(String key) {
        log.info("🔴 [REDIS] GET {}", key);
        return Optional.empty(); // Return empty for simulation
    }

    @Override
    public void remove(String key) {
        log.info("🔴 [REDIS] DEL {}", key);
    }
}