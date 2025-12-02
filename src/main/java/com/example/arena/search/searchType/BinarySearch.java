package com.example.arena.search.searchType;

import java.util.Arrays;

/**
 * A Generic Binary Search Implementation.
 * Works for any class T that implements Comparable<T> (Integer, String, etc.)
 */
public class BinarySearch {

    /**
     * Generic Search Method
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public <T extends Comparable<T>> int search(T[] array, T target) {
        // Pre-condition: Array must be sorted.
        // We assume the caller handles sorting, or we could check it (expensive).

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            // Compare items using .compareTo() instead of == or <
            // result < 0 means array[mid] is smaller than target
            // result > 0 means array[mid] is larger than target
            // result == 0 means equal
            int result = array[mid].compareTo(target);

            if (result == 0) {
                return mid; // Found
            } else if (result < 0) {
                low = mid + 1; // Look Right
            } else {
                high = mid - 1; // Look Left
            }
        }
        return -1; // Not Found
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        BinarySearch searcher = new BinarySearch();

        // Test 1: Integers (Wrapper Class)
        Integer[] nums = {10, 20, 30, 40, 50};
        System.out.println("Search 40 in Integers: Index " + searcher.search(nums, 40));

        // Test 2: Strings
        String[] names = {"Alice", "Bob", "Charlie", "Dave", "Eve"};
        // Note: Arrays must be sorted!
        Arrays.sort(names);
        System.out.println("Search 'Charlie' in Strings: Index " + searcher.search(names, "Charlie"));

        // Test 3: Not Found
        System.out.println("Search 'Zack' in Strings: Index " + searcher.search(names, "Zack"));
    }
}