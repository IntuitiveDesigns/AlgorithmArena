package com.example.arena.kafka.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PipelineOrchestrator<I, O> {

    private static final Logger log = LoggerFactory.getLogger(PipelineOrchestrator.class);

    private final SourceConnector<I> source;
    private final OutputSink<O> primarySink;
    private final OutputSink<I> dlqSink;
    private final Transformer<I, O> transformer;
    private final CacheStrategy<I> cache;

    private volatile boolean running = true;
    private Thread dispatcherThread;

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
        source.connect();
        log.info("Starting Parallel Pipeline (Virtual Threads)...");
        // Run the "Fetch Loop" on a standard OS thread
        dispatcherThread = new Thread(this::run, "pipeline-dispatcher");
        dispatcherThread.start();
    }

    public void stop() {
        log.info("Stopping Pipeline...");
        running = false;
        try {
            if (dispatcherThread != null) dispatcherThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            source.disconnect();
        }
    }

    private void run() {
        // JAVA 21 FEATURE: Virtual Threads
        // Creates a new lightweight thread for every single task submitted.
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            while (running) {
                // 1. FETCH (Synchronous)
                // We read one-by-one to control memory pressure
                PipelinePayload<I> inputPayload = source.fetch();

                if (inputPayload == null) {
                    try { Thread.sleep(10); } catch (InterruptedException e) { break; }
                    continue;
                }

                // 2. DISPATCH (Asynchronous)
                // We hand off processing immediately and go back to fetch the next item.
                executor.submit(() -> processEvent(inputPayload));
            }
        } // Executor waits for all threads to complete before closing
    }

    private void processEvent(PipelinePayload<I> inputPayload) {
        try {
            // A. Transform
            PipelinePayload<O> resultPayload = transformer.transform(inputPayload);

            // B. Sink (Must be thread-safe, KafkaProducer is)
            primarySink.write(resultPayload);

        } catch (Exception e) {
            log.error("❌ Failed ID: {}", inputPayload.id(), e);
            try {
                dlqSink.write(inputPayload);
            } catch (Exception dlqError) {
                log.error("💀 DLQ Failed", dlqError);
            }
        }
    }
}