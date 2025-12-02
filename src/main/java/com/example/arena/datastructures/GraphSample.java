package com.example.arena.datastructures;

import java.util.*;

public class GraphSample extends BigODataStructures {

    // Adjacency List: Node ID -> List of Neighbors
    private final Map<Integer, List<Integer>> adjList;

    public GraphSample() {
        super("Graph (Adjacency List)");
        this.adjList = new HashMap<>();
    }

    // --- BIG O CONTRACT ---

    @Override
    public String getAvgAccess() { return "O(V + E)"; } // DFS/BFS Traversal

    @Override
    public String getAvgSearch() { return "O(V + E)"; } // BFS Shortest Path

    @Override
    public String getAvgInsertion() { return "O(1)"; } // Adding an edge

    @Override
    public String getAvgDeletion() { return "O(degree)"; } // Removing edge from list

    @Override
    public String getWorstAccess() { return "O(V + E)"; }

    @Override
    public String getWorstSearch() { return "O(V + E)"; }

    @Override
    public String getWorstInsertion() { return "O(1)"; } // Map put is O(1)

    @Override
    public String getWorstDeletion() { return "O(V)"; } // If connected to all nodes

    @Override
    public String getSpaceComplexity() { return "O(V + E)"; } // Vertices + Edges

    // --- LOGIC ---

    /**
     * INSERTION (Adding an Edge)
     * We add the connection to both nodes (Undirected Graph).
     */
    public void addEdge(int source, int dest) {
        // computeIfAbsent creates the list if it doesn't exist yet
        adjList.computeIfAbsent(source, k -> new ArrayList<>()).add(dest);
        adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(source);
    }

    public void insertion() {
        System.out.println("Adding demo edges...");
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(1, 2);
        addEdge(2, 0); // Duplicate edge check logic could go here
        addEdge(2, 3);
        addEdge(3, 3);
    }

    /**
     * ACCESS (DFS - Depth First Search)
     * Uses a Stack (or Recursion). Used for "Exploring" or "Backtracking" puzzles.
     */
    public void access() {
        System.out.println("DFS Traversal starting from Node 2:");
        dfs(2, new HashSet<>());
        System.out.println();
    }

    // Recursive DFS Helper
    private void dfs(int node, Set<Integer> visited) {
        // 1. Visit
        System.out.print(node + " ");
        visited.add(node);

        // 2. Explore Neighbors
        for (int neighbor : adjList.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }

    /**
     * SEARCH (BFS - Breadth First Search)
     * Uses a Queue. Used for "Shortest Path" in unweighted graphs.
     * This finds if a path exists from Start to Target.
     */
    public boolean search(int start, int target) {
        if (!adjList.containsKey(start)) return false;

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == target) return true;

            for (int neighbor : adjList.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return false;
    }

    public void search() {
        // Demo search
        boolean found = search(0, 3);
        System.out.println("Path exists from 0 to 3? " + found);
    }

    /**
     * DELETION
     * Removing an edge requires finding it in the neighbor list.
     */
    public void removeEdge(int source, int dest) {
        List<Integer> srcNeighbors = adjList.get(source);
        List<Integer> destNeighbors = adjList.get(dest);

        if (srcNeighbors != null) srcNeighbors.remove(Integer.valueOf(dest));
        if (destNeighbors != null) destNeighbors.remove(Integer.valueOf(source));

        System.out.println("Removed edge between " + source + " and " + dest);
    }

    public void deletion() {
        removeEdge(0, 1);
    }

    // --- TEST RUNNER ---
    public static void main(String[] args) {
        GraphSample graph = new GraphSample();

        // 1. Build Graph
        // 0 -- 1
        // |    |
        // 2 -- 3
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        graph.addEdge(2, 3);
        graph.addEdge(3, 3);

        // 2. DFS (Access)
        graph.access();

        // 3. BFS (Search)
        graph.search(); // Check 0 -> 3

        // 4. Delete
        graph.deletion();
    }
}