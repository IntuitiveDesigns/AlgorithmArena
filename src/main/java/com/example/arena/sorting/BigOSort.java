package com.example.arena.sorting;

public abstract class BigOSort {

    private final String sortName;

    // Force the subclass to name itself
    public BigOSort(String sortName) {
        this.sortName = sortName;
    }

    public String getSortName() {
        return sortName;
    }

    // Standard run method for all sorts
    public abstract void run(int[] arr);

    // --- TIME COMPLEXITY ---
    public abstract String getBestTime();
    public abstract String getAverageTime();
    public abstract String getWorstTime();

    // --- SPACE COMPLEXITY ---
    public abstract String getSpaceComplexity();

    public String bigOContract() {
        return new StringBuilder()
                .append("Algorithm:         ").append(getSortName()).append(System.lineSeparator())
                .append("Best case time:    ").append(getBestTime()).append(System.lineSeparator())
                .append("Average case time: ").append(getAverageTime()).append(System.lineSeparator())
                .append("Worst case time:   ").append(getWorstTime()).append(System.lineSeparator())
                .append("Space complexity:  ").append(getSpaceComplexity())
                .toString();
    }
}