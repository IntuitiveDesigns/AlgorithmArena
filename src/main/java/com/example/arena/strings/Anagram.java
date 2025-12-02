package com.example.arena.strings;

import java.util.Arrays;

public class Anagram {

    /**
     * Approach 1: Sorting (Easier to read)
     * Time Complexity: O(N log N) because of sorting
     */
    public boolean isAnagramSort(String s, String t) {
        if (s.length() != t.length()) return false;

        char[] sChars = s.toCharArray();
        char[] tChars = t.toCharArray();

        Arrays.sort(sChars);
        Arrays.sort(tChars);

        return Arrays.equals(sChars, tChars);
    }

    /**
     * Approach 2: Frequency Array (The "Oracle/HackerRank" Way)
     * Time Complexity: O(N) - Much faster!
     * Logic: We count letters. +1 for string S, -1 for string T.
     * If the buckets are all zero at the end, it's a match.
     */
    public boolean isAnagramOptimized(String s, String t) {
        if (s.length() != t.length()) return false;

        // Assuming strictly English lowercase letters a-z
        int[] counts = new int[26];

        for (int i = 0; i < s.length(); i++) {
            counts[s.charAt(i) - 'a']++; // Increment for first string
            counts[t.charAt(i) - 'a']--; // Decrement for second string
        }

        // If any bucket is not 0, it's not an anagram
        for (int count : counts) {
            if (count != 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Anagram solver = new Anagram();
        System.out.println("Is Anagram (Sort): " + solver.isAnagramSort("listen", "silent"));
        System.out.println("Is Anagram (Opt):  " + solver.isAnagramOptimized("triangle", "integral"));
    }
}