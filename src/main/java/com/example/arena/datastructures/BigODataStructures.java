package com.example.arena.datastructures;

public abstract class BigODataStructures {

    private final String dataStructureName;

    public BigODataStructures(String dataStructureName) {
        this.dataStructureName = dataStructureName;
    }

    public String getDataStructureName() {
        return dataStructureName;
    }

    // --- AVERAGE CASE ---
    public abstract String getAvgAccess();
    public abstract String getAvgSearch();
    public abstract String getAvgInsertion();
    public abstract String getAvgDeletion();

    // --- WORST CASE ---
    public abstract String getWorstAccess();
    public abstract String getWorstSearch();
    public abstract String getWorstInsertion();
    public abstract String getWorstDeletion();

    // --- SPACE COMPLEXITY ---
    public abstract String getSpaceComplexity();

    // Default “array/linear” view. Map-like structures can override this.
    public String bigOContract() {
        return new StringBuilder()
                .append("Data structure:          ").append(getDataStructureName()).append(System.lineSeparator())
                .append("Average access:          ").append(getAvgAccess()).append(System.lineSeparator())
                .append("Average search:          ").append(getAvgSearch()).append(System.lineSeparator())
                .append("Average insertion:       ").append(getAvgInsertion()).append(System.lineSeparator())
                .append("Average deletion:        ").append(getAvgDeletion()).append(System.lineSeparator())
                .append("Worst-case access:       ").append(getWorstAccess()).append(System.lineSeparator())
                .append("Worst-case search:       ").append(getWorstSearch()).append(System.lineSeparator())
                .append("Worst-case insertion:    ").append(getWorstInsertion()).append(System.lineSeparator())
                .append("Worst-case deletion:     ").append(getWorstDeletion()).append(System.lineSeparator())
                .append("Space complexity:        ").append(getSpaceComplexity())
                .toString();
    }
}
