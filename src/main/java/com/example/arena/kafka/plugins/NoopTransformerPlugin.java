package com.example.arena.kafka.plugins;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.Transformer;
import com.example.arena.kafka.metrics.MetricsRuntime;
import com.example.arena.kafka.spi.TransformerPlugin;

public final class NoopTransformerPlugin implements TransformerPlugin {

    @Override
    public String id() {
        return "NOOP";
    }

    @Override
    public Transformer<String, String> create(PipelineConfig config, MetricsRuntime metrics) {
        return input -> input;
    }
}
