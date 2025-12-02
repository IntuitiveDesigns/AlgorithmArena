package com.example.arena.datastructures;

import java.util.HashMap;
import java.util.Map;

public class HashingSample extends BigODataStructures {

    // The Standard: HashMap (Not synchronized, allows null keys)
    // For Multi-threading, you would use ConcurrentHashMap
    private final Map<String, Integer> map;

    public HashingSample() {
        super("Hash Table (HashMap)");
        this.map = new HashMap<>();
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(1)"; } // Direct index lookup

    @Override
    public String getAvgSearch() { return "O(1)"; } // Finding key is instant

    @Override
    public String getAvgInsertion() { return "O(1)"; } // Calculating hash is fast

    @Override
    public String getAvgDeletion() { return "O(1)"; }

    @Override
    public String getWorstAccess() { return "O(n)"; } // Collision Storm (All keys hash to same bucket)

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(n)"; } // Resizing the map (rehashing) takes time

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // --- LOGIC ---

    /**
     * INSERTION (put)
     * Logic:
     * 1. Calculate hash(key).
     * 2. Map to index.
     * 3. If bucket empty, place node.
     * 4. If bucket occupied (Collision), append to LinkedList/Tree in that bucket.
     */
    public void insertion(String key, int value) {
        map.put(key, value);
        System.out.println("Inserted: " + key + " -> " + value);
    }

    // Overload to match interface
    public void insertion() {
        insertion("Alice", 30);
        insertion("Bob", 25);
        insertion("Charlie", 35);
        // "Collision" simulation (Internally, these might hit same bucket,
        // but HashMap handles it automatically)
        insertion("Dave", 40);
    }

    /**
     * ACCESS (get)
     * We provide the KEY to get the VALUE.
     * Note: If you don't have the key, you can't "access" index 5 directly like an array.
     */
    public void access(String key) {
        Integer val = map.get(key);
        System.out.println("Value for " + key + ": " + val);
    }

    public void access() {
        access("Alice");
    }

    /**
     * SEARCH (containsKey)
     * Checks if a key exists.
     * Warning: 'containsValue()' is O(n) because it has to check every bucket!
     */
    public boolean search(String key) {
        return map.containsKey(key);
    }

    public void search() {
        boolean found = search("Bob");
        System.out.println("Found Bob? " + found);
    }

    /**
     * DELETION (remove)
     * Calculates hash, finds the bucket, unlinks the node.
     */
    public void deletion(String key) {
        map.remove(key);
        System.out.println("Deleted key: " + key);
    }

    public void deletion() {
        deletion("Alice");
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        HashingSample hash = new HashingSample();

        // 1. Insert
        hash.insertion();

        // 2. Access
        hash.access(); // Value for Alice: 30

        // 3. Search
        hash.search(); // Found Bob? true

        // 4. Delete
        hash.deletion();
        System.out.println("Found Alice after delete? " + hash.search("Alice"));
    }
}