package com.example.arena.patterns.singleton;

/**
 * The "Bill Pugh" Singleton Implementation.
 * * Why this is Interview Gold:
 * 1. Lazy Loading: The instance isn't created until you call getInstance().
 * 2. Thread Safe: Java's ClassLoader guarantees the static inner class is loaded safely.
 * 3. Fast: No 'synchronized' blocks slowing down access.
 */
public class Singleton {

    // 1. Private Constructor: Prevents "new Singleton()" from outside
    private Singleton() {
        System.out.println("Singleton Instance Created!");
    }

    // 2. Static Inner Class (The "Holder")
    // This class is NOT loaded until getInstance() is called.
    private static class SingletonHelper {
        private static final Singleton INSTANCE = new Singleton();
    }

    // 3. Global Access Point
    public static Singleton getInstance() {
        return SingletonHelper.INSTANCE;
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        System.out.println("1. Main started. Singleton is not created yet.");

        System.out.println("2. Calling getInstance() for the first time...");
        Singleton s1 = Singleton.getInstance();

        System.out.println("3. Calling getInstance() a second time...");
        Singleton s2 = Singleton.getInstance();

        // Proof
        if (s1 == s2) {
            System.out.println("4. Success! Both variables point to the same object.");
            System.out.println(s1);
            System.out.println(s2);
        } else {
            System.err.println("Failure! Different objects created.");
        }
    }
}