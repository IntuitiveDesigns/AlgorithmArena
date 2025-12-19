package com.example.arena.kafka.spi;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.metrics.MetricsRuntime;

public interface PipelinePlugin {
    String id();          // e.g. "KAFKA", "SYNTHETIC", "POSTGRES"
    PluginKind kind();    // SOURCE / SINK / TRANSFORMER / CACHE

    Object create(PipelineConfig config, MetricsRuntime metrics) throws Exception;
}
