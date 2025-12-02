package com.example.arena.sorting.algorithms;

// 1. Point to the correct Parent
import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Counting extends BigOSort {

    // 2. Constructor
    public Counting() {
        super("Counting Sort");
    }

    // 3. The Actual Implementation (For Integers)
    @Override
    public void run(int[] arr) {
        int n = arr.length;
        if (n == 0) return;

        // Step 1: Find the Maximum value to know the range (k)
        int max = Arrays.stream(arr).max().orElse(0);
        // Note: Counting sort usually assumes non-negative integers.
        // If negatives exist, we'd need to find 'min' and use an offset.

        // Step 2: Create Count Array
        int[] count = new int[max + 1];

        // Step 3: Store count of each number
        for (int i = 0; i < n; i++) {
            count[arr[i]]++;
        }

        // Step 4: Calculate Cumulative Count (Prefix Sum)
        // This allows us to place elements directly into their sorted position.
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }

        // Step 5: Build the Output Array
        // We iterate backwards to maintain Stability (order of duplicate items).
        int[] output = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int currentVal = arr[i];
            int position = count[currentVal] - 1;

            output[position] = currentVal;
            count[currentVal]--; // Decrement count for next duplicate
        }

        // Step 6: Copy Sorted Output back to Original Array
        System.arraycopy(output, 0, arr, 0, n);
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
        return "O(n + k)";
    }

    @Override
    public String getSpaceComplexity() {
        return "O(k)"; // We need an array of size 'max'
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Counting sorter = new Counting();
        int[] input = {4, 2, 2, 8, 3, 3, 1};

        System.out.println("Running " + sorter.getSortName());
        System.out.println("Original: " + Arrays.toString(input));

        sorter.run(input);

        System.out.println("Sorted:   " + Arrays.toString(input));
    }
}