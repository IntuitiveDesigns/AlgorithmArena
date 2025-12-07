package com.example.arena.kafka.core;

import java.time.Instant;

/**
 * A pluggable source of events for the pipeline.
 *
 * Examples:
 * - Kafka topic consumer
 * - REST API poller
 * - S3 object reader
 * - Database change feed
 *
 * Contract:
 * - {@link #connect()} is called once before the pipeline starts reading.
 * - {@link #fetch()} is called repeatedly in a loop by the orchestrator.
 * - {@link #disconnect()} is called once when the pipeline stops.
 *
 * Implementations should prefer:
 * - Short, bounded blocking in {@link #fetch()} (but NOT infinite blocking)
 * - Returning {@code null} when no data is currently available
 *   so the orchestrator can back off briefly and try again.
 *
 * @param <T> Raw data type produced by this source.
 */
public interface SourceConnector<T> {

    /**
     * Initialize and connect to the underlying system.
     * <p>
     * Example responsibilities:
     * - Create HTTP/Kafka clients
     * - Open sockets
     * - Validate configuration
     */
    void connect();

    /**
     * Clean up any opened resources.
     * <p>
     * Example responsibilities:
     * - Close network connections
     * - Flush buffers
     * - Shutdown client libraries
     */
    void disconnect();

    /**
     * Fetch a single payload from the source.
     *
     * <p><b>Behavior Contract:</b></p>
     * <ul>
     *     <li>May block briefly while waiting for new data, but should not block indefinitely.</li>
     *     <li>Return {@code null} if no data is currently available.</li>
     *     <li>The orchestrator will call this method in a tight loop and apply its own backoff logic.</li>
     * </ul>
     *
     * @return the next {@link PipelinePayload}, or {@code null} if no data is available.
     */
    PipelinePayload<T> fetch();
}
