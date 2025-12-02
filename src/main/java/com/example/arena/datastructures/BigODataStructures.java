package com.example.arena.datastructures;

public abstract class BigODataStructures {

    // 1. Make this final so it MUST be set once
    private final String dataStructureName;

    // 2. Force subclasses to provide a name in the constructor
    public BigODataStructures(String dataStructureName) {
        this.dataStructureName = dataStructureName;
    }

    public String getDataStructureName() {
        return dataStructureName;
    }

    // --- Time Complexity (Average) ---
    public abstract String getAvgAccess();
    public abstract String getAvgSearch();
    public abstract String getAvgInsertion();
    public abstract String getAvgDeletion();

    // --- Time Complexity (Worst) ---
    public abstract String getWorstAccess();
    public abstract String getWorstSearch();
    public abstract String getWorstInsertion();
    public abstract String getWorstDeletion();

    // --- NEW: Space Complexity (Critical for Interviews) ---
    // Example: Arrays are O(n), QuickSort is O(log n) stack space
    public abstract String getSpaceComplexity();
}