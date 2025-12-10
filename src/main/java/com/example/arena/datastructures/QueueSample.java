package com.example.arena.datastructures;

import java.util.LinkedList;
import java.util.Queue;

// 1. Extend the Contract
public class QueueSample extends BigODataStructures {

    // Standard Practice: Use LinkedList as a Queue
    // (ArrayDeque is technically faster, but LinkedList is the standard interview answer)
    private final Queue<Integer> queue = new LinkedList<>();

    // 2. Constructor
    public QueueSample() {
        super("Queue (FIFO - LinkedList)");
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(1)"; } // Peek is instant

    @Override
    public String getAvgSearch() { return "O(n)"; } // Must scan the line

    @Override
    public String getAvgInsertion() { return "O(1)"; } // Enqueue at tail is instant

    @Override
    public String getAvgDeletion() { return "O(1)"; } // Dequeue from head is instant

    @Override
    public String getWorstAccess() { return "O(1)"; }

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(1)"; }

    @Override
    public String getWorstDeletion() { return "O(1)"; }

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // --- LOGIC ---

    /**
     * INSERTION (Enqueue / Offer)
     * Adds to the Back (Tail)
     */
    public void insertion(int value) {
        queue.offer(value); // 'offer' is safer than 'add'
        System.out.println("Enqueued: " + value);
    }

    /**
     * DELETION (Dequeue / Poll)
     * Removes from the Front (Head)
     */
    public void deletion() {
        if (!queue.isEmpty()) {
            Integer value = queue.poll(); // 'poll' removes from front
            System.out.println("Dequeued: " + value);
        } else {
            System.out.println("Queue is empty.");
        }
    }

    /**
     * ACCESS (Peek)
     * View the Front without removing.
     */
    public void access() {
        if (!queue.isEmpty()) {
            System.out.println("Front is: " + queue.peek());
        } else {
            System.out.println("Queue is empty.");
        }
    }

    /**
     * SEARCH (Contains)
     */
    public boolean search(int value) {
        return queue.contains(value);
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        QueueSample q = new QueueSample();

        System.out.println(q.bigOContract());
        System.out.println();

        System.out.println("Structure: " + q.getDataStructureName());

        // 1. Enqueue (First In)
        q.insertion(10);
        q.insertion(20);
        q.insertion(30);

        // 2. Access (Peek)
        q.access(); // Should be 10

        // 3. Dequeue (First Out)
        q.deletion(); // Removes 10
        q.access();   // Should now be 20

        // 4. Search
        System.out.println("Contains 30? " + q.search(30));
    }
}