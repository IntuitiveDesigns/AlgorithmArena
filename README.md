# Algorithm Arena ⚔️
## A Modern Java 21 Data Structures & Algorithms Benchmarking Framework

> "Theory tells you O(N log N) is fast. The Arena shows you how fast."

## 🚀 Overview

Algorithm Arena is not just a collection of algorithms. It is a modernization of classic CS concepts using Java 21 features such as Records, Pattern Matching, Sealed Classes, and Virtual Threads.

At its core is a **Polymorphic Benchmarking Arena** that allows you to compare algorithms in real time, validating theoretical Big-O complexity against real execution performance.

---

## 🌟 Unique Features

### 1. Self-Validating Complexity Contracts (`BigOAnalysis`)

Every algorithm must explicitly declare its **time and space complexity** before the program will compile.  
This guarantees consistent, transparent, and enforceable performance expectations.

---

### 2. The Sorting Arena (`SortingComparison`)

A console-driven benchmarking tool comparing 15+ sorting algorithms under identical conditions.

**Features:**

- Identical randomized datasets for fair comparison
- Microsecond-precision timers
- Recursive stack-overflow protection
- Sorting verification to ensure correctness

---

### 3. Enterprise Event Pipeline (`KafkaApp`)

A production-grade event pipeline capable of **25,000+ events/sec** on local hardware.

**Highlights:**

- **Strategy Pattern** architecture for Sources, Sinks, and Transformers
- **Dead Letter Queue (DLQ)** isolation for poison-pill events
- **Micrometer metrics + SLF4J structured logging** for observability
- Event-sourcing design with fast replay capability

---

### 4. Modern Java 21 Syntax

Refactored to use Java’s newest language features:

- Records for immutable data models (e.g., `PipelinePayload`)
- Pattern-matching switch expressions
- Virtual Threads for massive concurrency
- Functional interfaces demonstrating Strategy Pattern evolution

---

## 🛠️ Tech Stack

- **Language:** Java 21 (LTS)
- **Build System:** Gradle 8.5
- **Frameworks:** Spring Boot 3.2, Lombok
- **Messaging:** Apache Kafka (NVMe-optimized), LZ4 compression
- **Observability:** Micrometer, Logback structured logging

---

## 📂 Project Structure

```text
com.example.arena.kafka
    High-throughput event pipeline (Producer, Consumer, Orchestrator)

com.example.arena.sorting
    Benchmarking Arena and decorators

com.example.arena.datastructures
    Custom DS implementations (Heap, Graph, BST) with Big-O contracts

com.example.arena.algorithms
    Interview patterns (Sliding Window, Two Pointers)

com.example.arena.hackerrank
    Complex problem implementations (Trapping Rain Water, Group Anagrams)

com.example.arena.error
    Chaos Monkey simulator for CPU spikes and memory leaks
```

---

## ⚡ High-Performance Kafka Architecture

The Arena includes a Kafka setup optimized for:

- Local NVMe/SSD
- High-throughput ingestion
- Fast historical replay
- Zero external dependencies (no Redis, no S3)

Kafka acts as the **system of record**.

---

## 📂 Configuration Layout

- **Broker Configuration:** `docker-compose.yaml`
- **Client Configuration:** `src/main/resources/*.properties`

---

## 🛠️ Setup Instructions

### 1. Configure the Kafka Broker (Infrastructure)

The included `docker-compose.yaml` contains optimized settings:

**Key Optimizations**

- IO Threads: 8
- Network Threads: 12
- Log Segments: 1GB

Start infrastructure:

```bash
docker-compose up -d
```

---

### 2. Configure the Java Client

The application automatically loads:

- LZ4 compression
- 128 KB batching
- 1 MB+ consumer fetch sizes

No additional setup required.

---

### 3. Storage Requirement

Because `log.retention.bytes` is configured as a hard cap, ensure the Kafka log directory has **at least 500 GB** of free space.

---

## 🚀 How to Run

### 1. Start Infrastructure

```bash
docker-compose up -d
```

### 2. Start the Pipeline (Consumer)

```bash
./gradlew bootRun
```

This starts the KafkaApp orchestrator which waits for incoming events.

### 3. Generate Load (Producer)

Run from your IDE:

```text
com.example.arena.kafka.ArenaProducer.main()
```

This floods the pipeline with 25k+ events/sec (depending on local hardware).

---

## 🏁 Final Notes

Algorithm Arena combines:

- A benchmark lab for algorithms and data structures
- An enterprise-style event pipeline
- A modern Java 21 feature showcase

Use it to explore how theoretical performance translates into reality in a real-world architecture.