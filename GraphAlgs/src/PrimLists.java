// Title: Graph Traversal and Shortest Path Algorithms with DFS, BFS, Prim, and Dijkstra
// Due Date: 28/04/2023
// Completed By: Paulina Czarnota C21365726

/*
    Full Program Description:

    This is a Java program that implements a graph using adjacency lists to represent edges. The graph can read data from a text file and display the adjacency list. The program also includes methods to perform a depth-first traversal of the graph and to find the minimum spanning tree of the graph using Prim's algorithm.

    The GraphLists class contains an inner class Node which represents a node in the adjacency list. It has private variables V and E to store the number of vertices and edges respectively. It also has private variables adj to store the adjacency list, z to represent a sentinel node, mst to store the minimum spanning tree, visited to mark visited nodes during traversal, and id to store the id of visited nodes.

    The constructor of GraphLists takes a file name as input and reads data from the file to initialize the adjacency list. The display method displays the adjacency list. The DF method performs a depth-first traversal of the graph starting from a given vertex. The MST_Prim method finds the minimum spanning tree of the graph using Prim's algorithm.

    The toChar method is a private helper function that maps integer vertices to characters. The findMinVertex method is a private helper function that finds the vertex with the minimum weight from a set of vertices that have not been visited.
*/


// Import necessary libraries for I/O operations
import java.io.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Define a GraphLists class
class GraphLists {
    // Inner class to represent a node in the adjacency list
    class Node {

        public int vert;
        public int wgt;
        public Node next; 
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    // mst[] holds values of parent[] from the Prim algorithm for printing
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;

    // Variables used for graph traversal
    private int[] visited;
    private int id;
    
    // Default constructor for the GraphLists class
    public GraphLists(String graphFile) throws IOException {
        // Declare variables to hold graph information
        int u, v;
        int e, wgt;
        Node t;

        // Open and read from the graph file
        FileReader fr = new FileReader(graphFile);

        try(BufferedReader reader = new BufferedReader(fr)) {
            
            String splits = " +"; // Use multiple whitespace as delimiter
            String line = reader.readLine();
            String[] parts = line.split(splits);
            System.out.println("\nParts[] = " + parts[0] + " " + parts[1]);
            
            // Read in the number of vertices and edges from the file
            V = Integer.parseInt(parts[0]);
            E = Integer.parseInt(parts[1]);
            
            // Create a sentinel node
            z = new Node();
            z.next = z;
            
            // Create adjacency lists and initialise to the sentinel node z
            visited = new int[V + 1];
            adj = new Node[V + 1];
            mst = new int[V + 1];
        
            for (v = 1; v <= V; ++v) {

                adj[v] = z;
            }

            // Read in the edges from the text file
            System.out.println("\nReading Edges from Text File:\n");

            for (e = 1; e <= E; ++e) {

                line = reader.readLine();
                parts = line.split(splits);
                u = Integer.parseInt(parts[0]);
                v = Integer.parseInt(parts[1]);
                wgt = Integer.parseInt(parts[2]);
                
                // Print out the edge information
                System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));

                // Add the edge to the adjacency list for both vertices
                t = new Node();
                t.vert = v;
                t.wgt = wgt;
                t.next = adj[u];
                adj[u] = t;

                t = new Node();
                t.vert = u;
                t.wgt = wgt;
                t.next = adj[v];
                adj[v] = t;
            }
        }
    }

    // Convert vertex number into character for pretty printing
    private char toChar(int u) {

        return (char)(u + 64);
    }
    
    // Display the graph representation using adjacency list
    public void display() {
        // Start display method
        int v;
        Node n;

        System.out.println("\nDisplaying Adjacency List:");

        // Loop through each vertex and print out the adjacency list
        for(v = 1; v <= V; ++v) {

            System.out.print("\nadj[" + toChar(v) + "] ->" );

            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }

    // This method initialises a Depth First Traversal of the graph starting at the vertex s
    public void DF(int s) {
        // Set the global variable 'id' to 0, indicating that no vertices have been visited yet
        id = 0;
        
        // Loop through all vertices in the graph and set their visited status to 0 (not visited)
        for (int v = 1; v <= V; v++) { 

            visited[v] = 0;
        }

        // Print out a message indicating that a Depth First Traversal is starting, and which vertex it is starting from
        System.out.print("\nDepth First Graph Traversal:\n");
        System.out.println("Starting with Vertex " + toChar(s));

        // Call the 'dfVisit' method to start visiting vertices using Depth First Traversal starting from vertex s
        dfVisit(0, s);

        System.out.print("\n\n");
    }

    // Recursive Depth First Traversal
    // This method is used to perform a depth-first traversal of a graph in a recursive manner
    private void dfVisit(int prev, int v) {

        Node t;
        int u;

        // Mark the current vertex as visited and assign it an ID.
        visited[v] = ++id;

        // Print a message indicating that the vertex has just been visited along a particular edge
        System.out.print("\nDF just visited vertex " + toChar(v) + " along " + toChar(prev) + "--" + toChar(v));

        // Iterate over each adjacent vertex of the current vertex and perform depth-first traversal on them
        for (t = adj[v]; t != z; t = t.next) {

            u = t.vert;

            if (visited[u] == 0) {

                dfVisit(v, u);
            }
        }
    } 

    // Heap implementation of Prim's algorithm
    // This method finds the vertex with the minimum weight among the unvisited vertices
    private int findMinVertex(int[] wt, boolean[] visited) {

        int minVertex = -1;

        // Iterate over each vertex and check if it's unvisited and has a lower weight than the current minimum
        for (int v = 1; v <= V; v++) {

            if (!visited[v] && (minVertex == -1 || wt[v] < wt[minVertex])) {

                minVertex = v;
            }
        }
        
        // Return the vertex with the minimum weight
        return minVertex;
    }

    // This method uses Prim's algorithm to find the minimum spanning tree of the graph
    public void MST_Prim() {
        // Declare and initialize variables for distances, parents, and visited vertices
        int v, u, w;
        int[] dist, parent;
        boolean[] hPos;
        
        // Initialising arrays
        dist = new int[V + 1]; // the distance from starting vertex
        parent = new int[V + 1]; // array to hold parent of vertex
        hPos = new boolean[V + 1]; // heap Position

        // Initialize distances to infinity and visited vertices to false
        for (v = 0; v <= V; v++) {

            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0; 
            hPos[v] = false;
            visited[v] = 0;
            mst[v] = 0;
        }

        // Set the distance to the first vertex to 0 and its parent to 0 (no parent)
        dist[1] = 0;
        parent[1] = 0;

        // Find the minimum spanning tree using Prim's algorithm
        for (int i = 0; i < V; i++) {
            // Find the vertex with the minimum distance among the unvisited vertices
            u = findMinVertex(dist, hPos);
            hPos[u] = true;

            // Update the distances and parents of adjacent vertices
            Node t = adj[u];

            while (t != z) {
                v = t.vert;
                w = t.wgt;

                if (hPos[v] == false && w < dist[v]) {
                    parent[v] = u;
                    dist[v] = w;
                }
                t = t.next;
            }
        }

        // Calculate and print the total weight of the minimum spanning tree
        int mst_weight = 0;

        for (int i = 1; i <= V; i++) {

            if (dist[i] != Integer.MAX_VALUE) {

                mst_weight += dist[i];
            }
        }

        System.out.println("\n\nAdding to MST: Edge\n");

        // Print the edges of the minimum spanning tree
        for (int i = 1; i <= V; i++) {

            System.out.println(toChar(parent[i]) + " <-- (" + dist[i] + ") --> " + toChar(i));
        }

        // Print the total weight of the minimum spanning tree and the tree itself
        System.out.println("\nMST Weight = " + mst_weight);
        showMST(parent);
    }

    // This method prints the Minimum Spanning Tree (MST) parent array
    // It takes an integer array 'parent' as an input which holds the parent of each vertex in the MST
    public void showMST(int[] parent) {
        // Prints the title of the output
        System.out.println("\nMinimum Spanning Tree Parent Array:\n");
        
        // Iterates over all vertices in the graph
        for (int i = 1; i <= V; i++) {
            // Prints the parent of each vertex in the MST
            System.out.print(toChar(i) + " -> " + toChar(parent[i]) + "\n");
        }
    }

    // Breadth-first search (BFS) shortest path spanning tree on an unweighted graph
    public void BF(int s) {

        Queue q = new Queue();
        id = 0;
        int u, v;

        System.out.print("Breadth First Search:\n");
        // Mark all nodes as unvisited
        for(int i = 1; i <= V; i++) { 

            visited[i] = 0; 
        }

        // Enqueue the root node
        q.enQueue(s); 

        while(!q.isEmpty()) { // repeat V-1 times (while queue isn't empty)
            // Dequeue the front node from the queue
            v = q.deQueue(); 

            if(visited[v] == 0) { // if node has not been visited yet
                // Mark the node as visited and assign an ID to it
                visited[v] = id++;
                
                // For all neighbors of the current node
                for(Node t = adj[v]; t != z ; t = t.next) { 
                    // Get the ID of the neighbor
                    u = t.vert;

                    if(visited[u] == 0) { // if neighbor has not been visited yet
                        // Enqueue the neighbor and mark it as visited
                        q.enQueue(u);  
                    }
                }
                // Print the visited node
                System.out.print("\nBFS visited vertex " + toChar(v) );
            }
        } 
    } 
    
    // This code implements Dijkstra's algorithm to find the shortest path tree (SPT) in a weighted graph from a source vertex to all other vertices
    public void SPT_Dijkstra(int s) {
        // initialize arrays
        int[] dist = new int[V + 1];
        int[] parent = new int[V + 1];
        boolean[] visited = new boolean[V + 1];
        
        // Set initial values for arrays
        for (int i = 1; i <= V; i++) {

            dist[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        dist[s] = 0;
        parent[s] = -1;
        
        // Find shortest paths
        for (int i = 1; i < V; i++) {

            int u = findMinVertex(dist, visited);
            visited[u] = true;
            Node t = adj[u];
            
            while (t != z) {
                int v = t.vert;
                int w = t.wgt;
                
                if (!visited[v] && dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;
                }
                t = t.next;
            }
        }
        
        // Print the shortest path tree
        System.out.print("\nShortest Path Tree (SPT) using Dijkstra's Algorithm:\n\n");
        
        for (int i = 1; i <= V; i++) {

            if (i != s) {

                System.out.print("Vertex " + toChar(i) + ", distance = " + dist[i] + ", path = ");
                printPath(parent, i);
                System.out.println("");
            }
        }
    }
    
    // Print the path from the source node to a given vertex
    private void printPath(int[] parent, int v) {

        if (parent[v] == -1) {

            System.out.print(toChar(v));
        } else {

            printPath(parent, parent[v]);
            System.out.print("->" + toChar(v));
        }
    }
} 

// Driver code for Prim's algorithm on graphs
public class PrimLists {
    // This is the main method of the PrimLists class
    public static void main(String[] args) throws IOException {
        
        try {
            // Display a prompt to the user
            System.out.println("\n\nGraph Traversal and Shortest Path Algorithms with DFS, BFS, Prim, and Dijkstra\n");
            System.out.println("--------------------------------------------------------------------------------------");
            Scanner sc = new Scanner(System.in);
            
            // Prompt the user to enter the name of the graph file
            System.out.println("\nEnter the name of the graph file in .txt extension: ");
            String fName = sc.nextLine();
            
            // Create a file object and a scanner object to read from the file
            File file = new File(fName);
            Scanner fileScanner = new Scanner(file);
            
            // Prompt the user to enter the starting vertex of the graph
            System.out.println("\nEnter the starting vertex of the graph (as a number): ");
            int s = Integer.parseInt(sc.nextLine());
            
            // Create a GraphLists object using the graph file name
            GraphLists g = new GraphLists(fName);
            
            // Display the graph
            g.display();
            // Perform a depth-first search traversal of the graph, starting at vertex s
            g.DF(s);
            // Perform a breadth-first search traversal of the graph, starting at vertex s
            g.BF(s);
            // Find and display the minimum spanning tree of the graph using Prim's algorithm
            g.MST_Prim();
            // Find and display the shortest path tree of the graph using Dijkstra's algorithm, starting at vertex s
            g.SPT_Dijkstra(s);
            
            // Close the file and scanner objects and the scanner for user input
            fileScanner.close();
            sc.close(); // Closing Scanner after use
        } //end try
        catch (Exception e) { //start catch 
            // Display an error message if an exception is thrown while running the program
            System.out.println("\nPlease enter a valid file name with extension .txt, and a valid number of your starting vertex afterwards!");
        } //end catch
    }
}

class Heap { 

    private int[] a; // heap array
    private int[] hPos; // hPos[h[k]] == k
    private int[] dist; // dist[v] = priority of v
    private int N; // heap size

    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) { 

        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    } 

    public boolean isEmpty() { 

        return N == 0;
    } 

    // siftUp from position k. The key or node value at position k
    // may be greater that that of its parent at k/2
    // k is a position in the heap array h
    public void siftUp( int k) { 

        int v = a[k];
        a[0] = 0;
        dist[0] = Integer.MIN_VALUE;

        while (dist[v] < dist[a[k / 2]]) {

            hPos[a[k]]= k / 2; 
            a[k] = a[k / 2];
            k = k / 2;
        }
        a[k] = v;
        hPos[v] = k;
    } 

    // Key of node at position k may be less than that of its
    // children and may need to be moved down some levels
    // k is a position in the heap array h
    public void siftDown( int k) {

        int v, j;
        v = a[k];  

        while(k <= N / 2) {

            j = 2 * k;

            if(j < N && dist[a[j]] > dist[a[j + 1]]) {
                j++;
            }
            if(dist[v] <= dist[a[j]]) {
                break;
            }

            a[k] = a[j];
            hPos[a[k]] = k;
            k = j;
        }
        a[k] = v;
        hPos[v] = k;
    }

    public void printHeap() {

        System.out.print("Heap: ");

        for (int i = 1; i <= N; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    public void insert(int x) { 

        a[++N] = x;
        siftUp(N);
        
    } 

    public int remove() { 

        int v = a[1];
        hPos[v] = 0; // v is no longer in heap        
        a[N + 1] = 0;  // put null node into empty spot
        a[1] = a[N--];
        siftDown(1);
        return v;
    } 

    // display heap values and their priorities or distances
    void display() { 

        if (dist[a[1]] >= 0) { 

            System.out.println( a[1] + "(" + dist[a[1]] + ")" );
        } 
        else if (dist[a[1]] < 0) { 

            System.out.println( a[1] + "(" + -dist[a[1]] + ")" );
        }
        for(int i = 1; i <= N / 2; i = i * 2) { 

            for(int j = 2 * i; j < 4 * i && j <= N; ++j) { 

                System.out.print( a[j] + "(" + dist[a[j]] + ")  ");
            }
            System.out.println();
        } 
    }

} 

// This is an implementation of a queue using a linked list data structure. 
// In this implementation, nodes are added to the back of the queue and removed from the front, 
// hence the use of pointers for the head and tail of the queue which point to the first and last nodes respectively.
class Queue { 
    // Node class for the linked list
    private class Node { 

        int data; // data stored in the node
        Node next; // reference to the next node
    } 
    
    Node z, head, tail; // z node represents the end of the list, head points to the first node, tail points to the last node

    // Constructor to create an empty queue
    public Queue() {

        z = new Node(); 
        z.next = z;
        head = z; 
        tail = null;
    } 

    // Method to add a value to the back of the queue (insert operation)
    public void enQueue(int x) {
         
        Node t;
        t = new Node(); // create a new node
        t.data = x; // set the data of the new node to x
        t.next = z; // set the next reference of the new node to z
        
        // case of an empty list
        if (head == z) { 
            // set the head to the new node
            head = t; 
        } 
        // case of a non-empty list
        else { 
            // set the next reference of the current tail to the new node
            tail.next = t; 
        } 

        tail = t; // set the tail to the new node as it is now at the end of the queue
        // System.out.println("\nLinked List enQueue: " + x + ": ");
        // display();
    }
    
    // Method to remove a value from the front of the queue (remove operation)
    public int deQueue() { 

        int t = head.data; // get the data of the node at the front of the queue
        head = head.next; // move the head to the next node
        // System.out.println("\nLinked list deQueue: " + t + ": ");
        // display();
        return t; // return the data of the node that was removed
    } 

    // Method to check if the queue is empty
    public boolean isEmpty() { 

        return head == head.next; // return true if the head is the z node, false otherwise
    } 

    // Method to display the contents of the queue 
    public void display() { 

        Node t = head;
        // loop through the nodes in the queue
        while (t != t.next) { 
            // print the data of the current node
            System.out.print(t.data + "  "); 
            t = t.next; // move to the next node
        } 
        System.out.println("\n");
    } 
} 