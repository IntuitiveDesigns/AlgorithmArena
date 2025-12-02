package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Radix extends BigOSort {

    // 1. Constructor
    public Radix() {
        super("Radix Sort");
    }

    // 2. Main Entry Point
    @Override
    public void run(int[] arr) {
        if (arr.length == 0) return;
        radixsort(arr, arr.length);
    }

    // --- HELPER METHODS ---

    // A utility function to get maximum value in arr[]
    // Needed to know how many digits we are dealing with.
    private int getMax(int[] arr, int n) {
        int mx = arr[0];
        for (int i = 1; i < n; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }

    // A function to do counting sort of arr[] according to
    // the digit represented by exp.
    private void countSort(int[] arr, int n, int exp) {
        int[] output = new int[n]; // output array
        int[] count = new int[10]; // 0-9 digits
        Arrays.fill(count, 0);

        // Store count of occurrences in count[]
        for (int i = 0; i < n; i++)
            count[(arr[i] / exp) % 10]++;

        // Change count[i] so that count[i] now contains
        // actual position of this digit in output[]
        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        // Build the output array
        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        // Copy the output array to arr[], so that arr[] now
        // contains sorted numbers according to current digit
        for (int i = 0; i < n; i++)
            arr[i] = output[i];
    }

    // The main function that sorts arr[] of size n using Radix Sort
    private void radixsort(int[] arr, int n) {
        // Find the maximum number to know number of digits
        int m = getMax(arr, n);

        // Do counting sort for every digit. Note that instead
        // of passing digit number, exp is passed. exp is 10^i
        // where i is current digit number
        for (int exp = 1; m / exp > 0; exp *= 10)
            countSort(arr, n, exp);
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        return "O(nk)"; // k is the number of digits
    }

    @Override
    public String getAverageTime() {
        return "O(nk)";
    }

    @Override
    public String getWorstTime() {
        return "O(nk)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n + k)";
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Radix sorter = new Radix();
        // Radix sort typically handles positive integers
        int[] input = {170, 45, 75, 90, 802, 24, 2, 66};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println("Sorted: " + Arrays.toString(input));
    }
}