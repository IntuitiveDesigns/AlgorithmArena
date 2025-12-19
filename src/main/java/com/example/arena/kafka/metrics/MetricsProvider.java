package com.example.arena.kafka.metrics;

public interface MetricsProvider {
    String type(); // "PROMETHEUS" etc
    MetricsRuntime create(MetricsSettings settings);
}
