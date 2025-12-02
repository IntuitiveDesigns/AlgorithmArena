package com.example.arena.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ProductionSimulator
 * A "Broken" application designed to generate specific logs and system states
 * for debugging interviews.
 */
public class ProductionSimulator {

    // 1. MEMORY LEAK STORE
    // Static list that never gets cleared (GC cannot reclaim this)
    private static final List<byte[]> LEAKY_BUCKET = new ArrayList<>();

    // 2. DEADLOCK LOCKS
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("--- PRODUCTION ISSUE SIMULATOR ---");
        System.out.println("1. Simulate High CPU (Infinite Loop)");
        System.out.println("2. Simulate Memory Leak (OOM)");
        System.out.println("3. Simulate Deadlock (Thread Hang)");
        System.out.print("Select Disaster: ");

        int choice = scan.nextInt();

        switch (choice) {
            case 1 -> triggerHighCPU();
            case 2 -> triggerMemoryLeak();
            case 3 -> triggerDeadlock();
            default -> System.out.println("System stable... for now.");
        }
    }

    /**
     * SCENARIO 1: High CPU
     * Symptom: 'top' command shows 100% CPU. App becomes unresponsive.
     * Diagnosis: Thread dump shows runnable thread in tight loop.
     */
    private static void triggerHighCPU() {
        System.out.println("[ALERT] Starting complex calculation...");
        System.out.println("[LOG] Thread-1 processing data...");

        // Classic "condition never met" loop
        int i = 0;
        while (true) {
            i++;
            if (i % 1000000 == 0) {
                // Simulate math work without sleeping
                Math.pow(i, 2);
            }
            // Missing break condition!
        }
    }

    /**
     * SCENARIO 2: Memory Leak
     * Symptom: Application gets slower, eventually crashes with java.lang.OutOfMemoryError.
     * Diagnosis: Heap dump shows millions of byte[] arrays referenced by LEAKY_BUCKET.
     */
    private static void triggerMemoryLeak() {
        System.out.println("[ALERT] Cache service started.");
        int count = 0;
        while (true) {
            // Add 1MB of data every 10ms
            LEAKY_BUCKET.add(new byte[1024 * 1024]);
            count++;

            System.out.println("[LOG] Cache size: " + count + " MB");

            try { Thread.sleep(10); } catch (InterruptedException e) {}
        }
    }

    /**
     * SCENARIO 3: Deadlock
     * Symptom: App is running (CPU low), but requests hang forever.
     * Diagnosis: Thread dump shows "Found one Java-level deadlock".
     */
    private static void triggerDeadlock() {
        System.out.println("[ALERT] Starting Transaction Processor...");

        Thread t1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("Thread-1: Holding Lock A...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread-1: Waiting for Lock B...");
                synchronized (LOCK_B) {
                    System.out.println("Thread-1: Acquired Lock B!");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (LOCK_B) {
                System.out.println("Thread-2: Holding Lock B...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread-2: Waiting for Lock A...");
                synchronized (LOCK_A) {
                    System.out.println("Thread-2: Acquired Lock A!");
                }
            }
        });

        t1.start();
        t2.start();
    }
}