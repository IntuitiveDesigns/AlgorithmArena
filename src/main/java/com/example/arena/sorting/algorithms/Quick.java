package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Quick extends BigOSort {

    // 1. Constructor
    public Quick() {
        super("Quick Sort");
    }

    // 2. Main Entry Point
    @Override
    public void run(int[] arr) {
        if (arr.length > 1) {
            sort(arr, 0, arr.length - 1);
        }
    }

    /*
     * Partitioning Logic (The Core of QuickSort)
     * This function takes last element as pivot, places the pivot element at its
     * correct position in sorted array, and places all smaller elements
     * to left of pivot and all greater elements to right of pivot.
     */
    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1); // index of smaller element

        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (arr[j] <= pivot) {
                i++;

                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    /*
     * The main recursive function
     * arr[] --> Array to be sorted,
     * low --> Starting index, high --> Ending index
     */
    private void sort(int[] arr, int low, int high) {
        if (low < high) {
            /*
             * pi is partitioning index, arr[pi] is now at right place
             */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        return "O(n log n)";
    }

    @Override
    public String getAverageTime() {
        return "O(n log n)";
    }

    @Override
    public String getWorstTime() {
        return "O(n^2)"; // Occurs if array is already sorted (and we pick last element as pivot)
    }

    @Override
    public String getSpaceComplexity() {
        return "O(log n)"; // Recursion stack
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Quick sorter = new Quick();
        int[] input = {10, 7, 8, 9, 1, 5};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println("Sorted: " + Arrays.toString(input));
    }
}