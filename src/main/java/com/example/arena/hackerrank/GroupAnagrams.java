package com.example.arena.hackerrank;

import java.util.*;

public class GroupAnagrams {

    public static List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) return new ArrayList<>();

        // Map Key: The sorted string (Canonical Form) "aet"
        // Map Value: The list of original words ["eat", "tea", "ate"]
        Map<String, List<String>> map = new HashMap<>();

        for (String s : strs) {
            // 1. Sort the string to find its "Key"
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);

            // 2. Add to the correct bucket (The Speed Coding Way)
            // "If key doesn't exist, make new ArrayList. Then add 's'."
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }

        // 3. Return just the values (the groups)
        return new ArrayList<>(map.values());
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println("Input: " + Arrays.toString(input));

        List<List<String>> result = groupAnagrams(input);

        System.out.println("Output: " + result);
    }
}