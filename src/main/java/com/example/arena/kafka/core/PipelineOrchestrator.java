package com.example.arena.kafka.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipelineOrchestrator<I, O> {

    private static final Logger log = LoggerFactory.getLogger(PipelineOrchestrator.class);

    private final SourceConnector<I> source;
    private final OutputSink<O> primarySink;
    private final OutputSink<I> dlqSink;
    private final Transformer<I, O> transformer;
    private final CacheStrategy<I> cache;

    private volatile boolean running = true;
    private Thread workerThread;

    public PipelineOrchestrator(SourceConnector<I> source,
                                OutputSink<O> primarySink,
                                OutputSink<I> dlqSink,
                                Transformer<I, O> transformer,
                                CacheStrategy<I> cache) {
        this.source = source;
        this.primarySink = primarySink;
        this.dlqSink = dlqSink;
        this.transformer = transformer;
        this.cache = cache;
    }

    public void start() {
        source.connect(); // 1. Connect
        log.info("Starting Pipeline Orchestrator...");
        workerThread = new Thread(this::run, "pipeline-worker");
        workerThread.start();
    }

    public void stop() {
        log.info("Stopping Pipeline Orchestrator...");
        running = false;
        try {
            if (workerThread != null) workerThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            source.disconnect(); // 2. Disconnect
        }
    }

    private void run() {
        while (running) {
            // FIX: Use .fetch() instead of .read()
            PipelinePayload<I> inputPayload = source.fetch();

            if (inputPayload == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                continue;
            }

            try {
                // FIX: Pass Payload wrapper, not raw data
                PipelinePayload<O> resultPayload = transformer.transform(inputPayload);
                primarySink.write(resultPayload);

            } catch (Exception e) {
                log.error("❌ Processing Failed! ID: {}", inputPayload.id(), e);
                try {
                    // FIX: Write Payload to DLQ
                    dlqSink.write(inputPayload);
                } catch (Exception dlqError) {
                    log.error("💀 CRITICAL: DLQ Write Failed!", dlqError);
                }
            }
        }
    }
}