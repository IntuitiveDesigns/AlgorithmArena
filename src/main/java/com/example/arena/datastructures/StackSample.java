package com.example.arena.datastructures;

import java.util.ArrayDeque;
import java.util.Deque;

// 1. Extend the Contract
public class StackSample extends BigODataStructures {

    // Best Practice: Use Deque (Double Ended Queue) as a Stack
    // ArrayDeque is faster than Stack and LinkedList for this purpose.
    private final Deque<Integer> stack = new ArrayDeque<>();

    // 2. Constructor
    public StackSample() {
        super("Stack (LIFO - ArrayDeque)");
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "N/A"; } // Stacks do not allow random access (get index 5)

    @Override
    public String getAvgSearch() { return "O(n)"; } // Must dig through the stack

    @Override
    public String getAvgInsertion() { return "O(1)"; } // Push is instant

    @Override
    public String getAvgDeletion() { return "O(1)"; } // Pop is instant

    @Override
    public String getWorstAccess() { return "N/A"; }

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
     * INSERTION (push)
     * Adds to the Top.
     */
    public void insertion(int value) {
        stack.push(value); // Adds to the "top"
        System.out.println("Pushed: " + value);
    }

    // Helper to match interface
    public void insertion() {
        insertion(10);
        insertion(20);
        insertion(30);
    }

    /**
     * DELETION (pop)
     * Removes from the Top.
     */
    public void deletion() {
        if (!stack.isEmpty()) {
            Integer value = stack.pop(); // Removes from the "top"
            System.out.println("Popped: " + value);
        } else {
            System.out.println("Stack is empty.");
        }
    }

    /**
     * ACCESS (peek)
     * Look at the top without removing.
     */
    public void access() {
        if (!stack.isEmpty()) {
            System.out.println("Top is: " + stack.peek());
        } else {
            System.out.println("Stack is empty.");
        }
    }

    /**
     * SEARCH (contains)
     */
    public boolean search(int value) {
        return stack.contains(value);
    }

    public void search() {
        System.out.println("Contains 20? " + search(20));
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        StackSample s = new StackSample();

        System.out.println("Structure: " + s.getDataStructureName());

        // 1. Push (10 -> 20 -> 30)
        s.insertion();

        // 2. Peek (Should be 30)
        s.access();

        // 3. Pop (Removes 30)
        s.deletion();

        // 4. Peek (Should be 20)
        s.access();

        // 5. Search
        s.search();
    }
}