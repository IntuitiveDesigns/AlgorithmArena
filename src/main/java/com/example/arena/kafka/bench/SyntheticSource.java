package com.example.arena.kafka.bench;

import com.example.arena.kafka.core.PipelinePayload;
import com.example.arena.kafka.core.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Ultra-lightweight in-memory source used for SINK benchmarks.
 * Generates monotonically increasing "BENCH-<n>" payloads.
 */
public final class SyntheticSource implements SourceConnector<String> {

    private static final Logger log = LoggerFactory.getLogger(SyntheticSource.class);

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicLong counter = new AtomicLong(0L);

    @Override
    public void connect() {
        running.set(true);
        log.info("SyntheticSource connected (in-memory generator)");
    }

    @Override
    public void disconnect() {
        running.set(false);
        log.info("SyntheticSource disconnected");
    }

    @Override
    public PipelinePayload<String> fetch() {
        if (!running.get()) {
            return null;
        }
        long id = counter.getAndIncrement();
        return PipelinePayload.of("BENCH-" + id);
    }
}
