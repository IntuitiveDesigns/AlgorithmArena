package com.example.arena.kafka.plugins;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.core.PipelinePayload;
import com.example.arena.kafka.metrics.MetricsRuntime;
import com.example.arena.kafka.spi.CachePlugin;

import java.util.Optional;

public final class NoopCachePlugin implements CachePlugin {

    @Override
    public String id() {
        return "NOOP";
    }

    @Override
    public CacheStrategy<String> create(PipelineConfig config, MetricsRuntime metrics) {
        return new CacheStrategy<>() {
            @Override public void put(String key, PipelinePayload<String> value) {}
            @Override public Optional<PipelinePayload<String>> get(String key) { return Optional.empty(); }
            @Override public void remove(String key) {}
        };
    }
}
