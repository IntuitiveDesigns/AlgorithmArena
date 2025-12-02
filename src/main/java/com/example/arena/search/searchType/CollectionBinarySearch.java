package com.example.arena.search.searchType;

import com.example.arena.datastructures.BigODataStructures;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 1. Extend the Contract
public class CollectionBinarySearch extends BigODataStructures {

    public CollectionBinarySearch() {
        super("Binary Search (Collections)");
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(1) for ArrayList"; }

    @Override
    public String getAvgSearch() { return "O(log n) if RandomAccess"; } // O(n) for LinkedList!

    @Override
    public String getAvgInsertion() { return "O(n)"; }

    @Override
    public String getAvgDeletion() { return "O(n)"; }

    @Override
    public String getWorstAccess() { return "O(1)"; }

    @Override
    public String getWorstSearch() { return "O(n) if unsorted"; }

    @Override
    public String getWorstInsertion() { return "O(n)"; }

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(1)"; }

    // --- LOGIC ---

    public void collectionSearch() {
        // Use ArrayList because it supports fast random access (Indices)
        List<Integer> al = new ArrayList<>();
        al.add(1);
        al.add(2);
        al.add(3);
        al.add(10);
        al.add(20);

        // CRITICAL: The list MUST be sorted before searching
        Collections.sort(al);
        System.out.println("Sorted List: " + al);

        // Search for 10
        int key = 10;
        int res = Collections.binarySearch(al, key);

        if (res >= 0)
            System.out.println(key + " found at index = " + res);
        else
            System.out.println(key + " Not found");

        // Search for 15
        key = 15;
        res = Collections.binarySearch(al, key);

        if (res >= 0)
            System.out.println(key + " found at index = " + res);
        else
            System.out.println(key + " Not found. Insertion point would be: " + (-res - 1));
        // Note: When not found, it returns (-(insertion point) - 1)
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        CollectionBinarySearch searcher = new CollectionBinarySearch();

        System.out.println("Structure: " + searcher.getDataStructureName());
        searcher.collectionSearch();
    }
}