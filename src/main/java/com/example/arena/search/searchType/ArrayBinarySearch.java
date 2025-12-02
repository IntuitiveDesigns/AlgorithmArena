package com.example.arena.search.searchType;

import com.example.arena.datastructures.BigODataStructures;
import java.util.Arrays;

// 1. Extend the Contract
public class ArrayBinarySearch extends BigODataStructures {

    public ArrayBinarySearch() {
        super("Binary Search (Sorted Array)");
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(1)"; } // Array access

    @Override
    public String getAvgSearch() { return "O(log n)"; } // Halving the search space

    @Override
    public String getAvgInsertion() { return "O(n)"; } // Arrays are fixed size, shifting needed

    @Override
    public String getAvgDeletion() { return "O(n)"; }

    @Override
    public String getWorstAccess() { return "O(1)"; }

    @Override
    public String getWorstSearch() { return "O(log n)"; }

    @Override
    public String getWorstInsertion() { return "O(n)"; }

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(1) Iterative"; }

    // --- LOGIC ---

    /**
     * METHOD 1: The "Easy Way" (Library)
     * Useful for production code, but "cheating" in an interview.
     */
    public void searchUsingLibrary() {
        System.out.println("--- Library Binary Search ---");
        int[] arr = { 10, 20, 15, 22, 35 };

        // 1. CRITICAL: Binary Search ONLY works on sorted arrays.
        Arrays.sort(arr);
        System.out.println("Sorted Array: " + Arrays.toString(arr));

        int key = 22;
        int res = Arrays.binarySearch(arr, key);

        // FIX: Added 'res' to the print statement
        if (res >= 0)
            System.out.println(key + " found at index: " + res);
        else
            System.out.println(key + " Not found");
    }

    /**
     * METHOD 2: The "Interview Way" (Manual Implementation)
     * Logic: Use two pointers (Low and High). Find Mid.
     * If Mid < Target, ignore left half.
     * If Mid > Target, ignore right half.
     */
    public int manualBinarySearch(int[] arr, int target) {
        // Pre-condition: Array MUST be sorted
        // We assume 'arr' is already sorted here.

        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            // "Senior Engineer" Tip:
            // Don't use (low + high) / 2. It can overflow for huge arrays.
            // Use this instead:
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                return mid; // Found it!
            }

            if (arr[mid] < target) {
                low = mid + 1; // Target is in the right half
            } else {
                high = mid - 1; // Target is in the left half
            }
        }
        return -1; // Not found
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        ArrayBinarySearch searcher = new ArrayBinarySearch();
        System.out.println("Structure: " + searcher.getDataStructureName());

        // 1. Library Demo
        searcher.searchUsingLibrary();

        // 2. Manual Implementation Demo
        System.out.println("\n--- Manual Binary Search ---");
        int[] sortedData = {10, 15, 20, 22, 35, 40, 50};
        int target = 35;

        int index = searcher.manualBinarySearch(sortedData, target);
        System.out.println("Found " + target + " at index: " + index);

        System.out.println("Found 99? Index: " + searcher.manualBinarySearch(sortedData, 99));
    }
}