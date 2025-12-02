package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Heap extends BigOSort {

    // 1. Constructor
    public Heap() {
        super("Heap Sort");
    }

    @Override
    public void run(int[] arr) {
        int n = arr.length;

        // Step 1: Build heap (rearrange array)
        // Start from the last non-leaf node and heapify down
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        // Step 2: One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root (Maximum value) to the end
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Call max heapify on the reduced heap
            heapify(arr, i, 0);
        }
    }

    // Helper: To heapify a subtree rooted with node i
    // n is size of heap
    private void heapify(int[] arr, int n, int i) {
        int largest = i;    // Initialize largest as root
        int l = 2 * i + 1;  // left = 2*i + 1
        int r = 2 * i + 2;  // right = 2*i + 2

        // If left child is larger than root
        if (l < n && arr[l] > arr[largest])
            largest = l;

        // If right child is larger than largest so far
        if (r < n && arr[r] > arr[largest])
            largest = r;

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
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
        return "O(n log n)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)"; // In-place sort (Unlike Merge Sort)
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Heap sorter = new Heap();
        int[] input = {12, 11, 13, 5, 6, 7};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println("Sorted: " + Arrays.toString(input));
    }
}