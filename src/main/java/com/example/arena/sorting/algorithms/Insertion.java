package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Insertion extends BigOSort {

    // 1. Constructor
    public Insertion() {
        super("Insertion Sort");
    }

    // 2. Logic (Standard Java Syntax int[] arr)
    @Override
    public void run(int[] arr) {
        int n = arr.length;

        // Start from the second element (Index 1)
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        // Occurs when array is already sorted
        return "O(n)";
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
        Insertion sorter = new Insertion();
        int[] input = {12, 11, 13, 5, 6};

        System.out.println("Running " + sorter.getSortName());
        sorter.run(input);

        System.out.println("Sorted: " + Arrays.toString(input));
    }
}