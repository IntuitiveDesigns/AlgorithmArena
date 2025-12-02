package com.example.arena.datastructures;

import java.util.Hashtable;

public class HashTableSample extends BigODataStructures {

    // The Legacy Class: Hashtable
    // NOTE: It is synchronized (Thread-Safe), which makes it slower than HashMap.
    private final Hashtable<String, Integer> table;

    public HashTableSample() {
        super("Hash Table (Legacy java.util.Hashtable)");
        this.table = new Hashtable<>();
    }

    // --- BIG O CONTRACT ---
    // (Identical to HashMap, just slower due to synchronization overhead)

    @Override
    public String getAvgAccess() { return "O(1)"; }

    @Override
    public String getAvgSearch() { return "O(1)"; }

    @Override
    public String getAvgInsertion() { return "O(1)"; }

    @Override
    public String getAvgDeletion() { return "O(1)"; }

    @Override
    public String getWorstAccess() { return "O(n)"; }

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(n)"; }

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // --- LOGIC ---

    /**
     * ACCESS (get)
     * Retrieves value by key.
     * Note: This method is 'synchronized' internally!
     */
    public void access(String key) {
        if (table.containsKey(key)) {
            System.out.println("Value for " + key + ": " + table.get(key));
        } else {
            System.out.println("Key " + key + " not found.");
        }
    }

    public void access() {
        access("Alice");
    }

    /**
     * SEARCH (containsKey)
     * Checks if key exists.
     */
    public boolean search(String key) {
        return table.containsKey(key);
    }

    public void search() {
        System.out.println("Contains Bob? " + search("Bob"));
    }

    /**
     * INSERTION (put)
     * WARNING: If you try table.put(null, 1), it will throw NullPointerException!
     */
    public void insertion(String key, int value) {
        try {
            table.put(key, value);
            System.out.println("Inserted: " + key);
        } catch (NullPointerException e) {
            System.err.println("Error: Hashtable does not allow null keys/values!");
        }
    }

    public void insertion() {
        insertion("Alice", 30);
        insertion("Bob", 40);

        // Demonstrate the "No Nulls" rule (Interview Trap)
        System.out.println("Attempting to insert null key...");
        insertion(null, 50); // This will catch the exception
    }

    /**
     * DELETION (remove)
     */
    public void deletion(String key) {
        table.remove(key);
        System.out.println("Deleted: " + key);
    }

    public void deletion() {
        deletion("Bob");
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        HashTableSample legacy = new HashTableSample();

        legacy.insertion();
        legacy.access();
        legacy.search();
        legacy.deletion();
    }
}