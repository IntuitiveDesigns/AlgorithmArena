package com.example.arena.hackerrank;

import java.util.*;

public class Template {

    // HackerRank usually names the class 'Solution' and the method 'main'
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // PROMPT 1: Tell the user what to do
        System.out.println("Waiting for input... (Enter a number, e.g., 3)");

        if (scan.hasNextInt()) {
            int n = scan.nextInt();
            System.out.println("Read Size: " + n);

            // PROMPT 2
            System.out.println("Now enter " + n + " integers:");

            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                if (scan.hasNextInt()) {
                    arr[i] = scan.nextInt();
                }
            }
            System.out.println("Read Array: " + Arrays.toString(arr));
        }

        scan.nextLine(); // Consume newline

        // PROMPT 3
        System.out.println("Now enter a text string:");

        if (scan.hasNextLine()) {
            String line = scan.nextLine();
            System.out.println("Read String: " + line);
        }

        scan.close();
    }
}