package com.example.arena.numbersequences;

public class Modulo {

    public static void main(String[] args) {
        System.out.println("--- 1. Basic Modulo Check ---");
        checkModulo();

        System.out.println("\n--- 2. Odd/Even Check ---");
        System.out.println("Is 3 odd? " + isOdd(3));
        System.out.println("Is -3 odd? " + isOdd(-3)); // The bug fix test

        System.out.println("\n--- 3. Circular Array Demo ---");
        findFreeIndex();
    }

    public static void checkModulo() {
        int moduloNum = 2;
        // Reduced loop to 10 for readability in console
        for(int x=0; x < 10; x++) {
            System.out.println("x: " + x + " | " + x + " % " + moduloNum + " = " + (x % moduloNum));
        }
    }

    /**
     * Is number odd?
     * FIX: Changed '== 1' to '!= 0' to handle negative numbers correctly.
     * (-3 % 2) is -1, not 1.
     */
    public static boolean isOdd(int num) {
        // Checks the last bit. If 1, it's odd.
        return (num & 1) == 1;
    }

    /**
     * Circular Array Logic (Ring Buffer)
     * This is used heavily in networking and queues (like Kafka).
     */
    public static void findFreeIndex() {
        int queueCapacity = 5; // Reduced size for demo
        int[] circularQueue = new int[queueCapacity];

        System.out.println("Queue Capacity: " + queueCapacity);

        // Simulate inserting 12 items into a size-5 queue
        for(int value = 0; value < 12; value++) {

            // Pattern: index = counter % capacity
            int writeIndex = value % queueCapacity;

            circularQueue[writeIndex] = value;

            System.out.println("Inserted value " + value + " at Index [" + writeIndex + "]");
        }
    }
}