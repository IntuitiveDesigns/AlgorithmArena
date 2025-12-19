package com.example.arena.kafka.plugins;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.metrics.MetricsRuntime;
import com.example.arena.kafka.output.KafkaSink;
import com.example.arena.kafka.spi.SinkPlugin;

public final class KafkaSinkPlugin implements SinkPlugin {

    @Override
    public String id() {
        return "KAFKA";
    }

    @Override
    public OutputSink<String> create(PipelineConfig config, MetricsRuntime metrics) {
        String topic = config.getProperty("sink.topic", "arena-bench-test");
        return KafkaSink.fromConfig(config, topic, metrics);
    }
}
