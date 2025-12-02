package com.example.arena.utility;

import com.example.arena.model.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utility {

    private static final Random rd = new Random();

    public static int[] generateRandomIntArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rd.nextInt(100);
        }
        return arr;
    }

    public static char[] generateRandomCharArray(int size) {
        char[] charArr = new char[size];
        for (int i = 0; i < charArr.length; i++) {
            charArr[i] = (char) rd.nextInt(256);
        }
        return charArr;
    }

    public static List<Integer> generateRandomListIntArray(int size) {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // FIX: Removed 'new Integer()'. Java does this automatically (Autoboxing).
            arr.add(rd.nextInt(100));
        }
        return arr;
    }

    public static void diffTimestamp(long startMillis, long endMillis) {
        // 1. Create the Record correctly (All at once)
        // We convert milliseconds (long) to Instant objects
        Time time = new Time(
                java.time.Instant.ofEpochMilli(startMillis),
                java.time.Instant.ofEpochMilli(endMillis)
        );

        // 2. Calculate duration locally for printing
        long duration = endMillis - startMillis;

        int seconds = (int) (duration / 1000) % 60;
        int minutes = (int) ((duration / (1000 * 60)) % 60);
        int hours   = (int) ((duration / (1000 * 60 * 60)) % 24);
        long millis = duration % 1000;

        System.out.println("--- Time Comparison ---");
        // Records use .startTime() instead of .getStartTime()
        System.out.println("Start: " + time.startTime());
        System.out.println("End:   " + time.endTime());

        System.out.println("Difference: " + duration + " ms");
        System.out.printf("Breakdown: %d hr : %d min : %d sec : %d ms%n",
                hours, minutes, seconds, millis);
    }

    public static void printArray(int[] arr) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public static void printCharArray(char[] arr) {
        System.out.println(new String(arr));
    }
}