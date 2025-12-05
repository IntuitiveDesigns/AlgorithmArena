package com.example.arena.kafka.ingestion;

import com.example.arena.kafka.core.PipelinePayload;
import com.example.arena.kafka.core.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class RestSourceConnector implements SourceConnector<String> {

    private static final Logger log = LoggerFactory.getLogger(RestSourceConnector.class);

    // Internal buffer to adapt "Batch API" to "Single Item Pipeline"
    private final Queue<PipelinePayload<String>> buffer = new LinkedList<>();

    @Override
    public void connect() {
        log.info("🌎 REST Source Connected (Mocking HTTP Connection)");
    }

    @Override
    public void disconnect() {
        log.info("🌎 REST Source Disconnected");
    }

    @Override
    public PipelinePayload<String> fetch() {
        // 1. If buffer has data, return it immediately
        if (!buffer.isEmpty()) {
            return buffer.poll();
        }

        // 2. If buffer is empty, "Call the API" to get more data
        fetchFromExternalApi();

        // 3. Return next item (or null if API is empty/slow)
        return buffer.poll();
    }

    // Simulate an HTTP Call that returns a batch of 5 items
    private void fetchFromExternalApi() {
        try {
            // Simulate Network Latency
            Thread.sleep(500);

            // Add mock data to buffer
            for (int i = 0; i < 5; i++) {
                String data = "API-Data-" + UUID.randomUUID().toString().substring(0,6);
                buffer.add(PipelinePayload.of(data));
            }
            log.debug("Refilled buffer with 5 items from REST API");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}