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

    // --- BIG O CONTRACT VALUES ---
    // (Same asymptotics as HashMap, just slower in practice due to synchronization)

    @Override
    public String getAvgAccess()     { return "O(1)"; }
    @Override
    public String getAvgSearch()     { return "O(1)"; }
    @Override
    public String getAvgInsertion()  { return "O(1)"; }
    @Override
    public String getAvgDeletion()   { return "O(1)"; }

    @Override
    public String getWorstAccess()   { return "O(n)"; }
    @Override
    public String getWorstSearch()   { return "O(n)"; }
    @Override
    public String getWorstInsertion(){ return "O(n)"; }
    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // Override the default, array-style contract with a map-style summary
    @Override
    public String bigOContract() {
        return new StringBuilder()
                .append("Data structure:          ").append(getDataStructureName()).append(System.lineSeparator())
                .append("Core operations:         get(key), put(key, value), remove(key), containsKey(key)").append(System.lineSeparator())
                .append("Average time per op:     ").append(getAvgAccess()).append(System.lineSeparator())
                .append("Worst-case time per op:  ").append(getWorstAccess()).append(System.lineSeparator())
                .append("Space complexity:        ").append(getSpaceComplexity())
                .toString();
    }

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
            System.out.println("[Warning] Hashtable does not allow null keys or values. Skipped insertion.");
        }
    }

    public void insertion() {
        System.out.println("--- INSERTION DEMO ---");

        insertion("Alice", 30);
        insertion("Bob", 40);

        System.out.println("Attempting to insert null key...");
        insertion(null, 50);

        System.out.println();
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

        System.out.println(legacy.bigOContract());
        System.out.println();

        legacy.insertion();
        legacy.access();
        legacy.search();
        legacy.deletion();
    }
}
