// Title: Kruskal's Minimum Spanning Tree Algorithm
// Due Date: 28/04/2023
// Completed By: Paulina Czarnota C21365726

/*
    Full Program Description:

    This is a Java program that defines three classes: Edge, Heap, and UnionFindSets, which are used to implement the Kruskal's algorithm for finding the minimum spanning tree of a graph.

    The Edge class defines a data structure to represent an edge of a graph. It has three instance variables: u, v, and wgt (weight), which represent the two vertices that the edge connects, and the weight of the edge. It also has a default constructor, a parameterized constructor, a show() method that prints out an edge in a readable format, and a toChar() method that converts a vertex integer into a character for pretty printing.

    The Heap class implements a heap data structure. It has four instance variables: h, N, heapS, and edge. h is an integer array that represents the heap. N is the number of vertices in the graph. heapS is the size of the heap. edge is an array that stores the edges of the graph, where edge[v].wgt is the priority of vertex v. The class has a constructor that takes the number of vertices and an array of edges as inputs. It initializes the heap array with indices of the edges array, and converts the heap array into a proper heap using the siftDown() method. The class also has a siftDown() method that moves a node down the heap until it is in a position where it is greater than or equal to its children, and a remove() method that removes the edge with the highest priority from the heap and returns its value.

    The UnionFindSets class implements the Union-Find data structure, which is used to keep track of a partition of a set of elements into disjoint subsets. It has three instance variables: treeParent, rank, and N. treeParent is an array storing the parent of each vertex. rank is an array storing the rank of each vertex. N is the number of vertices in the Union-Find set. The class has a constructor that initializes each vertex as its own parent and with rank 0, and a findSet() method that returns the root vertex of the subset containing a given vertex. The class also has a union() method that combines two subsets into a single subset by connecting the roots of the subsets.

    Together, these classes implement the Kruskal's algorithm for finding the minimum spanning tree of a graph. The algorithm works by initializing a heap of edges, and repeatedly removing the edge with the highest priority from the heap and adding it to the minimum spanning tree, unless doing so would create a cycle. The Union-Find data structure is used to keep track of which vertices are already in the minimum spanning tree, and to check whether adding an edge would create a cycle. The algorithm terminates when all vertices are in the minimum spanning tree.
*/


// Import necessary libraries for I/O operations
import java.io.*;
import java.util.Scanner;

// Define the Edge class
class Edge { 
    // Declare instance variables
    public int u, v, wgt;

    // Define the default constructor for Edge
    public Edge() {
        // Initialize instance variables to default values
        u = 0;
        v = 0;
        wgt = 0;
    }

    // Define a parameterized constructor for Edge that takes three arguments
    public Edge(int x, int y, int w) {
        // Initialize instance variables with the passed arguments
        u = x;
        v = y;
        wgt = w;
    }

    // Define a method to print out an Edge object in a readable format
    public void show() {
        // Print out the Edge object in the format "Edge u--wgt--v"
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n");
    }

    // Define a method to convert a vertex integer into a character for pretty printing
    private char toChar(int u) {
        // Convert the integer value of the vertex into a corresponding ASCII character
        return (char) (u + 64);
    }
} 

// This class implements a heap data structure.
class Heap { 
    // The private fields 
	private int[] h; // An array to represent the heap
    int N, heapS; // The size of the heap
    Edge[] edge; // An array to store edges, where edge[v].wgt is the priority of v

    // Constructor for the heap that takes in the number of vertices and an array of edges.
    public Heap(int _N, Edge[] _edge) { 
        // Initialize the heap:
        int i;
        heapS = N = _N; // The heap size is equal to the number of vertices
        h = new int[N+1]; // Initialize the heap array
        edge = _edge; // Store the input edges in the class field
        
        // Fill the heap array with the indices of the edges array.
        for (i=0; i <= N; ++i) { 

            h[i] = i;
        }   
           
        // Convert the heap array into a proper heap using the siftDown operation.
        // We start from the bottom of the heap and work our way up.
        for(i = N / 2; i > 0; --i) { 

            siftDown(i);
        }
    }

    // This private method moves a node down the heap until it is in a position where it
    // is greater than or equal to its children (according to their weights).
    // The input parameter k is a position in the heap array h.
    private void siftDown(int k) {

        int e, j;
        e = h[k]; // Store the key of the node at position k.

        // While the node at position k has a left child.
        while (k <= N/2) { 
            // Set j to be the index of the left child of k.
            j = 2 * k; 

            // If the right child is smaller than the left child, move j to the right child.
            if ((j < N) && (edge[h[j]].wgt > edge[h[j+1]].wgt)) { 

                j++;
            } 

            // If the key of the child node is greater than or equal to the key of the current node,
            // we don't need to move anything else.
            if (edge[h[j]].wgt >= edge[e].wgt) { 

                break;
            } 

            // Otherwise, swap the positions of the current node and the child node.
            h[k] = h[j];
            k = j;

            // Update j to be the new left child of k.
            j = 2 * k;
        }

        // Place the key of the original node at its new position in the heap.
        h[k] = e;
    } 

    // This public method removes the edge with the highest priority from the heap,
    // and returns its value.
    public int remove() { 
        
        h[0] = h[1]; // top of heap moved to position 0
        h[1] = h[N--]; // last node of heap moved to top
        siftDown(1); // then sifted down
        return h[0]; // returns edge at top of heap 
    }
} 

// This class implements the Union-Find data structure, which is used to keep track of a partition of a set of elements into disjoint subsets. 
class UnionFindSets { 

    private int[] treeParent; // An array storing the parent of each vertex
    private int[] rank; // An array storing the rank of each vertex
    private int N; // The number of vertices in the Union-Find set

    // Constructor - Initializes the Union-Find data structure with a given number of vertices V.
    public UnionFindSets(int V) {

        N = V; // Set the number of vertices
        treeParent = new int[V + 1]; // Initialize the parent array
        rank = new int[V + 1]; // Initialize the rank array

        // Initialize each vertex as its own parent and with rank 0
        for (int i = 0; i <= V; i++) {

            treeParent[i] = i;
            rank[i] = 0;
        } 

    } 

    // FindSet - Returns the root vertex of the subset containing a given vertex.
    public int findSet(int vertex) { 
        // Traverse the parent array until the root vertex is found.
        while (vertex != treeParent[vertex]) { 

            vertex = treeParent[vertex];
        } 

        // Return the root vertex (i.e., the representative of the subset).
        return vertex;
    } 

   // Unifies two sets by setting the parent of one tree as the root of another
    public void union(int set1, int set2) {
        // Get the parent of set2 and set its parent to the parent of set1
        treeParent[findSet(set2)] = findSet(treeParent[set1]);
        System.out.print("Union: (" + toChar(set1) + "," + toChar(set2) + "): ");
    }

    // Unifies two sets based on rank
    public void unionByRank(int set1, int set2) {

        int u = findSet(set1); // Parent of set1
        int v = findSet(set2); // Parent of set2

        // If u has a lower rank than v, set u's parent to v
        // If v has a lower rank than u, set v's parent to u
        // If both have the same rank, set v's parent to u and increment the rank of u
        if (rank[u] < rank[v]) {
            treeParent[u] = v;
        } else if (rank[u] > rank[v]) {
            treeParent[v] = u;
        } else {
            treeParent[v] = u;
            rank[u]++;
        }
    }
    
    // Displays the trees in the Union Find data structure
    public void showTrees() { 
        // Start show trees operation
        int i;

        for(i = 1; i <= N; ++i) { 
            // For each vertex i, print its parent vertex in the tree
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  " );
        }
        System.out.print("\n");
    } 

    // Displays the sets in the Union Find data structure
    public void showSets() { 

        int u, root;
        int[] shown = new int[N+1];
        
        for (u = 1; u <= N; ++u) { 
            // For each vertex u, find its root and display its set if it hasn't been shown yet
            root = findSet(u);
            
            if(shown[root] != 1) { 

                showSet(root);
                shown[root] = 1;
            }
        } 
        System.out.print("\n");
    } 

    // Displays the set represented by the root vertex
    private void showSet(int root) { 
 
        int v;
        System.out.print("\nSet { ");

        for(v = 1; v <= N; ++v) { 
            // For each vertex v, print it if its parent vertex is the root
            if (findSet(v) == root) { 

                System.out.print(toChar(v) + " ");
            }
        } 
                
        System.out.print("}  ");
    } 

    // Display edge character
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}

// Graph class representing a weighted undirected graph.
class Graph {
    // V = number of vertices
    // E = number of edges
    // edge = array of all edges in the graph
    // mst = minimum spanning tree of the graph
    private int V, E;
    private Edge[] edge;
    private Edge[] mst;        
    
    // Constructor that reads the graph from a text file
    public Graph(String graphFile) throws IOException {
        
        int u, v, w, e;
        
        FileReader fr = new FileReader(graphFile);

        try (BufferedReader reader = new BufferedReader(fr)) {
            
            String splits = " +";  // multiple whitespace as delimiter
            String line = reader.readLine();
            String[] parts = line.split(splits);
            
            // Parse number of vertices and edges from the first line of the text file
            V = Integer.parseInt(parts[0]);
            E = Integer.parseInt(parts[1]);
            
            // Create the edge array
            edge = new Edge[E + 1];
            
            // Read each edge from the text file and create an Edge object for it
            System.out.println("\nReading Edges from Text File: \n");

            for (e = 1; e <= E; ++e) { 

                line = reader.readLine();
                parts = line.split(splits);
                u = Integer.parseInt(parts[0]);
                v = Integer.parseInt(parts[1]); 
                w = Integer.parseInt(parts[2]);
                
                System.out.println("Edge " + toChar(u) + "--(" + w + ")--" + toChar(v)); 
                
                // Create the edge object
                edge[e] = new Edge(u, v, w);
            } 
        } 
    } 

    // Computes the Minimum Spanning Tree (MST) of this graph using Kruskal's algorithm
    public Edge[] MST_Kruskal() { 

        int i = 0;
        Edge e;
        int uSet, vSet; // set1 and set2
        UnionFindSets partition;
        
        // Create an array to store the edges of the MST
        // Initially, it has no edges
        mst = new Edge[V - 1];

        // Create a heap for sorting the indices of the array of edges
        Heap h = new Heap(E, edge);

        // Create a partition of singleton sets for the vertices
        System.out.println("\nSets Before Kruskal's:");
        partition = new UnionFindSets(V);
        partition.showSets();
        System.out.println();

        while (i < V - 1) {
            // Removes the top edge from the heap
            e = h.edge[h.remove()];
            
            // Find the sets that contain the vertices of the edge
            uSet =  partition.findSet(e.u);
            vSet = partition.findSet(e.v);

            // If the vertices are in different sets, join them and add the edge to the MST
            if(uSet != vSet) {

                partition.unionByRank(uSet, vSet);
                System.out.print("Inserting Edge to MST: \n\n");
                e.show();
                mst[i++] = e;
                partition.showSets();
                System.out.println("\nTree of Vertices: \n");
                partition.showTrees();
                System.out.println();
            } 
        }
        
        System.out.println("Sets After Kruskal's: ");
        partition.showSets();

        // Return the array of edges that form the MST of this graph
        return mst;
    } 

    // This method converts the integer representation of a vertex into its corresponding character representation
    private char toChar(int u) {
        
        return (char)(u + 64);
    }

    // Displays the minimum spanning tree
    public void showMST() {
        // Start showMST operation
        int sum = 0;

        System.out.print("\nMinimum Spanning Tree Built from the Following Edges:\n\n");
        
        for(int e = 0; e < V - 1; ++e) {
            // Show each edge in the minimum spanning tree
            mst[e].show(); 
            sum += mst[e].wgt;
        } 

        // Show the total weight of the minimum spanning tree
        System.out.println();
        System.out.println("Weight of MST = " + sum);
        System.out.println();
    }
} 

// Driver code for Kruskal's algorithm on graphs
class KruskalTrees { 
    // Entry point of the program
    public static void main(String[] args) throws IOException {
        // Display a prompt to the user
        System.out.println("\n\nKruskal's Minimum Spanning Tree Algorithm\n");
        System.out.println("--------------------------------------------------------------------------------------");

        // Create a Scanner object to read input from the console
        Scanner sc = new Scanner(System.in);
        String fName = "";

        // Prompt user to enter filename until a valid filename is entered
        while (!fName.endsWith(".txt")) {

            System.out.print("\nEnter the name of the graph file in .txt extension: \n");
            fName = sc.nextLine();
            
            if (!fName.endsWith(".txt")) {

                System.out.println("Invalid filename! Please enter a filename with .txt extension.");
            }
        }

        // Create a Graph object using the specified file
        Graph g = new Graph(fName);

        // Compute the minimum spanning tree using Kruskal's algorithm
        g.MST_Kruskal();
        // Display the edges of the minimum spanning tree and its weight
        g.showMST();
        // Close the Scanner object
        sc.close();  
    } 
} 