package com.example.arena.inheritance;

public class ClassC extends ClassB {

    public ClassC() {
        System.out.println("3. ClassC Constructor Running");
    }

    @Override
    public void showIdentity() {
        super.showIdentity(); // Calls B's method
        System.out.println("...and also Class C");
    }
}