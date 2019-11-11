/**
 * Project Code for Topological Ordering modified.
 *
 * @author Mihir Hindocha - mxh170027
 * @author Sai Krishna Reddy Syamala - sxs169430
 * @version 1.0: 10/28/2018
 * Implementation of Depth First Search (DFS) to find Strongly Connected Components.
 * <p>
 * Implement the algorithm to find strongly connected components of a directed graph.
 * Add the method to your TopologicalOrdering class.  Make changes so that all methods share
 * as much of the code as possible.
 * <p>
 * public static StronglyConnectedComponents stronglyConnectedComponents(Graph g) { ... }
 */

package mxh170027;

import rbk.Graph;
import rbk.Graph.Factory;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Vertex;

import java.io.File;
import java.util.*;

public class StronglyConnectedComponents extends GraphAlgorithm<StronglyConnectedComponents.DFSVertex> {

    private static Stack<Vertex> vertexStack = new Stack<>();
    private static int componentNumber;

    /**
     * Class to store information about vertices during DFS.
     */
    public static class DFSVertex implements Factory {
        int cno;
        Vertex parent;
        boolean seen;

        /**
         * Constructor for DFSVertex initialises parent as null and seen as false.
         */
        public DFSVertex(Vertex u) {

            // Stores the component number for the component of graph it belongs to.
            cno = 0;
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
    public StronglyConnectedComponents(Graph g) {
        super(g, new DFSVertex(null));
    }

    /**
     * Main method to perform the depth first search on the graph.
     *
     * @param g graph on which the search is performed.
     * @return the object if type DFS using a helper method.
     */
    public static StronglyConnectedComponents depthFirstSearch(Graph g) {
        StronglyConnectedComponents d = new StronglyConnectedComponents(g);
        return d.depthFirstSearch();
    }

    /**
     * Helper method to traverse through the graph in depth first order.
     *
     * @return object of type DFS after traversal.
     */
    private StronglyConnectedComponents depthFirstSearch() {

        // Maintains number of vertices in graph.
        int vertexCount = g.size();

        dfs(g);

        // Check if the Stack Size = Number of vertices, then there are no cycles.
        if (vertexStack.size() == vertexCount) {
            return new StronglyConnectedComponents(g);
        } else {
            return null;
        }
    }

    /**
     * Common Helper method created to perform DFS on any iterable data structure like graph, list, etc.
     *
     * @param iterable object of data structure that can implement iterable
     */
    public void dfs(Iterable<Vertex> iterable) {

        componentNumber = 0;
        for (Vertex u : iterable) {
            get(u).cno = 0;
            get(u).seen = false;
            get(u).parent = null;
        }

        for (Vertex u : iterable) {
            if (!get(u).seen) {
                componentNumber++;
                dfsVisit(u);
            }
        }
    }

    /**
     * Method that checks each vertex and marks them as seen. It also updates the Stack of vertices vertexStack.
     *
     * @param u vertex to be checked
     */
    public void dfsVisit(Vertex u) {
        get(u).seen = true;
        get(u).cno = componentNumber;

        // Traverse to the next element in the Adjacency  List.
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

        if (!checkDirected(g)) {
            return null;
        }
        StronglyConnectedComponents output = depthFirstSearch(g);

        if (output == null) {
            return null;
        }
        List<Vertex> finishList = output.createFinishlist();
        return finishList;
    }

    /**
     * Helper method to create a FinishList from the Stack of vertices preserving the order in which nodes were visited.
     *
     * @return a list of all vertices visited in descending order.
     */
    public List<Vertex> createFinishlist() {
        List<Vertex> finishList = new LinkedList<>();
        while (!this.vertexStack.isEmpty()) {
            finishList.add(this.vertexStack.pop());
        }
        return finishList;
    }

    /**
     * Helper method to check if the input graph is directed or not.
     *
     * @param g the graph needed to be checked
     * @return true if graph is directed, false otherwise.
     */
    public static boolean checkDirected(Graph g) {

        // Checks if the graph is Undirected.
        return g.isDirected();
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
        StronglyConnectedComponents d = new StronglyConnectedComponents(g);
        return d.topologicalOrder1();
    }

    /**
     * Method defined to find the Strongly Connected Components in the given graph.
     *
     * @param g input graph to be checked
     * @return dfs object with the component numbers (cno) set.
     */
    public static StronglyConnectedComponents stronglyConnectedComponents(Graph g) {
        StronglyConnectedComponents d = new StronglyConnectedComponents(g);
        d.dfs(g);
        List<Vertex> list = d.createFinishlist();
        g.reverseGraph();
        d.dfs(list);
        g.reverseGraph();
        return d;
    }

    /**
     * Helper method created to print the Component number along with the vertices of the graph in that component.
     */
    public void printVertices() {
        HashMap<Integer, List<Vertex>> output = new HashMap<>();

        for (Vertex u : g) {
            // If given cno is already in the output.
            if (!output.containsKey(get(u).cno)) {
                List<Vertex> vertexList = new LinkedList<>();
                vertexList.add(u);
                output.put(get(u).cno, vertexList);
            } else {
                List<Vertex> vertexList = output.get(get(u).cno);
                vertexList.add(u);
                output.replace(get(u).cno, vertexList);
            }
        }
        for (Map.Entry<Integer, List<Vertex>> entry : output.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    /**
     * The Driver Program.
     */
    public static void main(String[] args) throws Exception {
        String string = "11 17    1 11 2  2 3 4   2 7 8   3 10 5  4 1 3   4 9 8   5 4 2   5 7 9   5 8 10  6 3 4   7 8 6   8 2 3   9 11 3  10 6 5  11 4 6  11 3 4  11 3 5 0";
//        (Example covered in class... Directed Graph with Cycles)

//        String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 7   6 7 1   7 6 1 0"; //Directed Graph with Cycles (Example from SP8)


        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        Graph g = Graph.readGraph(in);
        g.printGraph(false);

        StronglyConnectedComponents d = stronglyConnectedComponents(g); // Calling the function to calculate SCC on graph g.
        System.out.println("Total number of SCC: " + componentNumber);
        d.printVertices(); // Print the SCC Component with vertices within it.

    }
}
