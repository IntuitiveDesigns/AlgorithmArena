package com.example.arena.kafka.spi;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.CacheStrategy;
import com.example.arena.kafka.metrics.MetricsRuntime;

public interface CachePlugin {
    String id(); // e.g. "NOOP", "LOCAL", "REDIS"
    CacheStrategy<String> create(PipelineConfig config, MetricsRuntime metrics) throws Exception;
}
