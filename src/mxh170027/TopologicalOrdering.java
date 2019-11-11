/**
 * Starter Code for Topological Ordering.
 *
 * @author Mihir Hindocha - mxh170027
 * @author Sai Krishna Reddy Syamala - sxs169430
 * @version 1.0: 10/28/2018
 * Implementation of Depth First Search (DFS) to find Topological Order.
 * <p>
 * Implement topologicalOrdering1() in the starter code.
 * This is the DFS-based algorithm for finding the topological ordering
 * of a directed acyclic graph.
 */

package mxh170027;

import rbk.Graph;
import rbk.Graph.Factory;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

import java.io.File;
import java.util.*;

public class TopologicalOrdering extends GraphAlgorithm<TopologicalOrdering.DFSVertex> {

    static Stack<Vertex> vertexStack = new Stack<>();

    /**
     * Class to store information about vertices during DFS.
     */
    public static class DFSVertex implements Factory {

        Vertex parent;
        boolean seen;

        /**
         * Constructor for DFSVertex initialises parent as null and seen as false.
         */
        public DFSVertex(Vertex u) {
            parent = null;
            seen = false;
        }

        public DFSVertex make(Vertex u) {
            return new DFSVertex(u);
        }
    }

    /**
     * Code to initialize storage for vertex properties is in GraphAlgorithm class
     *
     * @param g graph for which DFS is performed
     */
    public TopologicalOrdering(Graph g) {
        super(g, new DFSVertex(null));
    }

    /**
     * Main method to perform the depth first search on the graph.
     *
     * @param g graph on which the search is performed.
     * @return the object if type DFS using a helper method.
     */
    public static TopologicalOrdering depthFirstSearch(Graph g) {
        TopologicalOrdering d = new TopologicalOrdering(g);
        return d.depthFirstSearch();
    }

    /**
     * Helper method to traverse through the graph in depth first order.
     *
     * @return object of type DFS after traversal.
     */
    private TopologicalOrdering depthFirstSearch() {

        // Boolean variable to check if the graph is cyclic.
        boolean isCyclic = true;

        // Maintains number of vertices in graph.
        int vertexCount = 0;

        // Initialize seen and parent of all vertices and have a count.
        for (Vertex u : g) {
            vertexCount++;
            get(u).seen = false;
            get(u).parent = null;
            if (u.inDegree() == 0) {
                isCyclic = false;
            }
        }

        // Check each node and add them to the Stack vertexStack.
        if (!isCyclic) {
            for (Vertex u : g) {
                if (!get(u).seen && get(u) != null) {
                    dfsVisit(u);
                }
            }
        }

        //Check if the Stack Size = Number of vertices, then there are no cycles.
        if (vertexStack.size() == vertexCount) {
            return new TopologicalOrdering(g);
        } else {
            return null;
        }
    }

    /**
     * Method that checks each vertex and marks them as seen. It also updates the Stack of vertices vertexStack.
     *
     * @param u vertex to be checked
     */
    public void dfsVisit(Vertex u) {
        get(u).seen = true;

        //Traverse to the next element in the Adjacency  List.
        for (Edge e : g.outEdges(u)) {
            Vertex v = e.otherEnd(u);
            if (!get(v).seen) {
                get(v).parent = u;
                dfsVisit(v);
            }
        }
        vertexStack.push(u);
    }

    /**
     * Member function to find topological order of elements in the Graph.
     */
    public List<Vertex> topologicalOrder1() {

        //Checks if the graph is Undirected.
        if (!g.isDirected()) {
            return null;
        }
        TopologicalOrdering output = depthFirstSearch(g);

        if (output == null) {
            return null;
        }
        List<Vertex> finishList = new ArrayList<>();
        while (!output.vertexStack.isEmpty()) {
            finishList.add(output.vertexStack.pop());
        }
        return finishList;
    }

    /**
     * Helper function to print the list with the topological order.
     *
     * @param finishList the list containing topological order to be printed.
     */
    public static void printTopologicalOrder1(List<Vertex> finishList) {
        Iterator<Vertex> iterator = finishList.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
    }

    /**
     * Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
     *
     * @param g the input graph
     * @return object of type DFS with the topological ordering
     */
    public static List<Vertex> topologicalOrder1(Graph g) {
        TopologicalOrdering d = new TopologicalOrdering(g);
        return d.topologicalOrder1();
    }

    /**
     * The Drives Program.
     */
    public static void main(String[] args) throws Exception {
//      String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0"; //Directed Acyclic Graph with Cycles (No Output)
//      String string1 = "9 8   1 3 9   1 9 3   2 6 5   3 9 4   3 7 5   4 7 1   6 8 1   7 9 2 0"; //Undirected Graph example (No Output)
        String string = "7 6   1 2 3   2 5 5   2 6 4   2 4 5   4 7 1   3 7 1 0"; //Directed Acyclic Graph example (Topological Order Output)

        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readGraph(in);
        g.printGraph(false);

        List<Vertex> answer = topologicalOrder1(g);
        if (answer != null) {
            printTopologicalOrder1(answer);
        }
    }
}
