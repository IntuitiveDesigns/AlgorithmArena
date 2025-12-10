package com.example.arena.datastructures;

public class BinaryTreeSearchSample extends BigODataStructures {

    static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    public BinaryTreeSearchSample() {
        super("Binary Search Tree (Iterative)");
        this.root = null;
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(log n)"; }

    @Override
    public String getAvgSearch() { return "O(log n)"; }

    @Override
    public String getAvgInsertion() { return "O(log n)"; }

    @Override
    public String getAvgDeletion() { return "O(log n)"; }

    @Override
    public String getWorstAccess() { return "O(n)"; }

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(n)"; }

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(1) for Search/Insert (Iterative)"; }

    // --- LOGIC ---

    /**
     * ACCESS
     * Iterative In-Order Traversal (Using a Stack manually)
     * This is a classic "Hard" interview question.
     */
    public void access() {
        if (root == null) return;

        java.util.Stack<Node> stack = new java.util.Stack<>();
        Node current = root;

        System.out.print("Iterative Traversal: ");

        while (current != null || !stack.isEmpty()) {
            // 1. Go Left as far as possible
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // 2. Process Node
            current = stack.pop();
            System.out.print(current.value + " ");

            // 3. Go Right
            current = current.right;
        }
        System.out.println();
    }

    /**
     * SEARCH (Iterative)
     * Value: O(1) Space Complexity (No recursion stack used)
     */
    public boolean search(int value) {
        Node current = root;

        while (current != null) {
            if (value == current.value) {
                return true; // Found
            } else if (value < current.value) {
                current = current.left; // Go left
            } else {
                current = current.right; // Go right
            }
        }
        return false; // Not found
    }

    /**
     * SEARCH (Void version to match your interface)
     */
    public void search() {
        // Just a placeholder to satisfy the specific method signature if needed,
        // but typically search requires an argument.
        System.out.println("Search requires a value parameter.");
    }

    /**
     * INSERTION (Iterative)
     */
    public void insertion(int value) {
        Node newNode = new Node(value);

        // Case 1: Tree is empty
        if (root == null) {
            root = newNode;
            System.out.println("Inserted root: " + value);
            return;
        }

        // Case 2: Find the spot
        Node current = root;
        Node parent = null;

        while (true) {
            parent = current;
            if (value < current.value) {
                current = current.left;
                if (current == null) {
                    parent.left = newNode;
                    System.out.println("Inserted " + value + " to left of " + parent.value);
                    return;
                }
            } else {
                current = current.right;
                if (current == null) {
                    parent.right = newNode;
                    System.out.println("Inserted " + value + " to right of " + parent.value);
                    return;
                }
            }
        }
    }

    /**
     * DELETION
     * (Deletion is extremely complex to do Iteratively in an interview.
     * It is acceptable to use Recursion for deletion even in an iterative class,
     * but here is the logic for completeness).
     */
    public void deletion(int value) {
        // For brevity and sanity, we often stick to Recursive for deletion
        // because pointer management is messy iteratively.
        // We will call the recursive helper here (same as previous file).
        root = deleteRec(root, value);
        System.out.println("Deleted: " + value);
    }

    // Reusing the recursive logic because Iterative Deletion is 100 lines of code
    private Node deleteRec(Node root, int value) {
        if (root == null) return root;

        if (value < root.value) root.left = deleteRec(root.left, value);
        else if (value > root.value) root.right = deleteRec(root.right, value);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            root.value = minValue(root.right);
            root.right = deleteRec(root.right, root.value);
        }
        return root;
    }

    private int minValue(Node root) {
        int minv = root.value;
        while (root.left != null) {
            minv = root.left.value;
            root = root.left;
        }
        return minv;
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        BinaryTreeSearchSample bst = new BinaryTreeSearchSample();

        bst.insertion(50);
        bst.insertion(30);
        bst.insertion(70);
        bst.insertion(20);
        bst.insertion(40);

        bst.access(); // Iterative Traversal: 20 30 40 50 70

        System.out.println(bst.bigOContract());
        System.out.println();

        System.out.println("Search 40: " + bst.search(40)); // true
        System.out.println("Search 99: " + bst.search(99)); // false
    }
}