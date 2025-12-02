package com.example.arena.patterns.singleton;

/**
 * The "Enum" Singleton.
 * Recommended by Joshua Bloch (author of Effective Java).
 *
 * WHY THIS IS THE BEST IMPLEMENTATION:
 * 1. Thread Safe: The JVM guarantees Enum constants are initialized only once.
 * 2. Serialization Safe: Handled automatically by Java (no readResolve() needed).
 * 3. Reflection Safe: You cannot accidentally create another instance using Reflection.
 */
public enum SingletonEnum {
    INSTANCE; // The single instance

    // You can have standard fields and methods just like a class
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        // Usage is slightly different:
        // You don't call getInstance(), you just access .INSTANCE

        SingletonEnum singleton = SingletonEnum.INSTANCE;

        System.out.println("1. Setting value to 100...");
        singleton.setValue(100);

        // Retrieve via a different reference
        SingletonEnum anotherRef = SingletonEnum.INSTANCE;
        System.out.println("2. Retrieving from second reference: " + anotherRef.getValue());

        if (singleton == anotherRef) {
            System.out.println("3. Success! Both point to the exact same object.");
        }
    }
}