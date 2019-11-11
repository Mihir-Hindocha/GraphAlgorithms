/**
 * @author Mihir Hindocha - mxh170027
 * @author Prit Thakkar - pvt170000
 * @version 1.0: 11/25/2018
 * <p>
 * Finding Diameter of a tree.
 * <p>
 * Implement the algorithm to find the diameter of a tree using the algorithm
 * discussed in class, that runs BFS twice. Code this algorithm without
 * modifying Graph.java and BFSOO.java, using them from package rbk.
 */

package mxh170027;

import rbk.BFSOO;
import rbk.Graph;

import java.io.File;
import java.util.Scanner;

public class DiameterOfTree {

    /**
     * Method created to find the diameter of the graph. It uses the findFarthest method.
     *
     * @param g input graph
     * @return diameter of the input graph as int
     */
    public static int diameter(Graph g) {

        // If graph is empty
        if (g.size() == 0) return 0;

        // BFS run on graph from any node. Here node 1.
        BFSOO b = new BFSOO(g);
        b.bfs(g.getVertex(1));

        // Finding the farthest node.
        Graph.Vertex farthest = findFarthestNode(g, b);

        // Running BFS on farthest node for Diameter.
        b.bfs(farthest);

        return b.getDistance(findFarthestNode(g, b));
    }

    /**
     * Helper method to find the farthest element in the graph.
     *
     * @param g input graph
     * @param b BFSOO object: used for source
     * @return vertex with the maximum distance from source
     */
    private static Graph.Vertex findFarthestNode(Graph g, BFSOO b) {

        int max = 0;
        int dist;
        Graph.Vertex farthest = b.getSource();

        for (Graph.Vertex u : g) {
            dist = b.getDistance(u);
            if (max < dist) {
                max = dist;
                farthest = u;
            }
        }
        return farthest;
    }

    public static void main(String[] args) throws Exception {
        //String string = "7 8   1 2 2   1 3 3   2 4 5   3 4 4   4 5 1   5 1 -7 -1 1";
        String string = "10 9   1 2 2   1 3 3   2 4 5   2 5 4   3 6 1   3 7 1   4 8 1   7 9 1   7 10 1 1";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);
        // Read graph from input
        Graph g = Graph.readGraph(in);
        int s = in.nextInt();

        g.printGraph(false);

        int diam = diameter(g);
        System.out.println(diam);
    }
}
