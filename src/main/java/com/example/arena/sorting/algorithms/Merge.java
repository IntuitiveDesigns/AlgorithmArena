package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Merge extends BigOSort {

    // 1. Constructor
    public Merge() {
        super("Merge Sort");
    }

    // 2. Main Entry Point (Called by Test Runner)
    @Override
    public void run(int[] arr) {
        if (arr.length > 1) {
            // Kick off recursive sort from index 0 to end
            sort(arr, 0, arr.length - 1);
        }
    }

    // Helper: Recursive Sort Function
    private void sort(int[] arr, int l, int r) {
        if (l < r) {
            // Find the middle point
            // Optimization: Use (l + (r - l) / 2) to avoid integer overflow for huge arrays
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sort(arr, l, m);
            sort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    // Helper: Merges two subarrays of arr[]
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    private void merge(int[] arr, int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int[] L = new int[n1];
        int[] R = new int[n2];

        /* Copy data to temp arrays */
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        /* Merge the temp arrays */
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        return "O(n log n)"; // Always divides array in half
    }

    @Override
    public String getAverageTime() {
        return "O(n log n)";
    }

    @Override
    public String getWorstTime() {
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)"; // Requires temporary arrays L[] and R[]
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Merge sorter = new Merge();
        int[] input = {12, 11, 13, 5, 6, 7};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println("Sorted: " + Arrays.toString(input));
    }
}