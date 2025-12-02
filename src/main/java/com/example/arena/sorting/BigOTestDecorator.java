package com.example.arena.sorting;

public class BigOTestDecorator extends BigOSort {

    // The sort algorithm we are wrapping (Decorating)
    private final BigOSort algorithm;

    public BigOTestDecorator(BigOSort algorithm) {
        // We pass the name up, but append " (Timed)" to show it's decorated
        super(algorithm.getSortName() + " (Timed)");
        this.algorithm = algorithm;
    }

    @Override
    public void run(int[] arr) {
        // 1. Capture Start Time
        long startTime = System.nanoTime();

        // 2. Run the actual algorithm
        algorithm.run(arr);

        // 3. Capture End Time & Print
        long duration = System.nanoTime() - startTime;

        // Convert to readable format (microseconds)
        double micros = duration / 1000.0;
        System.out.printf("[%s] Execution Time: %.3f µs%n", algorithm.getSortName(), micros);
    }

    // --- DELEGATE EVERYTHING ELSE TO THE WRAPPED ALGORITHM ---

    @Override
    public String getBestTime() {
        return algorithm.getBestTime();
    }

    @Override
    public String getAverageTime() {
        return algorithm.getAverageTime();
    }

    @Override
    public String getWorstTime() {
        return algorithm.getWorstTime();
    }

    @Override
    public String getSpaceComplexity() {
        return algorithm.getSpaceComplexity();
    }
}