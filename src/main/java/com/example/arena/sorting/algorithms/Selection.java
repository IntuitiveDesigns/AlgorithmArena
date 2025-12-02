package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Selection extends BigOSort {

    // 1. Constructor
    public Selection() {
        super("Selection Sort");
    }

    // 2. Logic
    @Override
    public void run(int[] arr) {
        int n = arr.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {

            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[min_idx]) {
                    min_idx = j;
                }
            }

            // Swap the found minimum element with the first element
            int temp = arr[min_idx];
            arr[min_idx] = arr[i];
            arr[i] = temp;
        }
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        // UNLIKE Bubble Sort, Selection Sort is ALWAYS O(n^2).
        // It doesn't know if the array is sorted; it scans for the min anyway.
        return "O(n^2)";
    }

    @Override
    public String getAverageTime() {
        return "O(n^2)";
    }

    @Override
    public String getWorstTime() {
        return "O(n^2)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Selection sorter = new Selection();
        int[] input = {64, 25, 12, 22, 11};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println("Sorted: " + Arrays.toString(input));
    }
}