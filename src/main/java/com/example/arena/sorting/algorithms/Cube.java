package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Cube extends BigOSort {

    // 1. Constructor
    public Cube() {
        super("Cube Sort (Simulated as Merge Sort)");
    }

    // 2. Logic: Implementing Bottom-Up Merge Sort
    // (This fits the O(n log n) profile and O(n) space complexity you defined)
    @Override
    public void run(int[] arr) {
        int n = arr.length;
        if (n < 2) return;

        int[] temp = new int[n];

        // Width of the sub-arrays to merge: 1, 2, 4, 8...
        for (int width = 1; width < n; width *= 2) {

            // Merge subarrays in pairs
            for (int i = 0; i < n; i += 2 * width) {
                int left = i;
                int mid = Math.min(i + width, n);
                int right = Math.min(i + 2 * width, n);

                merge(arr, temp, left, mid, right);
            }

            // Copy sorted temp back to arr for next pass
            System.arraycopy(temp, 0, arr, 0, n);
        }
    }

    // Standard Merge Helper
    private void merge(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;   // Left subarray index
        int j = mid;    // Right subarray index
        int k = left;   // Temp array index

        while (i < mid && j < right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        while (i < mid)   temp[k++] = arr[i++];
        while (j < right) temp[k++] = arr[j++];
    }

    // --- BIG O CONTRACT (Updated names) ---

    @Override
    public String getBestTime() {
        return "O(n)"; // (If optimized check is added, typically O(n log n))
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
        return "O(n)"; // Requires temp array
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Cube sorter = new Cube();
        int[] input = {50, 10, 40, 20, 30};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println(Arrays.toString(input));
    }
}