package com.example.arena.datastructures;

// 1. Extend the Study Contract
public class LinkedListSample extends BigODataStructures {

    // The Definition used in 99% of Interviews
    // (Static inner class so it doesn't need an instance of the outer class)
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    private ListNode head;

    // 2. Constructor
    public LinkedListSample() {
        super("Singly Linked List");
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(n)"; } // Must traverse from head

    @Override
    public String getAvgSearch() { return "O(n)"; }

    @Override
    public String getAvgInsertion() { return "O(1)"; } // At the Head

    @Override
    public String getAvgDeletion() { return "O(1)"; } // At the Head

    @Override
    public String getWorstAccess() { return "O(n)"; }

    @Override
    public String getWorstSearch() { return "O(n)"; }

    @Override
    public String getWorstInsertion() { return "O(1)"; } // Always O(1) at head

    @Override
    public String getWorstDeletion() { return "O(n)"; } // If deleting the last node (must find it first)

    @Override
    public String getSpaceComplexity() { return "O(n)"; }

    // --- LOGIC ---

    /**
     * INSERTION (At Head)
     * Big O: O(1) - The main advantage over Arrays! No shifting required.
     */
    public void insertion(int value) {
        ListNode newNode = new ListNode(value);
        newNode.next = head; // Point new node to old head
        head = newNode;      // Update head pointer
        System.out.println("Inserted at Head: " + value);
    }

    // Helper to add many for testing
    public void addMany(int... values) {
        for (int v : values) insertion(v);
    }

    /**
     * ACCESS (Get by Index)
     * Big O: O(n) - We have to walk the chain.
     */
    public void access(int index) {
        ListNode current = head;
        int count = 0;
        while (current != null) {
            if (count == index) {
                System.out.println("Value at index " + index + ": " + current.val);
                return;
            }
            count++;
            current = current.next;
        }
        System.out.println("Index " + index + " out of bounds.");
    }

    // Void placeholder
    public void access() { access(0); }

    /**
     * SEARCH (Contains)
     * Big O: O(n)
     */
    public boolean search(int value) {
        ListNode current = head;
        while (current != null) {
            if (current.val == value) return true;
            current = current.next;
        }
        return false;
    }

    public void search() {
        System.out.println("Contains 99? " + search(99));
    }

    /**
     * DELETION (By Value)
     * Logic: We need to keep track of 'prev' so we can link prev.next to current.next
     */
    public void deletion(int value) {
        if (head == null) return;

        // Case 1: Delete Head
        if (head.val == value) {
            head = head.next;
            System.out.println("Deleted Head: " + value);
            return;
        }

        // Case 2: Delete Middle/Tail
        ListNode current = head;
        ListNode prev = null;

        while (current != null && current.val != value) {
            prev = current;
            current = current.next;
        }

        // If found
        if (current != null) {
            prev.next = current.next; // Skip over 'current'
            System.out.println("Deleted: " + value);
        } else {
            System.out.println("Value " + value + " not found.");
        }
    }

    public void deletion() {
        if (head != null) deletion(head.val);
    }

    // --- INTERVIEW ALGORITHMS ---

    // Example: Print the list
    public void printList() {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    // Example: Reverse (Classic Question)
    public void reverse() {
        ListNode prev = null;
        ListNode current = head;
        while (current != null) {
            ListNode nextTemp = current.next; // Save next
            current.next = prev;              // Reverse pointer
            prev = current;                   // Move prev forward
            current = nextTemp;               // Move current forward
        }
        head = prev; // Update global head
        System.out.println("List Reversed.");
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        LinkedListSample list = new LinkedListSample();

        System.out.println(list.bigOContract());
        System.out.println();

        System.out.println("Structure: " + list.getDataStructureName());

        // 1. Insert (Note: Inserting at head reverses order of entry)
        list.addMany(10, 20, 30, 40, 50);
        list.printList(); // 50 -> 40 -> 30 -> 20 -> 10 -> null

        // 2. Access
        list.access(2); // Index 2 is 30

        // 3. Search
        System.out.println("Found 20? " + list.search(20));

        // 4. Delete
        list.deletion(30);
        list.printList(); // 50 -> 40 -> 20 -> 10 -> null

        // 5. Reverse (The Interview Special)
        list.reverse();
        list.printList(); // 10 -> 20 -> 40 -> 50 -> null
    }
}