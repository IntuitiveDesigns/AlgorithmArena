package com.example.arena.sorting.algorithms;

// 1. Point to the correct Parent package
import com.example.arena.sorting.BigOSort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bucket extends BigOSort {

    public Bucket() {
        super("Bucket Sort");
    }

    @Override
    public void run(int[] arr) {
        int n = arr.length;
        if (n <= 0) return;

        // Step 1: Find Max
        int maxVal = arr[0];
        for (int value : arr) {
            if (value > maxVal) maxVal = value;
        }

        // Step 2: Create Buckets
        int numberOfBuckets = (int) Math.sqrt(n);
        if (numberOfBuckets == 0) numberOfBuckets = 1;

        List<List<Integer>> buckets = new ArrayList<>(numberOfBuckets);
        for (int i = 0; i < numberOfBuckets; i++) {
            buckets.add(new ArrayList<>());
        }

        // Step 3: Distribute
        for (int value : arr) {
            // Safe casting to prevent overflow before division
            int bucketIndex = (int) ((long) value * numberOfBuckets / (maxVal + 1));
            buckets.get(bucketIndex).add(value);
        }

        // Step 4: Sort & Merge
        int index = 0;
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
            for (int value : bucket) {
                arr[index++] = value;
            }
        }
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        return "O(n + k)";
    }

    @Override
    public String getAverageTime() {
        return "O(n + k)";
    }

    @Override
    public String getWorstTime() {
        return "O(n^2)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Bucket sorter = new Bucket();
        int[] input = {29, 25, 3, 49, 9, 37, 21, 43};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        for (int i : input) System.out.print(i + " ");
    }
}