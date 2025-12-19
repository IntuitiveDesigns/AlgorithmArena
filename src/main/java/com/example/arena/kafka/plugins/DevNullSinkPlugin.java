package com.example.arena.kafka.plugins;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.metrics.MetricsRuntime;
import com.example.arena.kafka.spi.SinkPlugin;

public final class DevNullSinkPlugin implements SinkPlugin {

    @Override
    public String id() {
        return "DEVNULL";
    }

    @Override
    public OutputSink<String> create(PipelineConfig config, MetricsRuntime metrics) {
        return payload -> { /* intentionally no-op */ };
    }
}
