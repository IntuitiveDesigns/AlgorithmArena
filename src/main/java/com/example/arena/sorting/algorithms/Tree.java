package com.example.arena.sorting.algorithms;

import com.example.arena.sorting.BigOSort;
import java.util.Arrays;

public class Tree extends BigOSort {

    // 1. Constructor
    public Tree() {
        super("Tree Sort (Binary Search Tree)");
    }

    // Inner Node class for the BST
    private static class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
            left = right = null;
        }
    }

    // 2. Logic
    @Override
    public void run(int[] arr) {
        if (arr.length == 0) return;

        // Step 1: Build the BST
        Node root = null;
        for (int value : arr) {
            root = insert(root, value);
        }

        // Step 2: In-Order Traversal to populate the array
        // We use a single-element array to track the index by reference
        int[] indexTracker = {0};
        inOrderRec(root, arr, indexTracker);
    }

    // Helper: Recursive Insert
    private Node insert(Node root, int value) {
        if (root == null) {
            root = new Node(value);
            return root;
        }
        if (value < root.value)
            root.left = insert(root.left, value);
        else if (value >= root.value) // Handle duplicates by putting them on right
            root.right = insert(root.right, value);

        return root;
    }

    // Helper: In-Order Traversal (Sorted)
    private void inOrderRec(Node root, int[] arr, int[] indexTracker) {
        if (root != null) {
            inOrderRec(root.left, arr, indexTracker);
            arr[indexTracker[0]++] = root.value; // Write back to array
            inOrderRec(root.right, arr, indexTracker);
        }
    }

    // --- BIG O CONTRACT (Updated to match Parent) ---

    @Override
    public String getBestTime() {
        return "O(n log n)"; // Balanced Tree
    }

    @Override
    public String getAverageTime() {
        return "O(n log n)";
    }

    @Override
    public String getWorstTime() {
        return "O(n^2)"; // Skewed Tree (e.g., input is already sorted)
    }

    @Override
    public String getSpaceComplexity() {
        return "O(n)"; // Storing nodes
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        Tree sorter = new Tree();
        int[] input = {5, 1, 4, 2, 8, 5}; // Note the duplicate '5'

        System.out.println(sorter.bigOContract());
        System.out.println();

        sorter.run(input);
        System.out.println("Sorted: " + Arrays.toString(input));
    }
}