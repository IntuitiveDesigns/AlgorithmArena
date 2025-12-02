package com.example.arena.sorting.algorithms;

// IMPORT the sorting class
import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Bubble extends BigOSort {

    public Bubble() {
        super("Bubble Sort");
    }

    @Override
    public void run(int[] arr) {
        // ... (Your implementation logic stays the same) ...
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    @Override
    public String getBestTime() { return "O(n)"; }

    @Override
    public String getAverageTime() { return "O(n^2)"; }

    @Override
    public String getWorstTime() { return "O(n^2)"; }

    @Override
    public String getSpaceComplexity() { return "O(1)"; }

    public static void main(String[] args) {
        Bubble b = new Bubble();
        int[] nums = {5, 2, 9, 1};
        b.run(nums);
        System.out.println(Arrays.toString(nums));
    }
}