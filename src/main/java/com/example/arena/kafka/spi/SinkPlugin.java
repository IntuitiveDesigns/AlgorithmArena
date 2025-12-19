package com.example.arena.kafka.spi;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.metrics.MetricsRuntime;

public interface SinkPlugin {
    String id(); // e.g. "KAFKA", "DEVNULL", "POSTGRES", "DLQ_LOG"
    OutputSink<String> create(PipelineConfig config, MetricsRuntime metrics) throws Exception;
}
