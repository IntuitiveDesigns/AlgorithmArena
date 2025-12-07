package com.example.arena.kafka.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Orchestrates the flow:
 *
 *  SourceConnector -> Transformer -> OutputSink (-> DLQ on failure)
 *
 * Uses Java 21 Virtual Threads for high-concurrency, low-overhead processing.
 *
 * @param <I> Input data type from the Source
 * @param <O> Output data type sent to the primary Sink
 */
public class PipelineOrchestrator<I, O> {

    private static final Logger log = LoggerFactory.getLogger(PipelineOrchestrator.class);

    private final SourceConnector<I> source;
    private final OutputSink<O> primarySink;
    private final OutputSink<I> dlqSink;
    private final Transformer<I, O> transformer;
    private final CacheStrategy<I> cache;

    /**
     * Maximum number of in-flight tasks allowed at once.
     * This provides simple backpressure and protects the JVM.
     */
    private final int maxInFlight;

    private volatile boolean running = false;
    private Thread dispatcherThread;

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
        this.maxInFlight = maxInFlight;
    }

    /**
     * Convenience constructor with a default maxInFlight.
     */
    public PipelineOrchestrator(SourceConnector<I> source,
                                OutputSink<O> primarySink,
                                OutputSink<I> dlqSink,
                                Transformer<I, O> transformer,
                                CacheStrategy<I> cache) {
        this(source, primarySink, dlqSink, transformer, cache, 64);
    }

    /**
     * Start the pipeline:
     * - Initializes source and cache
     * - Spins up the dispatcher loop on a dedicated thread
     */
    public void start() {
        if (running) {
            log.warn("Pipeline already running.");
            return;
        }

        try {
            log.info("Connecting SourceConnector...");
            source.connect();
        } catch (Exception e) {
            // In case any implementation throws unchecked
            log.error("Failed to connect SourceConnector", e);
            throw new RuntimeException("Failed to start pipeline: source.connect() failed", e);
        }

        try {
            log.info("Initializing CacheStrategy...");
            cache.init();
        } catch (Exception e) {
            log.error("Failed to initialize CacheStrategy", e);
            throw new RuntimeException("Failed to start pipeline: cache.init() failed", e);
        }

        running = true;
        log.info("Starting Parallel Pipeline (Virtual Threads), maxInFlight={}", maxInFlight);

        dispatcherThread = new Thread(this::run, "pipeline-dispatcher");
        dispatcherThread.start();
    }

    /**
     * Stop the pipeline gracefully:
     * - Signals dispatcher loop to stop
     * - Waits briefly for it to join
     * - Disconnects source and closes cache
     */
    public void stop() {
        if (!running) {
            log.warn("Pipeline already stopped.");
            return;
        }

        log.info("Stopping Pipeline...");
        running = false;

        try {
            if (dispatcherThread != null && dispatcherThread.isAlive()) {
                dispatcherThread.interrupt();
                dispatcherThread.join(5000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while waiting for dispatcher thread to stop", e);
        }

        try {
            log.info("Disconnecting SourceConnector...");
            source.disconnect();
        } catch (Exception e) {
            log.error("Error while disconnecting source", e);
        }

        try {
            log.info("Closing CacheStrategy...");
            cache.close();
        } catch (Exception e) {
            log.error("Error while closing cache", e);
        }

        log.info("Pipeline stopped.");
    }

    /**
     * Dispatcher loop:
     * - Uses a virtual-thread executor
     * - Uses a Semaphore to bound in-flight tasks (backpressure)
     * - Continuously polls the SourceConnector for new events
     */
    private void run() {
        Semaphore inFlight = new Semaphore(maxInFlight);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            while (running && !Thread.currentThread().isInterrupted()) {

                PipelinePayload<I> inputPayload;
                try {
                    inputPayload = source.fetch();
                } catch (Exception e) {
                    log.error("Error fetching from SourceConnector", e);
                    // brief backoff on fetch error
                    sleepQuietly(10);
                    continue;
                }

                if (inputPayload == null) {
                    // No data right now, back off a bit
                    sleepQuietly(5);
                    continue;
                }

                // Acquire permit – backpressure if too many in-flight
                try {
                    inFlight.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                final PipelinePayload<I> taskPayload = inputPayload;
                executor.submit(() -> {
                    try {
                        processEvent(taskPayload);
                    } finally {
                        inFlight.release();
                    }
                });
            }
        } catch (Exception e) {
            log.error("Pipeline execution encountered a fatal error", e);
        }

        log.info("Dispatcher loop exited.");
    }

    /**
     * Core event processing:
     * - Optionally interacts with CacheStrategy (you can extend this later)
     * - Applies Transformer
     * - Writes to primary sink
     * - Routes failures to DLQ sink
     */
    private void processEvent(PipelinePayload<I> inputPayload) {
        try {
            // If you want to use cache, e.g.:
            // cache.put(inputPayload.id(), inputPayload);

            PipelinePayload<O> resultPayload = transformer.transform(inputPayload);
            primarySink.write(resultPayload);

        } catch (Exception e) {
            log.error("❌ Failed processing ID: {}", inputPayload.id(), e);
            try {
                dlqSink.write(inputPayload);
            } catch (Exception dlqError) {
                log.error("💀 DLQ write failed for ID: {}", inputPayload.id(), dlqError);
            }
        }
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
