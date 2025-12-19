package com.example.arena.kafka.plugins;

import com.example.arena.kafka.cache.LocalCacheStrategy;
import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.metrics.MetricsRuntime;
import com.example.arena.kafka.spi.CachePlugin;

public final class LocalCachePlugin implements CachePlugin {

    @Override
    public String id() {
        return "LOCAL";
    }

    @Override
    public CacheStrategy<String> create(PipelineConfig config, MetricsRuntime metrics) {
        return new LocalCacheStrategy<>();
    }
}
