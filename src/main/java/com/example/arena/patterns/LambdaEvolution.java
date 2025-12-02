package com.example.arena.patterns;

import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaEvolution {

    // --- 1. THE OLD WAY (Pre-Java 8) ---
    // We had to define an interface for every single behavior.
    interface ValidationStrategy {
        boolean isValid(String s);
    }

    // We had to create specific classes or anonymous inner classes
    public static void validateOldSchool(String input, ValidationStrategy strategy) {
        if (strategy.isValid(input)) {
            System.out.println("[Old] Valid: " + input);
        } else {
            System.out.println("[Old] Invalid: " + input);
        }
    }

    // --- 2. THE NEW WAY (Java 8+) ---
    // We use standard Functional Interfaces: Predicate, Function, Consumer, Supplier
    // No custom interfaces needed!
    public static void validateModern(String input, Predicate<String> strategy) {
        if (strategy.test(input)) {
            System.out.println("[New] Valid: " + input);
        } else {
            System.out.println("[New] Invalid: " + input);
        }
    }

    // --- 3. THE "STRATEGY" FACTORY ---
    // We can return logic (functions) as data.
    public static Function<Integer, Integer> getMathStrategy(String type) {
        return switch (type) {
            case "DOUBLE" -> (n) -> n * 2;
            case "SQUARE" -> (n) -> n * n;
            default       -> (n) -> n;
        };
    }

    public static void main(String[] args) {
        String data = "Oracle";

        // Scenario A: The Verbose Anonymous Class
        validateOldSchool(data, new ValidationStrategy() {
            @Override
            public boolean isValid(String s) {
                return s.length() > 3;
            }
        });

        // Scenario B: The Lambda (One Liner)
        // Replaces the Strategy Interface entirely
        validateModern(data, s -> s.startsWith("O"));
        validateModern(data, String::isEmpty); // Method Reference

        // Scenario C: Dynamic Logic
        int num = 5;
        Function<Integer, Integer> strategy = getMathStrategy("SQUARE");
        System.out.println("Strategy Result: " + strategy.apply(num)); // 25
    }
}