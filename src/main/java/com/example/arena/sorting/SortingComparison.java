package com.example.arena.sorting;

import com.example.arena.sorting.algorithms.*; // assumes Bubble, Quick, Tim, Tree, etc.

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SortingComparison {

    private static final int N = 50_000;

    public static void main(String[] args) {
        int[] base = createRandomArray(N, 0, 1_000_000);

        List<BigOSort> algorithms = List.of(
                new Bubble(),
                new Bucket(),
                new Counting(),
                new Cube(),
                new Heap(),
                new Insertion(),
                new Merge(),
                new Quick(),
                new Radix(),
                new Selection(),
                new Shell(),
                new Sort(),
                new Tim(),
                new Tree()
        );

        for (BigOSort algo : algorithms) {
            int[] data = Arrays.copyOf(base, base.length);

            System.out.println(algo.bigOContract());
            long start = System.nanoTime();
            algo.run(data);
            long end = System.nanoTime();

            long durationMillis = (end - start) / 1_000_000;

            // Optional: verify sorted
            boolean sorted = isSorted(data);

            System.out.println("Sorted correctly: " + sorted);
            System.out.println("Runtime:          " + durationMillis + " ms");
            System.out.println("Sample output:    " +
                    Arrays.toString(Arrays.copyOf(data, Math.min(10, data.length))));
            System.out.println("--------------------------------------------------");
        }
    }

    private static int[] createRandomArray(int n, int min, int max) {
        Random random = new Random(42); // deterministic
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(max - min + 1) + min;
        }
        return arr;
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
