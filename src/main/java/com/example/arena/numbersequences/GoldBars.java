package com.example.arena.numbersequences;

import java.util.Random;

public class GoldBars {

    // Simulates merging two bars
    public static int processGoldBars(int barOne, int barTwo) {
        return barOne + barTwo;
    }

    public static void runTest(int[] intArray) {
        if (intArray == null || intArray.length == 0) {
            System.out.println("No gold bars to process.");
            return;
        }

        System.out.println("Processing " + intArray.length + " bars...");

        // ---------------- METHOD 1: SEQUENTIAL ACCUMULATION
        // Logic: Take the running total and merge it with the next bar.
        // ((A + B) + C) + D ...

        long startTime = System.nanoTime();

        // Use LONG to prevent integer overflow if bars are large
        long sum = intArray[0];
        int loops = 0;

        for (int i = 1; i < intArray.length; i++) {
            loops++;
            // We cast to int here because your process method takes int,
            // but in reality, you should process longs.
            sum = processGoldBars((int) sum, intArray[i]);
        }

        long duration = System.nanoTime() - startTime;
        System.out.println("[Sequential] Sum: " + sum + " | Loops: " + loops + " | Time (ns): " + duration);


        // ---------------- METHOD 2: PAIRWISE ACCUMULATION
        // Logic: Process 2 bars at a time, then add that result to the total.
        // (A + B) + (C + D) ...
        // This is safer to implement with a standard "step by 2" loop.

        startTime = System.nanoTime();
        sum = 0;
        loops = 0;

        // Loop through the array in steps of 2
        for (int i = 0; i < intArray.length; i += 2) {
            loops++;

            // Safety Check: Do we have a pair, or is this the last odd-one-out?
            if (i + 1 < intArray.length) {
                // We have a pair (i and i+1)
                int pairSum = processGoldBars(intArray[i], intArray[i + 1]);
                sum += pairSum;
            } else {
                // We have one leftover bar at the end
                sum += intArray[i];
            }
        }

        duration = System.nanoTime() - startTime;
        System.out.println("[Pairwise]   Sum: " + sum + " | Loops: " + loops + " | Time (ns): " + duration);
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        // Generate random bars locally so this file runs without dependencies
        int[] intArray = generateRandomIntArray(10000); // 10,000 bars
        runTest(intArray);
    }

    // Local Helper to replace 'Utility'
    private static int[] generateRandomIntArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(100); // Random bar weight 0-99
        }
        return arr;
    }
}