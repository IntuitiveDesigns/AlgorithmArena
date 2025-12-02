package com.example.arena.hackerrank;

import java.util.*;

public class MergeIntervals {

    public static int[][] merge(int[][] intervals) {
        if (intervals.length <= 1) return intervals;

        // 1. SORT by start time (Critical Step)
        // Lambda shortcut: (a, b) -> Integer.compare(a[0], b[0])
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        // 2. Use a LinkedList because it's easy to peek/add to the end
        LinkedList<int[]> merged = new LinkedList<>();

        for (int[] interval : intervals) {
            // Case A: List is empty OR No Overlap (Start time > Last End time)
            if (merged.isEmpty() || merged.getLast()[1] < interval[0]) {
                merged.add(interval);
            }
            // Case B: Overlap! Merge them.
            else {
                // Update the end time of the LAST interval to be the max of both
                merged.getLast()[1] = Math.max(merged.getLast()[1], interval[1]);
            }
        }

        // Convert back to primitive array
        return merged.toArray(new int[merged.size()][]);
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        int[][] input = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};

        System.out.println("Input Intervals:");
        System.out.println(Arrays.deepToString(input));

        int[][] result = merge(input);

        System.out.println("Merged Intervals:");
        System.out.println(Arrays.deepToString(result));
    }
}