# Changelog
All notable changes to this project will be documented in this file.

The format is based on **Keep a Changelog**  
and this project adheres to **Semantic Versioning**.

---

## [Unreleased]
### Added
- Connector **Service Provider Interfaces (SPI)**:  
  `SourceConnector`, `OutputSink`, `Transformer`, `CacheStrategy`
- New **PipelineOrchestrator** with virtual-thread parallelism and max-in-flight control
- **Avro serialization** support with **Confluent Schema Registry**
- Added example Avro schema: `CustomerEvent.avsc`
- **KafkaAvroSink** implementation
- **PostgreSQL SinkConnector** (`PostgresSink`)
- **SyntheticSource** for deterministic & high-throughput ingestion testing
- **SinkBenchmarkApp** and **SourceBenchmarkApp** to independently measure throughput
- **ArenaProducer** and **ArenaConsumer** helpers for connector validation
- Added new configuration files:
    - `pipeline.properties`
    - `pipeline-sink-bench.properties`
    - `pipeline-source-bench.properties`
- Added Docker Compose support for:
    - Kafka
    - Schema Registry
    - PostgreSQL

### Changed
- Refactored internal package structure to separate:
    - Connector SPI
    - Orchestrator runtime
    - Benchmarking utilities
- Updated `build.gradle` with new dependencies (Avro, Schema Registry, PostgreSQL driver)
- Improved Kafka producer/consumer defaults for higher throughput testing
- Updated logging for better observability

### Fixed
- Several build issues related to missing imports and package visibility
- Improved exception handling in sinks/sources to ensure clean shutdown behavior

---

## [0.1.0] - Initial Release
### Added
- Basic high-throughput pipeline with REST/Kafka source and Kafka sink
- `PipelinePayload` record with tracing ID + metadata
- LocalCacheStrategy
- Early version of PipelineOrchestrator
- Initial project skeleton, configuration system, and metrics endpoint
