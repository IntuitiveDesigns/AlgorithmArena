package com.example.arena.datastructures;

public class BinaryTreeSample extends BigODataStructures {

    // 1. The "Node" Class (The building block of trees)
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

    public BinaryTreeSample() {
        super("Binary Search Tree (BST)");
        this.root = null;
    }

    // --- BIG O CONTRACT IMPLEMENTATION ---

    @Override
    public String getAvgAccess() { return "O(log n)"; } // Traversal is O(n), but access by height is log n

    @Override
    public String getAvgSearch() { return "O(log n)"; }

    @Override
    public String getAvgInsertion() { return "O(log n)"; }

    @Override
    public String getAvgDeletion() { return "O(log n)"; }

    @Override
    public String getWorstAccess() { return "O(n)"; } // Skewed tree (linked list)

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(n)"; }

    @Override
    public String getWorstDeletion() { return "O(n)"; }

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // --- ACTUAL LOGIC ---

    /**
     * ACCESS (Traversal)
     * In-Order Traversal: Left -> Root -> Right
     * Prints values in sorted order.
     */
    public void access() {
        System.out.print("In-Order Traversal: ");
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(Node root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.print(root.value + " ");
            inOrderRec(root.right);
        }
    }

    /**
     * SEARCH
     * Logic: If value < root, go left. If value > root, go right.
     */
    public boolean search(int value) {
        return searchRec(root, value);
    }

    private boolean searchRec(Node root, int value) {
        // Base Cases: root is null or key is present at root
        if (root == null) return false;
        if (root.value == value) return true;

        // Val is greater than root's key
        if (value < root.value)
            return searchRec(root.left, value);

        // Val is less than root's key
        return searchRec(root.right, value);
    }

    /**
     * INSERTION
     * Logic: Walk down the tree until you find a null spot in the correct position.
     */
    public void insertion(int value) {
        root = insertRec(root, value);
        System.out.println("Inserted: " + value);
    }

    private Node insertRec(Node root, int value) {
        if (root == null) {
            root = new Node(value);
            return root;
        }

        if (value < root.value)
            root.left = insertRec(root.left, value);
        else if (value > root.value)
            root.right = insertRec(root.right, value);

        return root;
    }

    /**
     * DELETION
     * Logic:
     * 1. Node is leaf: Just remove.
     * 2. Node has 1 child: Replace node with child.
     * 3. Node has 2 children: Find smallest in Right Subtree, copy value, delete that node.
     */
    public void deletion(int value) {
        root = deleteRec(root, value);
        System.out.println("Deleted: " + value);
    }

    private Node deleteRec(Node root, int value) {
        if (root == null) return root;

        // Traverse to find the node
        if (value < root.value)
            root.left = deleteRec(root.left, value);
        else if (value > root.value)
            root.right = deleteRec(root.right, value);
        else {
            // Found the node. Now delete it.

            // Case 1 & 2: One child or no child
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            // Case 3: Two children
            // Get the smallest value in the right subtree (Successor)
            root.value = minValue(root.right);

            // Delete the inorder successor
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
        BinaryTreeSample bst = new BinaryTreeSample();

        // 1. Build the tree
        bst.insertion(50);
        bst.insertion(30);
        bst.insertion(20);
        bst.insertion(40);
        bst.insertion(70);
        bst.insertion(60);
        bst.insertion(80);

        // 2. Traversal
        // Expected: 20 30 40 50 60 70 80
        bst.access();

        // 3. Search
        System.out.println("Found 60? " + bst.search(60)); // true
        System.out.println("Found 99? " + bst.search(99)); // false

        // 4. Delete
        bst.deletion(20); // Leaf node
        bst.access();

        bst.deletion(30); // Node with one child (40)
        bst.access();

        bst.deletion(50); // Root node with two children
        bst.access();
    }
}