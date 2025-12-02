package com.example.arena.model;

import java.time.Duration;
import java.time.Instant;

public record Time(Instant startTime, Instant endTime) {

    // Helper to calculate duration on the fly
    public Duration duration() {
        if (startTime == null || endTime == null) return Duration.ZERO;
        return Duration.between(startTime, endTime);
    }
}