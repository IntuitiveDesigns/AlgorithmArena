package com.example.arena.kafka.spi;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.Transformer;
import com.example.arena.kafka.metrics.MetricsRuntime;

public interface TransformerPlugin {
    String id(); // e.g. "NOOP", "UPPER"
    Transformer<String, String> create(PipelineConfig config, MetricsRuntime metrics) throws Exception;
}
