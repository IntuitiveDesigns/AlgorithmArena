package com.example.arena.inheritance;

public class InheritanceChallenge {

    public static void main(String[] args) {

        System.out.println("--- CHALLENGE 1: Constructor Chaining ---");
        // QUESTION: What prints when we create C?
        // ANSWER: A -> B -> C (Always parents first)
        new ClassC();


        System.out.println("\n--- CHALLENGE 2: Field Shadowing (The Trap) ---");
        ClassB objB = new ClassB();
        ClassA objA = objB; // Upcasting (Polymorphism)

        // METHOD call follows the Object type (ClassB)
        objA.showIdentity();
        // EXPECTED: "I am Class B"

        // FIELD access follows the Reference type (ClassA)
        System.out.println("Field Value: " + objA.name);
        // EXPECTED: "Class A Field" (NOT Class B!)
        // Why? Because fields are not polymorphic in Java.


        System.out.println("\n--- CHALLENGE 3: Static Method Hiding ---");
        ClassA item = new ClassB();
        item.staticMethod();
        // EXPECTED: "Static Method in A"
        // Why? Static methods are bound at compile time based on the reference type (ClassA).


        System.out.println("\n--- CHALLENGE 4: Polymorphism Collection ---");
        ClassA[] zoo = { new ClassA(), new ClassC(), new ClassD() };

        for (ClassA animal : zoo) {
            animal.showIdentity();
        }
        // EXPECTED:
        // I am Class A
        // I am Class B ...and also Class C
        // I am Class D (The Sibling)
    }
}