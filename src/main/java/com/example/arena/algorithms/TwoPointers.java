package com.example.arena.algorithms;

import java.util.Arrays;

public class TwoPointers {

    /**
     * PATTERN 1: Left and Right Pointers
     * Scenario: "Check if this string is a palindrome."
     */
    public boolean isPalindrome(String s) {
        // 1. Clean the string (Interview standard: ignore non-alphanumeric)
        String clean = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        int left = 0;
        int right = clean.length() - 1;

        while (left < right) {
            if (clean.charAt(left) != clean.charAt(right)) {
                return false; // Mismatch found
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * PATTERN 2: Sorted Two Sum
     * Scenario: "Find two numbers in a sorted array that add up to Target."
     * Naive: O(n^2). This approach: O(n).
     */
    public int[] twoSumSorted(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int currentSum = arr[left] + arr[right];

            if (currentSum == target) {
                return new int[]{left, right}; // Found indices
            } else if (currentSum < target) {
                left++; // We need a bigger sum, move left up
            } else {
                right--; // We need a smaller sum, move right down
            }
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        TwoPointers solver = new TwoPointers();

        System.out.println("Palindrome Check: " + solver.isPalindrome("A man, a plan, a canal: Panama"));

        int[] sortedData = {1, 3, 4, 6, 8, 11};
        System.out.println("Two Sum (Target 10): " + Arrays.toString(solver.twoSumSorted(sortedData, 10)));
    }
}