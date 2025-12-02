package com.example.arena.model;

import lombok.Data;
import lombok.NoArgsConstructor;

// @Data is the "Super Annotation". It includes Getter, Setter, toString, equals, and hashCode.
@Data
@NoArgsConstructor
public class TwoDGraph {

    // 1. ALWAYS make model fields private
    private String graphName;
    private String xAxisName;
    private String yAxisName;

    // 2. Arrays are fine, but ensure they match in length logic later
    private int[] xAxisValues;
    private int[] yAxisValues;

    private Time time;

    // 3. Replaced java.awt.Color with String (e.g. "#FF0000")
    // This keeps your model lightweight and server-friendly.
    private String colorHex;

    // 4. "Senior Engineer" Addition: Validation Logic
    // Lombok generates the standard getters/setters, but you can add custom logic too.
    public boolean isValid() {
        if (xAxisValues == null || yAxisValues == null) return false;
        return xAxisValues.length == yAxisValues.length;
    }
}