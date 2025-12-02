package com.example.arena.numbersequences;

import java.util.HashMap;
import java.util.Map;

public class Fibonacci {

    // Cache for Memoization (Top-Down Dynamic Programming)
    // We use Long because Fibonacci numbers exceed 'int' size very quickly (at n=46).
    private static final Map<Integer, Long> memoCache = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("--- Fibonacci Series ---");

        // Hardcoded test for the library consistency
        int n = 10;
        System.out.println("Printing first " + n + " numbers (Iterative):");
        for (int i = 1; i <= n; i++) {
            System.out.print(getFibonacciIterative(i) + " ");
        }
        System.out.println();

        System.out.println("Calculating Fib(50) using Memoization (Instant):");
        System.out.println(getFibonacciMemoized(50));

        System.out.println("Calculating Fib(10) using Recursive (Slow for large N):");
        System.out.println(getFibonacciRecursive(10));
    }

    /**
     * Approach 1: Naive Recursion
     * Time Complexity: O(2^n) - EXPONENTIAL (Terrible)
     * Space Complexity: O(n) - Stack depth
     * WARNING: Do not use this for n > 40. It will hang.
     */
    public static long getFibonacciRecursive(int n) {
        if (n <= 2) {
            return 1;
        }
        return getFibonacciRecursive(n - 1) + getFibonacciRecursive(n - 2);
    }

    /**
     * Approach 2: Iterative (Loop)
     * Time Complexity: O(n) - LINEAR (Great)
     * Space Complexity: O(1) - Constant (Great)
     * This is usually the best answer for space efficiency.
     */
    public static long getFibonacciIterative(int n) {
        if (n <= 2) {
            return 1;
        }

        long fibo1 = 1;
        long fibo2 = 1;
        long fibonacci = 1;

        for (int i = 3; i <= n; i++) {
            fibonacci = fibo1 + fibo2; // Sum previous two
            fibo1 = fibo2;             // Shift window
            fibo2 = fibonacci;
        }
        return fibonacci;
    }

    /**
     * Approach 3: Memoization (Recursion + Caching)
     * Time Complexity: O(n) - Linear (We only calculate each number once)
     * Space Complexity: O(n) - Map storage + Stack depth
     * This demonstrates "Dynamic Programming".
     */
    public static long getFibonacciMemoized(int n) {
        if (n <= 2) {
            return 1;
        }

        // 1. Check Cache
        if (memoCache.containsKey(n)) {
            return memoCache.get(n);
        }

        // 2. Compute (Recursive)
        long result = getFibonacciMemoized(n - 1) + getFibonacciMemoized(n - 2);

        // 3. Store in Cache
        memoCache.put(n, result);

        return result;
    }
}