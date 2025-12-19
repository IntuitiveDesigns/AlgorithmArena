package com.example.arena.kafka.plugins;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.metrics.MetricsRuntime;
import com.example.arena.kafka.spi.SinkPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DlqLogSinkPlugin implements SinkPlugin {

    private static final Logger log = LoggerFactory.getLogger(DlqLogSinkPlugin.class);

    @Override
    public String id() {
        return "DLQ_LOG";
    }

    @Override
    public OutputSink<String> create(PipelineConfig config, MetricsRuntime metrics) {
        return payload -> log.warn("⚠️ [DLQ] Dropped Event ID: {}", payload.id());
    }
}
