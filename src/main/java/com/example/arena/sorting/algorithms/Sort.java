package com.example.arena.sorting.algorithms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// 1. Correct Package Import
import com.example.arena.sorting.BigOSort;

public class Sort extends BigOSort {

    // 2. Constructor
    public Sort() {
        super("Java Library Sort (Arrays.sort)");
    }

    // 3. Logic for Primitive Arrays (Dual-Pivot Quicksort)
    @Override
    public void run(int[] arr) {
        if (arr != null) {
            Arrays.sort(arr);
        }
    }

    // 4. Logic for Object Lists (TimSort - Hybrid of Merge & Insertion)
    public List<Integer> sortIntegers(List<Integer> arr, boolean descending){
        if (arr == null) return null;

        if (descending) {
            Collections.sort(arr, Collections.reverseOrder());
        } else {
            Collections.sort(arr);
        }
        return arr;
    }

    // --- BIG O CONTRACT (Reflecting Dual-Pivot Quicksort) ---

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
        return "O(n^2)"; // Quicksort worst case (extremely rare in Dual-Pivot)
    }

    @Override
    public String getSpaceComplexity() {
        return "O(log n)"; // Stack space for Quicksort
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Sort sorter = new Sort();

        // Test 1: Primitive Array (Arrays.sort)
        int[] primitiveArr = {9, 2, 7, 1};
        System.out.println("Running " + sorter.getSortName());
        sorter.run(primitiveArr);
        System.out.println("Sorted Primitives: " + Arrays.toString(primitiveArr));

        // Test 2: List (Collections.sort)
        List<Integer> list = Arrays.asList(9, 2, 7, 1);
        System.out.println("Sorting List Descending...");
        sorter.sortIntegers(list, true);
        System.out.println("Sorted List: " + list);
    }
}