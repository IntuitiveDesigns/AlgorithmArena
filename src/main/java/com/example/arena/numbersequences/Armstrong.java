package com.example.arena.numbersequences;

/**
 * An Armstrong number (for 3 digits) is when the sum of the cubes of its digits equals the number itself.
 * Example: 371 = 3*3*3 + 7*7*7 + 1*1*1
 * 27 + 343 + 1 = 371
 */
public class Armstrong {

    public boolean checkThreeDigitNum(int num) {
        int originalNum = num;
        int result = 0;

        while (originalNum != 0) {
            int remainder = originalNum % 10;

            // FIX: Add the CUBE of the digit.
            // Do not multiply by 'result'.
            result += remainder * remainder * remainder;

            originalNum /= 10;
        }

        // Return the comparison directly
        return result == num;
    }

    // Works for ANY number of digits (153, 1634, 54748, etc.)
    public boolean isArmstrongGeneral(int num) {
        int originalNum = num;
        int sum = 0;

        // 1. Calculate number of digits dynamically
        // (String conversion is the easiest/cleanest way to do this in Java)
        int power = String.valueOf(num).length();

        while (originalNum != 0) {
            int digit = originalNum % 10;

            // 2. Raise digit to the power of the total digit count
            sum += Math.pow(digit, power);

            originalNum /= 10;
        }

        return sum == num;
    }
}