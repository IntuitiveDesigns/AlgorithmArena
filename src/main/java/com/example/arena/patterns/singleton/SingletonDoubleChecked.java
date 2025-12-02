package com.example.arena.patterns.singleton;

public class SingletonDoubleChecked {

    // 'volatile' ensures that multiple threads handle the uniqueInstance variable
    // correctly when it is being initialized.
    // KEY INTERVIEW ANSWER: It prevents "Instruction Reordering".
    private volatile static SingletonDoubleChecked uniqueInstance;

    // Private constructor prevents external instantiation
    private SingletonDoubleChecked() {
        System.out.println("Singleton Instance Created (This should only print ONCE)");
    }

    public static SingletonDoubleChecked getInstance() {
        // Check 1: If it exists, skip the synchronized block (Performance)
        if(uniqueInstance == null) {
            synchronized (SingletonDoubleChecked.class) {
                // Check 2: Double check inside the lock
                if (uniqueInstance == null) {
                    uniqueInstance  = new SingletonDoubleChecked();
                }
            }
        }
        return uniqueInstance;
    }

    // --- TEST RUNNER (Proof of Thread Safety) ---
    public static void main(String[] args) {
        System.out.println("--- Double Checked Locking Test ---");

        // We create a Runnable that tries to get the instance
        Runnable task = () -> {
            SingletonDoubleChecked s = SingletonDoubleChecked.getInstance();
            System.out.println("Thread " + Thread.currentThread().getName() + " got Hash: " + s.hashCode());
        };

        // Create 3 threads racing to get the instance
        Thread t1 = new Thread(task, "T1");
        Thread t2 = new Thread(task, "T2");
        Thread t3 = new Thread(task, "T3");

        t1.start();
        t2.start();
        t3.start();
    }
}