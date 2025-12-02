package com.example.arena.datastructures;

import java.util.HashMap;
import java.util.Map;

// 1. Extend the Contract
public class MapSample extends BigODataStructures {

    private final Map<String, Integer> map;

    public MapSample() {
        super("HashMap (Map Interface)");
        this.map = new HashMap<>();
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(1)"; }

    @Override
    public String getAvgSearch() { return "O(1)"; }

    @Override
    public String getAvgInsertion() { return "O(1)"; }

    @Override
    public String getAvgDeletion() { return "O(1)"; }

    @Override
    public String getWorstAccess() { return "O(n)"; } // Collisions

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(n)"; } // Resizing

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // --- LOGIC ---

    /**
     * INSERTION (put)
     * Adds key-value pair. If key exists, it overwrites the value.
     */
    public void insertion(String key, int value) {
        map.put(key, value);
        System.out.println("Inserted: " + key + " -> " + value);
    }

    // Helper to match interface signature
    public void insertion() {
        insertion("Steven", 45);
        insertion("Alice", 30);
    }

    /**
     * ACCESS (get)
     * Retrieves value. Returns null if not found.
     */
    public void access(String key) {
        if (map.containsKey(key)) {
            System.out.println("Value for " + key + ": " + map.get(key));
        } else {
            System.out.println("Key " + key + " not found.");
        }
    }

    public void access() {
        access("Steven");
    }

    /**
     * SPECIAL: GetOrDefault
     * This is CRITICAL for "Frequency Count" interview questions.
     */
    public void demonstrateGetOrDefault() {
        // Returns 0 if "Bob" doesn't exist.
        int bobAge = map.getOrDefault("Bob", 0);
        System.out.println("Bob's Age (Default): " + bobAge);
    }

    /**
     * SEARCH (containsKey)
     */
    public boolean search(String key) {
        return map.containsKey(key);
    }

    public void search() {
        System.out.println("Contains Alice? " + search("Alice"));
    }

    /**
     * DELETION (remove)
     */
    public void deletion(String key) {
        map.remove(key);
        System.out.println("Deleted: " + key);
    }

    public void deletion() {
        deletion("Steven");
    }

    /**
     * ITERATION
     * You must know how to loop through a map!
     */
    public void iterate() {
        System.out.println("--- Iterating Map ---");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        MapSample sample = new MapSample();

        System.out.println("Structure: " + sample.getDataStructureName());

        // 1. Insert
        sample.insertion();

        // 2. Special Trick (GetOrDefault)
        sample.demonstrateGetOrDefault();

        // 3. Search
        sample.search();

        // 4. Iterate
        sample.iterate();

        // 5. Delete
        sample.deletion();
        sample.iterate();
    }
}