package com.example.arena.datastructures;

// 1. Extend the Contract
public class MatrixSample extends BigODataStructures {

    private int[][] matrix;

    public MatrixSample() {
        super("2D Matrix (int[][])");
        // Initialize a 3x3 sample grid
        this.matrix = new int[][]{
                {10, 20, 30},
                {40, 50, 60},
                {70, 80, 90}
        };
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(1)"; } // matrix[r][c] is instant

    @Override
    public String getAvgSearch() { return "O(R * C)"; } // R = Rows, C = Cols

    @Override
    public String getAvgInsertion() { return "O(1)"; } // Setting a value is instant

    @Override
    public String getAvgDeletion() { return "O(1)"; } // Reseting a value is instant

    @Override
    public String getWorstAccess() { return "O(1)"; }

    @Override
    public String getWorstSearch() { return "O(R * C)"; } // Must scan every cell

    @Override
    public String getWorstInsertion() { return "O(R * C)"; } // If resizing the matrix (copying)

    @Override
    public String getWorstDeletion() { return "O(1)"; }

    @Override
    public String getSpaceComplexity() { return "O(R * C)"; }

    // --- LOGIC ---

    /**
     * ACCESS (Traversal)
     * Standard nested loops: Row then Column.
     */
    public void access() {
        System.out.println("--- Matrix Traversal ---");
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                System.out.print(matrix[r][c] + "\t");
            }
            System.out.println(); // New line after every row
        }
    }

    /**
     * SEARCH (Linear)
     * Scans every cell.
     */
    public boolean search(int value) {
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                if (matrix[r][c] == value) return true;
            }
        }
        return false;
    }

    // Helper to match interface
    public void search() {
        System.out.println("Found 50? " + search(50));
    }

    /**
     * INSERTION (Set Value)
     * Matrices are fixed size, so 'insertion' usually means 'update cell'.
     */
    public void insertion(int row, int col, int value) {
        if (isValid(row, col)) {
            matrix[row][col] = value;
            System.out.println("Set [" + row + "][" + col + "] to " + value);
        } else {
            System.out.println("Invalid coordinate: " + row + "," + col);
        }
    }

    public void insertion() {
        insertion(1, 1, 99); // Change center to 99
    }

    /**
     * DELETION (Reset Value)
     * Usually implies setting to 0 or -1.
     */
    public void deletion(int row, int col) {
        if (isValid(row, col)) {
            matrix[row][col] = 0;
            System.out.println("Deleted (Reset) [" + row + "][" + col + "]");
        }
    }

    public void deletion() {
        deletion(0, 0);
    }

    // Helper to check bounds (Crucial for Matrix problems!)
    private boolean isValid(int r, int c) {
        return r >= 0 && r < matrix.length && c >= 0 && c < matrix[0].length;
    }

    // --- INTERVIEW ALGORITHM ---

    /**
     * SEARCH SORTED MATRIX (The "Staircase" Search)
     * If rows are sorted and columns are sorted, we don't need O(R*C).
     * We can do O(R + C).
     * Logic: Start at Top-Right. If too big, go Left. If too small, go Down.
     */
    public boolean searchSorted(int target) {
        int row = 0;
        int col = matrix[0].length - 1; // Top-Right corner

        while (row < matrix.length && col >= 0) {
            int current = matrix[row][col];
            if (current == target) {
                return true;
            } else if (current > target) {
                col--; // Current is too big, move LEFT
            } else {
                row++; // Current is too small, move DOWN
            }
        }
        return false;
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        MatrixSample grid = new MatrixSample();

        System.out.println("Structure: " + grid.getDataStructureName());

        // 1. Access
        grid.access();

        // 2. Insert (Update)
        grid.insertion(); // Change 50 -> 99
        grid.access();

        // 3. Search
        grid.search();

        // 4. Sorted Search (Optimization)
        System.out.println("Optimized Search for 70: " + grid.searchSorted(70));
    }
}