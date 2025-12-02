package com.example.arena.inheritance;

public class ClassA {
    public String name = "Class A Field";

    public ClassA() {
        System.out.println("1. ClassA Constructor Running");
    }

    public void showIdentity() {
        System.out.println("I am Class A");
    }

    public static void staticMethod() {
        System.out.println("Static Method in A");
    }
}