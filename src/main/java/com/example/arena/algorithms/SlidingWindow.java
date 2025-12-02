package com.example.arena.algorithms;

public class SlidingWindow {

    /**
     * Scenario: "Find the maximum sum of any contiguous subarray of size k."
     * Naive: O(n*k). This approach: O(n).
     */
    public int maxSumSubarray(int[] arr, int k) {
        if (arr.length < k) return -1;

        int maxSum = 0;
        int windowSum = 0;
        int windowStart = 0;

        for (int windowEnd = 0; windowEnd < arr.length; windowEnd++) {
            // 1. Add the next element
            windowSum += arr[windowEnd];

            // 2. Once we hit the window size 'k', start sliding
            if (windowEnd >= k - 1) {
                maxSum = Math.max(maxSum, windowSum);

                // Subtract the element that is sliding out of view
                windowSum -= arr[windowStart];
                // Move the start forward
                windowStart++;
            }
        }
        return maxSum;
    }

    public static void main(String[] args) {
        SlidingWindow solver = new SlidingWindow();
        int[] data = {2, 1, 5, 1, 3, 2};
        int k = 3;

        // Window [2, 1, 5] = 8
        // Window [1, 5, 1] = 7
        // Window [5, 1, 3] = 9 (Max)
        // Window [1, 3, 2] = 6
        System.out.println("Max Sum (Window 3): " + solver.maxSumSubarray(data, k));
    }
}