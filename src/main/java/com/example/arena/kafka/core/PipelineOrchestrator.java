package com.example.arena.kafka.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

public class PipelineOrchestrator<I, O> {

    private static final Logger log = LoggerFactory.getLogger(PipelineOrchestrator.class);

    private final SourceConnector<I> source;
    private final OutputSink<O> primarySink;
    private final OutputSink<I> dlqSink;
    private final Transformer<I, O> transformer;
    private final CacheStrategy<I> cache;

    private final int maxInFlight;
    private final Semaphore inFlight;

    private volatile boolean running = true;
    private Thread dispatcherThread;

    /**
     * Default: maxInFlight ≈ 4x CPU cores.
     */
    public PipelineOrchestrator(SourceConnector<I> source,
                                OutputSink<O> primarySink,
                                OutputSink<I> dlqSink,
                                Transformer<I, O> transformer,
                                CacheStrategy<I> cache) {
        this(source,
                primarySink,
                dlqSink,
                transformer,
                cache,
                Runtime.getRuntime().availableProcessors() * 4);
    }

    /**
     * Explicitly configurable parallelism.
     */
    public PipelineOrchestrator(SourceConnector<I> source,
                                OutputSink<O> primarySink,
                                OutputSink<I> dlqSink,
                                Transformer<I, O> transformer,
                                CacheStrategy<I> cache,
                                int maxInFlight) {
        this.source = source;
        this.primarySink = primarySink;
        this.dlqSink = dlqSink;
        this.transformer = transformer;
        this.cache = cache;
        this.maxInFlight = Math.max(1, maxInFlight);
        this.inFlight = new Semaphore(this.maxInFlight);
    }

    public void start() {
        source.connect();
        log.info("Starting Parallel Pipeline (Virtual Threads) with maxInFlight={} ...", maxInFlight);
        dispatcherThread = new Thread(this::run, "pipeline-dispatcher");
        dispatcherThread.start();
    }

    public void stop() {
        log.info("Stopping Pipeline...");
        running = false;
        if (dispatcherThread != null) {
            dispatcherThread.interrupt(); // wake up fetch / sleep
            try {
                dispatcherThread.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        source.disconnect();
        log.info("Pipeline stopped.");
    }

    private void run() {
        // Single shared virtual-thread executor.
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            while (running && !Thread.currentThread().isInterrupted()) {

                // 1. FETCH (may block; implementation-specific)
                PipelinePayload<I> inputPayload = source.fetch();

                if (inputPayload == null) {
                    // No data available right now – back off a bit.
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue;
                }

                // 2. BACKPRESSURE: limit number of in-flight tasks
                try {
                    inFlight.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                // 3. DISPATCH (Virtual Thread)
                executor.submit(() -> {
                    try {
                        processEvent(inputPayload);
                    } finally {
                        inFlight.release();
                    }
                });
            }

            // Wait for in-flight tasks to finish before closing executor
            // (executor.close() will also wait, but this makes intent explicit)
            while (inFlight.availablePermits() < maxInFlight) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        } catch (Exception e) {
            log.error("Pipeline run loop terminated with error", e);
        }
    }

    private void processEvent(PipelinePayload<I> inputPayload) {
        try {
            // Optional: simple cache hook (idempotency / dedupe)
            // var cached = cache.get(inputPayload.id());
            // if (cached.isPresent()) {
            //     return; // or use cached result
            // }

            PipelinePayload<O> resultPayload = transformer.transform(inputPayload);
            primarySink.write(resultPayload);

            // cache.put(inputPayload.id(), inputPayload);

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
