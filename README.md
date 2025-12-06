
# Algorithm Arena ⚔️
## A Modern Java 21 Data Structures & Algorithms Benchmarking Framework

> "Theory tells you O(N log N) is fast. The Arena shows you how fast."

## 🚀 Overview

Algorithm Arena modernizes classic CS concepts using Java 21 features such as Records, Pattern Matching, Sealed Classes, and Virtual Threads.

At its core is a **Polymorphic Benchmarking Arena** that compares algorithms in real time, validating theoretical Big‑O performance against real execution behavior.

---

## 🌟 Unique Features

### 1. Self‑Validating Complexity Contracts (`BigOAnalysis`)
Every algorithm declares its **time and space complexity** before compiling, ensuring transparency and consistency.

---

### 2. Sorting Arena (`SortingComparison`)
Benchmark 15+ sorting algorithms under controlled conditions.

**Includes:**
- Identical randomized datasets  
- Microsecond timers  
- Recursive overflow protection  
- Automatic sorting verification  

---

### 3. Enterprise Event Pipeline (`KafkaApp`)
A production‑grade ingestion pipeline capable of **200,000+ events/sec** using Java 21 Virtual Threads.

**Highlights:**
- Strategy‑pattern Sources, Sinks, Transformers  
- Dead Letter Queue (DLQ) for poison‑pill events  
- Prometheus metrics + Grafana dashboards  
- Fast replay and event‑sourcing architecture  

---

### 4. Modern Java 21 Syntax
- Immutable Records  
- Pattern‑matching switches  
- Virtual Threads for extreme concurrency  
- Clean functional interfaces  

---

# 📊 Observability: Prometheus + Grafana Integration

Algorithm Arena now includes a full observability stack to monitor real‑time throughput, latency, and saturation.

---

## 🚀 1. Access Grafana

After running Docker:

➡️ http://localhost:3000  
**Username:** admin  
**Password:** admin  

---

## 🔗 2. Connect Prometheus as a Data Source

1. Click **Add your first data source**  
2. Select **Prometheus**  
3. Set URL:

```
http://prometheus:9090
```

4. Click **Save & Test** → *“Data source is working.”*

---

## 📈 3. Build a Throughput Dashboard

1. **+ → New Dashboard**  
2. **Add visualization**  
3. Select **Prometheus**  
4. Query:

```
rate(pipeline_transform_latency_seconds_count[5s])
```

5. Click **Run queries**  
A throughput graph appears when the app is running.

---

## 🔥 4. See Throughput Live

Run:

```
./gradlew bootRun
```

Grafana updates instantly with EPS, latency, and performance curves.

---

## 📝 PURE JAVA vs KAFKA PERFORMANCE

| Mode | Measures | Notes |
|------|----------|-------|
| **sink.type=CONSOLE** | Pure Java engine throughput | Peaks above 200k EPS |
| **sink.type=KAFKA** | Full pipeline performance | Docker Desktop on Windows limits Kafka I/O |

> For real Kafka benchmarking, use Linux or a cloud deployment.

---

# 🛠️ Tech Stack
- Java 21  
- Gradle 8.5  
- Spring Boot 3.2  
- Kafka (NVMe optimized)  
- Prometheus  
- Grafana  
- Micrometer  

---

# 🛠️ Setup Instructions

## 1. Start Infrastructure
```
docker-compose up -d
```

## 2. Start the Pipeline
```
./gradlew bootRun
java -Xms4g -Xmx4g -jar .\build\libs\AlgorithmArena-0.0.1-SNAPSHOT.jar
```

## 3. Generate Load (Producer)
```
com.example.arena.kafka.ArenaProducer.main()
```

---

# 📁 Configuration Layout
- Infrastructure: `docker-compose.yaml`  
- Pipeline config: `src/main/resources/pipeline.properties`  

---

# 🏁 Final Notes
Algorithm Arena is a blend of:

- Algorithm benchmarking  
- Enterprise ingestion pipeline  
- Performance engineering  
- Modern Java 21 exploration  

GitHub: https://github.com/IntuitiveDesigns/AlgorithmArena
