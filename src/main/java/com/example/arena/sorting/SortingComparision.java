package com.example.arena.sorting;

import com.example.arena.sorting.algorithms.*;

import java.util.*;

public class SortingComparision {

    // Registry of all available algorithms
    private static final List<BigOSort> ALGORITHMS = Arrays.asList(
            new Bubble(),
            new Selection(),
            new Insertion(),
            new Shell(),
            new Merge(),
            new Quick(),
            new Heap(),
            new Counting(),
            new Radix(),
            new Bucket(),
            new Tim(),
            new Tree(),
            new Cube(),
            new Sort() // Java Library Sort
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = getIntInput(scanner);

            if (choice == 0) {
                System.out.println("Exiting Sorting Arena. Goodbye!");
                break;
            }

            // Ask for array size
            System.out.print("Enter Array Size (e.g. 1000, 10000): ");
            int size = getIntInput(scanner);
            if (size <= 0) size = 1000; // Default

            if (choice == ALGORITHMS.size() + 1) {
                runAllTests(size);
            } else if (choice > 0 && choice <= ALGORITHMS.size()) {
                runSingleTest(ALGORITHMS.get(choice - 1), size);
            } else {
                System.out.println("Invalid choice.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine(); // consume newline
            scanner.nextLine(); // wait for enter
        }
    }

    private static void printMenu() {
        System.out.println("\n==========================================");
        System.out.println("          SORTING ALGORITHM ARENA         ");
        System.out.println("==========================================");
        for (int i = 0; i < ALGORITHMS.size(); i++) {
            System.out.printf("%2d. %s\n", (i + 1), ALGORITHMS.get(i).getSortName());
        }
        System.out.printf("%2d. RUN ALL ALGORITHMS (Benchmark)\n", ALGORITHMS.size() + 1);
        System.out.println(" 0. Exit");
        System.out.println("==========================================");
        System.out.print("Select an option: ");
    }

    private static void runSingleTest(BigOSort algorithm, int size) {
        System.out.println("\n--- Preparing " + algorithm.getSortName() + " ---");

        // 1. Generate Data
        int[] data = generateRandomArray(size);

        // 2. Run Test
        long duration = executeSort(algorithm, data);

        // 3. Verify & Report
        boolean isSorted = isSorted(data);
        System.out.printf("Result: %s | Time: %d ms | Sorted: %s\n",
                algorithm.getSortName(), duration, (isSorted ? "YES" : "NO"));

        // 4. Print Complexity Info
        System.out.println("   -> Best:    " + algorithm.getBestTime());
        System.out.println("   -> Average: " + algorithm.getAverageTime());
        System.out.println("   -> Worst:   " + algorithm.getWorstTime());
        System.out.println("   -> Space:   " + algorithm.getSpaceComplexity());
    }

    private static void runAllTests(int size) {
        System.out.println("\n--- RUNNING BENCHMARK (Size: " + size + ") ---");
        System.out.printf("%-20s | %-10s | %-10s\n", "Algorithm", "Time (ms)", "Status");
        System.out.println("------------------------------------------------");

        // We use a fresh array copy for every algorithm so it's fair
        int[] originalData = generateRandomArray(size);

        for (BigOSort algo : ALGORITHMS) {
            // Copy original data so every sort gets the exact same unsorted input
            int[] testData = Arrays.copyOf(originalData, originalData.length);

            try {
                long duration = executeSort(algo, testData);
                boolean sorted = isSorted(testData);
                System.out.printf("%-20s | %-10d | %-10s\n",
                        algo.getSortName(), duration, (sorted ? "PASS" : "FAIL"));
            } catch (Exception e) {
                System.out.printf("%-20s | %-10s | %-10s\n",
                        algo.getSortName(), "ERROR", e.getClass().getSimpleName());
            } catch (StackOverflowError e) {
                System.out.printf("%-20s | %-10s | %-10s\n",
                        algo.getSortName(), "STACK OVR", "Recursion too deep");
            }
        }
    }

    // --- HELPER METHODS ---

    private static long executeSort(BigOSort algo, int[] data) {
        long start = System.currentTimeMillis();
        algo.run(data);
        long end = System.currentTimeMillis();
        return end - start;
    }

    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(10000); // Random values 0-9999
        }
        return arr;
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }

    private static int getIntInput(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next(); // consume bad input
            return -1;
        }
    }
}