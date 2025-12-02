package com.example.arena.patterns.singleton;

public class SingletonEager {

    // Eager: Created immediately when the class loads.
    // JVM guarantees this is thread-safe.
    private static final SingletonEager uniqueInstance = new SingletonEager();

    private SingletonEager() {
        System.out.println("Eager Singleton Created!");
    }

    // FIX: Removed 'synchronized'. It is not needed for Eager Loading
    // and slows down performance unnecessarily.
    public static SingletonEager getInstance() {
        return uniqueInstance;
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        System.out.println("1. Main started.");

        // Notice: The constructor might run BEFORE this line prints,
        // depending on when the JVM loads the class.

        System.out.println("2. Calling getInstance...");
        SingletonEager s1 = SingletonEager.getInstance();

        System.out.println("3. Calling getInstance again...");
        SingletonEager s2 = SingletonEager.getInstance();

        if (s1 == s2) {
            System.out.println("Success: Both references point to the same object.");
        }
    }
}