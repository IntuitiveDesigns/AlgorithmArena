package com.example.arena.algorithms;

import java.util.PriorityQueue;

public class TopKElements {

    /**
     * Scenario: "Find the K-th largest element in a stream."
     * Logic: Use a Min-Heap of size K.
     * If a new number is bigger than the heap's smallest (root), swap them.
     */
    public int findKthLargest(int[] nums, int k) {
        // Min-Heap (Smallest element at the top)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        for (int n : nums) {
            minHeap.add(n);

            // If we have more than k elements, remove the smallest one.
            // This leaves us with the 'k' largest elements in the heap.
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // The root is now the Kth largest element (the smallest of the top k)
        return minHeap.peek();
    }

    public static void main(String[] args) {
        TopKElements solver = new TopKElements();
        int[] data = {3, 2, 1, 5, 6, 4};
        int k = 2;

        // Sorted: 1, 2, 3, 4, 5, 6
        // 2nd Largest is 5.
        System.out.println("2nd Largest Element: " + solver.findKthLargest(data, k));
    }
}