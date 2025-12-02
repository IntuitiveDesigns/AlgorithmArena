package com.example.arena.datastructures;

import com.example.arena.error.InvalidArraySize;

import java.util.Arrays;

// 1. EXTEND the abstract class
public class ArraySample extends BigODataStructures {

    // 2. CONSTRUCTOR: Pass the name to the parent class
    public ArraySample() {
        super("Static Array");
    }

    // --- BIG O CONTRACT IMPLEMENTATION (The Study Guide) ---

    @Override
    public String getAvgAccess() { return "O(1)"; }

    @Override
    public String getAvgSearch() { return "O(n)"; }

    @Override
    public String getAvgInsertion() { return "O(n)"; }

    @Override
    public String getAvgDeletion() { return "O(n)"; }

    @Override
    public String getWorstAccess() { return "O(1)"; }

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(n)"; }

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(n)"; }


    // --- ACTUAL LOGIC (The Interview Code) ---

    /**
     * Big O: O(1)
     * Explanation: Access is instant because arrays are stored in contiguous memory.
     * We just calculate: Start_Address + (Index * Element_Size).
     */
    public int access(int[] myArray, int index) {
        try {
            return myArray[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Index " + index + " is out of bounds.");
            return -1;
        }
    }

    /**
     * Big O: O(n)
     * Explanation: In the worst case (item is at the end or not found),
     * we must check every single index.
     */
    public int search(int[] myArray, int valueToFind) {
        for (int i = 0; i < myArray.length; i++) {
            if (myArray[i] == valueToFind) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Big O: O(n)
     * Explanation: Arrays are fixed size. We must create a NEW array and copy all N elements.
     */
    public int[] insertion(int[] myArray, int valueToAdd) {
        // 1. Create new array with +1 size
        int[] newArray = new int[myArray.length + 1];

        // 2. Copy old elements
        for (int i = 0; i < myArray.length; i++) {
            newArray[i] = myArray[i];
        }

        // 3. Add new element at the END
        newArray[myArray.length] = valueToAdd;

        return newArray;
    }

    /**
     * Big O: O(n)
     * Explanation: We must shift all elements after the deleted index to the left.
     */
    public int[] deleteAtIndex(int[] myArray, int indexToDelete) {
        if (indexToDelete < 0 || indexToDelete >= myArray.length) {
            // Note: You might need to make sure InvalidArraySize exists in your error package
            // or just throw IllegalArgumentException
            throw new InvalidArraySize("Invalid Index");
        }

        int[] newArray = new int[myArray.length - 1];
        int newIndex = 0;

        for (int i = 0; i < myArray.length; i++) {
            if (i == indexToDelete) {
                continue; // Skip this one
            }
            newArray[newIndex++] = myArray[i];
        }
        return newArray;
    }

    /**
     * Bonus: Matrix Printing
     */
    public void print2DArray(int[][] matrix) {
        System.out.println("Printing 2D Matrix:");
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.print(matrix[row][col] + " ");
            }
            System.out.println();
        }
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        ArraySample tool = new ArraySample();

        // 1. Verify the Contract
        System.out.println("Structure: " + tool.getDataStructureName());
        System.out.println("Access Complexity: " + tool.getAvgAccess());

        // 2. Test the Logic
        int[] data = {10, 20, 30, 40};

        System.out.println("\n--- ACCESS & SEARCH ---");
        System.out.println("Access Index 2: " + tool.access(data, 2));
        System.out.println("Search for 20: " + tool.search(data, 20));

        System.out.println("\n--- INSERTION ---");
        data = tool.insertion(data, 99);
        System.out.println(Arrays.toString(data));

        System.out.println("\n--- DELETION ---");
        data = tool.deleteAtIndex(data, 0); // Delete 10
        System.out.println(Arrays.toString(data));
    }
}