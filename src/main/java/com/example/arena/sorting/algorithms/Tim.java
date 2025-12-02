package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Tim extends BigOSort {

    private static final int RUN = 32;

    // 1. Constructor
    public Tim() {
        super("Tim Sort");
    }

    // 2. Logic
    @Override
    public void run(int[] arr) {
        int n = arr.length;

        // Step 1: Sort individual subarrays of size RUN using Insertion Sort
        for (int i = 0; i < n; i += RUN) {
            insertionSort(arr, i, Math.min((i + RUN - 1), (n - 1)));
        }

        // Step 2: Merge the sorted runs using Merge Sort logic
        // Start merging from size RUN (32). merge 32+32=64, then 64+64=128, etc.
        for (int size = RUN; size < n; size = 2 * size) {

            // Pick starting point of left sub array. We are going to merge arr[left..left+size-1]
            // and arr[left+size, left+2*size-1]
            for (int left = 0; left < n; left += 2 * size) {

                // Find ending point of left sub array
                // mid+1 is starting point of right sub array
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));

                // Merge subarray arr[left...mid] & arr[mid+1...right]
                if (mid < right) {
                    merge(arr, left, mid, right);
                }
            }
        }
    }

    // Helper: Insertion sort for the small runs
    private void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int temp = arr[i];
            int j = i - 1;
            while (j >= left && arr[j] > temp) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = temp;
        }
    }

    // Helper: Standard Merge function
    private void merge(int[] arr, int l, int m, int r) {
        int len1 = m - l + 1;
        int len2 = r - m;

        int[] left = new int[len1];
        int[] right = new int[len2];

        for (int x = 0; x < len1; x++) left[x] = arr[l + x];
        for (int x = 0; x < len2; x++) right[x] = arr[m + 1 + x];

        int i = 0, j = 0, k = l;
        while (i < len1 && j < len2) {
            if (left[i] <= right[j]) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }
        while (i < len1) arr[k++] = left[i++];
        while (j < len2) arr[k++] = right[j++];
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        return "O(n)"; // If array is already sorted (Insertion sort detects this)
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
        return "O(n)"; // Needs temp arrays for merging
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Tim sorter = new Tim();
        int[] input = {5, 21, 7, 23, 19};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println("Sorted: " + Arrays.toString(input));
    }
}