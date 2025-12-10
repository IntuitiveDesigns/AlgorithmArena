package com.example.arena.datastructures;

import java.util.HashSet;
import java.util.Set;

// 1. Extend the Contract
public class SetSample extends BigODataStructures {

    // Standard Practice: Use HashSet (Fastest, Unordered)
    // If you need order, use 'LinkedHashSet' (Insertion Order) or 'TreeSet' (Sorted)
    private final Set<String> set = new HashSet<>();

    // 2. Constructor
    public SetSample() {
        super("HashSet (Unique Items)");
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "N/A"; } // Sets do not support Access by Index!

    @Override
    public String getAvgSearch() { return "O(1)"; } // Contains

    @Override
    public String getAvgInsertion() { return "O(1)"; } // Add

    @Override
    public String getAvgDeletion() { return "O(1)"; } // Remove

    @Override
    public String getWorstAccess() { return "N/A"; }

    @Override
    public String getWorstSearch() { return "O(n)"; } // Hash Collisions

    @Override
    public String getWorstInsertion() { return "O(n)"; }

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // --- LOGIC ---

    /**
     * INSERTION (add)
     * Returns TRUE if added, FALSE if it was a duplicate.
     */
    public void insertion(String value) {
        boolean wasAdded = set.add(value);
        if (wasAdded) {
            System.out.println("Inserted: " + value);
        } else {
            System.out.println("Duplicate ignored: " + value);
        }
    }

    // Helper to match interface
    public void insertion() {
        insertion("Apple");
        insertion("Banana");
        insertion("Apple"); // Duplicate!
    }

    /**
     * ACCESS (Iteration)
     * Sets do NOT have .get(index). You cannot ask for "Item 5".
     * You can only iterate over them.
     */
    public void access() {
        if (set.isEmpty()) {
            System.out.println("Set is empty.");
            return;
        }
        System.out.print("Set Contents (Random Order): ");
        for (String item : set) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    /**
     * SEARCH (contains)
     * This is the superpower of Sets. Instant lookup.
     */
    public boolean search(String value) {
        return set.contains(value);
    }

    public void search() {
        System.out.println("Contains Banana? " + search("Banana"));
    }

    /**
     * DELETION (remove)
     */
    public void deletion(String value) {
        boolean removed = set.remove(value);
        if (removed) {
            System.out.println("Deleted: " + value);
        } else {
            System.out.println("Value not found: " + value);
        }
    }

    public void deletion() {
        deletion("Banana");
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        SetSample sample = new SetSample();

        System.out.println(sample.bigOContract());
        System.out.println();

        System.out.println("Structure: " + sample.getDataStructureName());

        // 1. Insert (Notice the duplicate "Apple" is ignored)
        sample.insertion();

        // 2. Access (Iteration)
        sample.access();

        // 3. Search
        sample.search();

        // 4. Delete
        sample.deletion();
        sample.access();
    }
}