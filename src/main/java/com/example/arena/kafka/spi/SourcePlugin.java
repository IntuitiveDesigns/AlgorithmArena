package com.example.arena.kafka.spi;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.SourceConnector;
import com.example.arena.kafka.metrics.MetricsRuntime;

public interface SourcePlugin {
    String id(); // e.g. "SYNTHETIC", "KAFKA", "REST"
    SourceConnector<String> create(PipelineConfig config, MetricsRuntime metrics) throws Exception;
}
