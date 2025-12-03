# Algorithm Arena ⚔️

**A Modern Java 21 Data Structures & Algorithms Benchmarking Framework.**

> "Theory tells you `O(N log N)` is fast. The Arena shows you *how* fast."

## 🚀 Overview
This repository is not just a collection of standard algorithms. It is an **architectural experiment** to modernize classic CS concepts using **Java 21** features (Records, Pattern Matching, Sealed Classes, Virtual Threads).

It features a **Polymorphic Benchmarking Arena** that allows you to pit algorithms against each other in real-time, verifying theoretical Big O complexity against actual execution time.

## 🌟 Unique Features

### 1. Self-Validating Contracts (`BigOAnalysis`)
Unlike standard implementations, every algorithm in this library MUST sign a "Performance Contract."
The architecture enforces that the developer explicitly defines the **Time** and **Space** complexity constraints before the code will even compile.

### 2. The Sorting Arena (`SortingComparison`)
A console-based CLI that benchmarks 15+ sorting algorithms against the exact same randomized datasets.
* **Features:** * Fair-play data cloning (every sort gets the same input).
    * Microsecond-level precision timing.
    * Recursive StackOverflow protection.
    * Verification logic (proves the array is actually sorted).

### 3. Modern Java 21 Syntax
Legacy patterns have been refactored into modern standards:
* **Records:** Used for immutable data models.
* **Switch Expressions:** Used for pattern matching in logic gates.
* **Virtual Threads:** Used for high-throughput concurrency simulations.
* **Lambda Evolution:** Demonstrating the shift from Strategy Pattern to Functional Interfaces.

## 🛠️ The Tech Stack
* **Language:** Java 21 (LTS)
* **Build System:** Gradle 8.5
* **Frameworks:** Spring Boot 3.2 (Bootstrapping), Lombok

## 📂 Project Structure
* `com.example.arena.sorting` - The Benchmarking Arena and Decorators.
* `com.example.arena.datastructures` - Custom implementations (Heap, Graph, BST) extending the Big O contract.
* `com.example.arena.algorithms` - Interview Patterns (Sliding Window, Two Pointers).
* `com.example.arena.hackerrank` - Solutions to complex problems (Trapping Rain Water, Group Anagrams).
* `com.example.arena.error` - A "Chaos Monkey" simulator for debugging CPU spikes and Memory Leaks.

## ⚡ How to Run
1. **Clone the repo:**
   ```bash
   git clone [https://github.com/IntuitiveDesigns/AlgorithmArena]
