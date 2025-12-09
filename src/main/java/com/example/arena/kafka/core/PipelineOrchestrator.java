package com.example.arena.kafka.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Core orchestration engine.
 *
 * - Pulls events from a SourceConnector
 * - Dispatches them to virtual threads (via an ExecutorService)
 * - Applies the Transformer
 * - Writes to the primary OutputSink, or DLQ on failure
 *
 * @param <I> Input payload type
 * @param <O> Output payload type
 */
public class PipelineOrchestrator<I, O> {

    private static final Logger log = LoggerFactory.getLogger(PipelineOrchestrator.class);

    private final SourceConnector<I> source;
    private final OutputSink<O> primarySink;
    private final OutputSink<I> dlqSink;
    private final Transformer<I, O> transformer;
    private final CacheStrategy<I> cache;
    private final int maxInFlight;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private ExecutorService executor;
    private Thread dispatcherThread;
    private Semaphore inFlightLimiter;

    public PipelineOrchestrator(SourceConnector<I> source,
                                OutputSink<O> primarySink,
                                OutputSink<I> dlqSink,
                                Transformer<I, O> transformer,
                                CacheStrategy<I> cache,
                                int maxInFlight) {

        this.source = Objects.requireNonNull(source, "source");
        this.primarySink = Objects.requireNonNull(primarySink, "primarySink");
        this.dlqSink = Objects.requireNonNull(dlqSink, "dlqSink");
        this.transformer = Objects.requireNonNull(transformer, "transformer");
        this.cache = Objects.requireNonNull(cache, "cache");

        if (maxInFlight <= 0) {
            throw new IllegalArgumentException("maxInFlight must be > 0");
        }
        this.maxInFlight = maxInFlight;
    }

    /**
     * Starts the orchestrator:
     * - connects the source
     * - spins up the virtual-thread executor
     * - launches a dispatcher loop on a dedicated thread
     */
    public void start() {
        if (!running.compareAndSet(false, true)) {
            log.warn("PipelineOrchestrator.start() called but pipeline is already running");
            return;
        }

        log.info("Connecting SourceConnector...");
        source.connect();

        log.info("Initializing CacheStrategy...");
        this.inFlightLimiter = new Semaphore(maxInFlight);

        // Java 21 virtual threads
        this.executor = Executors.newVirtualThreadPerTaskExecutor();

        log.info("Starting Parallel Pipeline (Virtual Threads), maxInFlight={}", maxInFlight);

        this.dispatcherThread = new Thread(this::runDispatcherLoop, "pipeline-dispatcher");
        this.dispatcherThread.setDaemon(true);
        this.dispatcherThread.start();
    }

    /**
     * Requests a graceful shutdown:
     * - stops the dispatcher loop
     * - waits briefly for it to finish
     * - shuts down the executor
     * - disconnects the source
     */
    public void stop() {
        if (!running.compareAndSet(true, false)) {
            log.warn("PipelineOrchestrator.stop() called but pipeline is not running");
            return;
        }

        log.info("Stopping Pipeline...");

        try {
            if (dispatcherThread != null) {
                dispatcherThread.join(5_000L);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while waiting for dispatcher to stop", e);
        }

        if (executor != null) {
            executor.shutdown();
        }

        source.disconnect();
        log.info("Pipeline stopped.");
    }

    // -------------------------------------------------------------------------
    // INTERNAL DISPATCH LOOP
    // -------------------------------------------------------------------------

    private void runDispatcherLoop() {
        try {
            while (running.get()) {
                PipelinePayload<I> inputPayload = source.fetch();

                if (inputPayload == null) {
                    // Back off very briefly if there is no data
                    try {
                        Thread.sleep(5L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    continue;
                }

                // Acquire a permit before dispatching to avoid unbounded in-flight tasks.
                inFlightLimiter.acquireUninterruptibly();

                executor.submit(() -> {
                    try {
                        processEvent(inputPayload);
                    } finally {
                        // Always release, even on failure
                        inFlightLimiter.release();
                    }
                });
            }
        } catch (Exception e) {
            log.error("Fatal error in dispatcher loop; stopping pipeline", e);
            running.set(false);
        }
    }

    private void processEvent(PipelinePayload<I> inputPayload) {
        try {
            PipelinePayload<O> resultPayload = transformer.transform(inputPayload);
            primarySink.write(resultPayload);
        } catch (Exception e) {
            // send to DLQ, but never let an exception kill the orchestrator
            log.error("❌ Failed ID: {}", inputPayload.id(), e);
            try {
                dlqSink.write(inputPayload);
            } catch (Exception dlqError) {
                log.error("💀 DLQ write also failed for ID: {}", inputPayload.id(), dlqError);
            }
        }
    }
}
