package com.example.arena.sorting;

import com.example.arena.sorting.algorithms.Quick;

import java.util.Arrays;

/**
 * TestSort
 * This class is a "Client" that verifies the Decorator Pattern works.
 * It does not extend BigOSort; it uses it.
 */
public class TestSort {

    public static void main(String[] args) {
        System.out.println("--- Testing the Decorator Pattern ---");

        // 1. Create a Base Algorithm (e.g., QuickSort)
        BigOSort realAlgorithm = new Quick();

        // 2. Wrap it in the Decorator (This adds the timing functionality)
        BigOSort decoratedAlgorithm = new BigOTestDecorator(realAlgorithm);

        // 3. Create Inputs
        int[] input = {50, 10, 20, 40, 30};
        System.out.println("Input: " + Arrays.toString(input));

        // 4. Run the DECORATED version
        // This calls BigOTestDecorator.run() -> starts timer -> calls Quick.run() -> stops timer
        decoratedAlgorithm.run(input);

        // 5. Verify Output
        System.out.println("Output: " + Arrays.toString(input));

        // 6. Verify Contract Delegation
        // The decorator should pass these calls through to QuickSort
        System.out.println("\n--- Verifying Contract Pass-Through ---");
        System.out.println("Name: " + decoratedAlgorithm.getSortName()); // Should say "Quick Sort (Timed)"
        System.out.println("Time: " + decoratedAlgorithm.getBestTime()); // Should say "O(n log n)"
    }
}