package com.example.arena.streams;

import java.util.*;
import java.util.stream.Collectors;

public class StreamSample {

    // 1. Data Model (Record is perfect for Streams)
    record Employee(String name, String department, int age, double salary) {}

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("Alice", "Engineering", 30, 120000),
                new Employee("Bob", "Sales", 25, 70000),
                new Employee("Charlie", "Engineering", 40, 150000),
                new Employee("Dave", "HR", 35, 90000),
                new Employee("Eve", "Sales", 28, 85000),
                new Employee("Frank", "Engineering", 22, 60000)
        );

        System.out.println("--- 1. FILTER & MAP ---");
        // Goal: Get names of Engineering employees earning > 100k
        List<String> highEarners = employees.stream()
                .filter(e -> "Engineering".equals(e.department()))
                .filter(e -> e.salary() > 100000)
                .sorted(Comparator.comparing(Employee::name)) // Sort Alphabetically
                .map(Employee::name) // Extract Name
                .toList(); // Java 16+ syntax (collect(Collectors.toList()) for older)

        System.out.println("High Earning Engineers: " + highEarners);


        System.out.println("\n--- 2. GROUPING BY ---");
        // Goal: Group employees by Department
        // Map<String, List<Employee>>
        Map<String, List<Employee>> byDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::department));

        byDept.forEach((dept, team) -> {
            System.out.println(dept + ": " + team.size() + " employees");
        });


        System.out.println("\n--- 3. AGGREGATION (Average) ---");
        // Goal: Calculate Average Salary per Department
        // Map<String, Double>
        Map<String, Double> avgSalary = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.averagingDouble(Employee::salary)
                ));

        avgSalary.forEach((dept, avg) ->
                System.out.printf("%s Average: $%.2f%n", dept, avg));


        System.out.println("\n--- 4. PARTITIONING ---");
        // Goal: Split into two lists: Seniors (>30) and Juniors (<=30)
        Map<Boolean, List<Employee>> bySeniority = employees.stream()
                .collect(Collectors.partitioningBy(e -> e.age() > 30));

        System.out.println("Seniors: " + bySeniority.get(true).size());
        System.out.println("Juniors: " + bySeniority.get(false).size());


        System.out.println("\n--- 5. STATISTICS SUMMARY ---");
        // Goal: Get Count, Min, Max, Sum, and Avg of Salaries all at once
        DoubleSummaryStatistics stats = employees.stream()
                .mapToDouble(Employee::salary)
                .summaryStatistics();

        System.out.println("Max Salary: " + stats.getMax());
        System.out.println("Total Payroll: " + stats.getSum());
    }
}