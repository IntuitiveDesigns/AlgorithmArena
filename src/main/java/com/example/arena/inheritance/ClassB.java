package com.example.arena.inheritance;

public class ClassB extends ClassA {
    public String name = "Class B Field"; // SHADOWING!

    public ClassB() {
        System.out.println("2. ClassB Constructor Running");
    }

    @Override
    public void showIdentity() {
        System.out.println("I am Class B");
    }

    // Static methods are HIDDEN, not overridden
    public static void staticMethod() {
        System.out.println("Static Method in B");
    }
}