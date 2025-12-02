package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Shell extends BigOSort {

    // 1. Constructor
    public Shell() {
        super("Shell Sort");
    }

    // 2. Logic
    @Override
    public void run(int[] arr) {
        int n = arr.length;

        // Start with a big gap, then reduce the gap
        // Standard Gap Sequence: n/2, n/4, ... 1
        for (int gap = n / 2; gap > 0; gap /= 2) {

            // Do a gapped insertion sort for this gap size.
            for (int i = gap; i < n; i += 1) {

                // Save a[i] in temp and make a hole at position i
                int temp = arr[i];

                // Shift earlier gap-sorted elements up until
                // the correct location for a[i] is found
                int j;
                for (j = i; j >= gap && arr[j - gap] > temp; j -= gap) {
                    arr[j] = arr[j - gap];
                }

                // Put temp (the original a[i]) in its correct location
                arr[j] = temp;
            }
        }
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        return "O(n log n)"; // Depends heavily on gap sequence
    }

    @Override
    public String getAverageTime() {
        return "O(n (log n)^2)";
    }

    @Override
    public String getWorstTime() {
        return "O(n (log n)^2)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(1)"; // In-place
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Shell sorter = new Shell();
        int[] input = {12, 34, 54, 2, 3};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println("Sorted: " + Arrays.toString(input));
    }
}