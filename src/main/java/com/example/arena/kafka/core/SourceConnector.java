package com.example.arena.kafka.core;

public interface SourceConnector<T> {
    /**
     * Connect to the source system.
     */
    void connect();

    /**
     * Disconnect/Cleanup resources.
     */
    void disconnect();

    /**
     * Fetch a SINGLE payload (Blocking/Synchronous).
     * This matches the 'while(running)' loop in your Orchestrator.
     * * @return A single Payload, or null if no data is available.
     */
    PipelinePayload<T> fetch();
}