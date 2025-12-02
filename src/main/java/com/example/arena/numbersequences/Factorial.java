package com.example.arena.numbersequences;

public class Factorial {

    /**
     * Find factorial of a number using recursion.
     * Note: This works for N up to 20. For N > 20, you need BigInteger.
     */
    // Iterative Version (Safer for large numbers)
    public long getFactorialIterative(int num) {
        if (num < 0) throw new IllegalArgumentException("Negative numbers not allowed");

        long result = 1;

        // Loop from 2 up to num
        for (int i = 2; i <= num; i++) {
            result *= i;
        }

        return result;
    }
}