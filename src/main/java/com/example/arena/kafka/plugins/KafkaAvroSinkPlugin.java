package com.example.arena.kafka.plugins;

import com.example.arena.kafka.config.PipelineConfig;
import com.example.arena.kafka.core.OutputSink;
import com.example.arena.kafka.metrics.MetricsRuntime;
import com.example.arena.kafka.output.KafkaAvroSink;
import com.example.arena.kafka.spi.SinkPlugin;

public final class KafkaAvroSinkPlugin implements SinkPlugin {

    @Override
    public String id() {
        return "KAFKA_AVRO";
    }

    @Override
    public OutputSink<String> create(PipelineConfig config, MetricsRuntime metrics) {
        String topic = config.getProperty("sink.topic", "arena-avro-default");
        // Delegate to the Sink class, matching your KafkaSink style
        return KafkaAvroSink.fromConfig(config, topic, metrics);
    }
}