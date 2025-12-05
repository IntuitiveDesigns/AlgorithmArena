package com.example.arena.hackerrank;

public class TrappingRainWater {

    /**
     * THE CHALLENGE:
     * Given n non-negative integers representing an elevation map where the width of each bar is 1,
     * compute how much water it can trap after raining.
     *
     * - Brute Force: Check every bar, look left/right for max heights. Time: O(N^2). Space: O(1).
     * - Dynamic Prog: Pre-compute LeftMax[] and RightMax[] arrays. Time: O(N). Space: O(N).
     * - Two Pointers: Move inward, tracking max height. Time: O(N). Space: O(1).
     *
     * We implement the TWO POINTER (Optimal) solution.
     */
    public static int solve(int[] height) {
        if (height == null || height.length < 3) {
            return 0; // Cannot trap water with less than 3 bars
        }

        int left = 0;
        int right = height.length - 1;

        int leftMax = 0;
        int rightMax = 0;

        int waterTrapped = 0;

        while (left < right) {
            // Logic: Water level is limited by the *shorter* of the two walls (left vs right).

            if (height[left] < height[right]) {
                // We are on the left side (it is lower).
                // If current bar is lower than LeftMax, it traps water.
                if (height[left] >= leftMax) {
                    leftMax = height[left]; // Update max (No water trapped here)
                } else {
                    waterTrapped += (leftMax - height[left]);
                }
                left++;
            } else {
                // We are on the right side (it is lower or equal).
                // If current bar is lower than RightMax, it traps water.
                if (height[right] >= rightMax) {
                    rightMax = height[right]; // Update max
                } else {
                    waterTrapped += (rightMax - height[right]);
                }
                right--;
            }
        }

        return waterTrapped;
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        // Example: [0,1,0,2,1,0,1,3,2,1,2,1]
        // This visualizes a "valley" shape.
        int[] elevationMap = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};

        System.out.println("--- Trapping Rain Water ---");
        System.out.println("Elevation Map: " + java.util.Arrays.toString(elevationMap));

        // Expected Output: 6 units of water
        int result = solve(elevationMap);

        System.out.println("Water Trapped: " + result);
    }
}