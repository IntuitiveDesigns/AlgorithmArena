package com.example.arena.datastructures;

import java.util.PriorityQueue;

// 1. Extend the Abstract Class
public class HeapSample extends BigODataStructures {

    // Java's PriorityQueue is a Min-Heap by default (Smallest item at the top)
    private final PriorityQueue<Integer> heap = new PriorityQueue<>();

    // 2. Call Parent Constructor
    public HeapSample() {
        super("Min Heap (PriorityQueue)");
    }

    // --- BIG O CONTRACT IMPLEMENTATION ---

    @Override
    public String getAvgAccess() { return "O(1)"; } // Peek is instant

    @Override
    public String getAvgSearch() { return "O(n)"; } // Must scan array to find item

    @Override
    public String getAvgInsertion() { return "O(log n)"; } // Bubble up

    @Override
    public String getAvgDeletion() { return "O(log n)"; } // Poll (remove root) & Bubble down

    @Override
    public String getWorstAccess() { return "O(1)"; }

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(log n)"; }

    @Override
    public String getWorstDeletion() { return "O(log n)"; } // Removing specific item (not root) is O(n), but standard poll is log n

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // --- ACTUAL LOGIC ---

    /**
     * ACCESS (Peek)
     * View the top element without removing it.
     */
    public void access() {
        if (!heap.isEmpty()) {
            Integer min = heap.peek();
            System.out.println("Min Value (Peek): " + min);
        } else {
            System.out.println("Heap is empty.");
        }
    }

    /**
     * SEARCH (Contains)
     * Heaps are not good for searching specific values other than the root.
     */
    public boolean search(int value) {
        // contains() iterates through the underlying array
        return heap.contains(value);
    }

    /**
     * INSERTION (Offer)
     * Adds to the bottom and "bubbles up".
     */
    public void insertion(int value) {
        heap.offer(value); // 'offer' is safer than 'add'
        System.out.println("Inserted: " + value);
    }

    /**
     * DELETION (Poll)
     * Removes the root, moves the last item to the top, and "bubbles down".
     */
    public void deletion() {
        if (!heap.isEmpty()) {
            Integer removed = heap.poll();
            System.out.println("Removed Min: " + removed);
        } else {
            System.out.println("Cannot delete, heap is empty.");
        }
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        HeapSample sample = new HeapSample();

        System.out.println(sample.bigOContract());
        System.out.println();

        System.out.println("Structure: " + sample.getDataStructureName());

        sample.insertion(10);
        sample.insertion(5);
        sample.insertion(20);

        sample.access(); // Should print 5 (Min heap)

        System.out.println("Found 20? " + sample.search(20));

        sample.deletion(); // Removes 5
        sample.access();   // Should now print 10
    }
}