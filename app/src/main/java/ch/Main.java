package ch;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.PriorityQueue;
import java.util.Scanner;

import ch.Graph.Vertex;

class Main {
    public static final String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
    private static long[] ids;

    public static Graph readGraph(Scanner sc, boolean chGraph) {
        int n = sc.nextInt();
        int m = sc.nextInt();

        Graph g = new Graph();

        long id;
        float x, y;
        int rank;
        ids = new long[n];
        Vertex vertex;
        // int step = 1000;

        for (int i = 0; i < n; i++) {
            id = sc.nextLong();

            ids[i] = id;
            x = Float.parseFloat(sc.next());
            y = Float.parseFloat(sc.next());
            vertex = new Graph.Vertex(x, y);

            if (chGraph) {
                rank = Integer.parseInt(sc.next());
                vertex.registerRank(rank);
            }

            g.addVertex(id, vertex);
            // if (i % step == 0) {
            // System.out.println((i / step) + " of " + (n / step));
            // }
        }

        long from, to, contracted;
        int weight;

        for (int i = 0; i < m; i++) {
            from = sc.nextLong();
            to = sc.nextLong();
            weight = sc.nextInt();

            if (chGraph) {
                contracted = sc.nextLong();
                g.addEdge(from, to, contracted, weight);
            } else {
                g.addUndirectedEdge(from, to, weight);
            }

            // if (i % step == 0) {
            // System.out.println((i / step) + " of " + (m / step));
            // }
        }

        return g;
    }

    public static long[] getIds() {
        return ids;
    }

    public static Graph getGraph(String fileName, boolean contractedGraph) throws FileNotFoundException {
        File file = new File( currentPath + "/" + fileName + ".graph" );
        System.out.println(currentPath + "/" + fileName + ".graph");
        Scanner sc = new Scanner(file);
        Graph graph = Main.readGraph(sc, contractedGraph);
        sc.close();
        System.out.println(graph.n + " " + graph.m);

        return graph;
    }

    public static void main(String[] args) throws Exception {

        /////////////////////////////////////////////////////////////////////
        // Preprocess the graph and store it in file new-contracted.graph //
        ///////////////////////////////////////////////////////////////////
        /// Uncomment the lines below and run the method
        // Graph graph = Main.getGraph("denmark", false);
        // ContractionHierachy ch = new ContractionHierachy();
        // ch.storeGraph(graph);
        // ch.preprocess();
        // System.out.println("Done PreProcessing");
        // graph.storeGraph("new-contracted");

        /////////////////////////////////////////////////////////////////////////
        // Run dijkstra and bidirectiona dijkstra using 115724 and 4214353078 //
        ///////////////////////////////////////////////////////////////////////
        /// This will print out number of relaxed edges and time
        /// Uncomment the line below. The algorithms runs on denmark.graph
        // Tests.TestNonContractedDenmarkGraph();

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // Run dijkstra and bidirectional dijkstra and Contraction Hierarchy using 115724 and 4214353078 //
        //////////////////////////////////////////////////////////////////////////////////////////////////
        /// This will print out number of relaxed edges and time
        /// Uncomment the line below. The algorithms runs on contracted16.graph
        // Tests.TestContractedDenmarkGraph();

        ////////////////////////////////////////////////////////////////////////////////
        // Run dijkstra and bidirectional on denmark graph using 1000 vertices pairs //
        //////////////////////////////////////////////////////////////////////////////
        /// You need to have the folder csv as a subfolder of contraction-hierarchies folder
        /// Running the below method will result in a .csv file containing time, vertices
        /// and contracted edges for the different algorithms. The name of the file is
        /// the same as the number of pairs of vertices, in this case nonContracted-1000.csv
        /// Uncomment the line below. The algorithms runs on denmark graph.
        // Tests.repeatedTestOnNonContractedGraph(1000, 50);
        

        //////////////////////////////////////////////////////////////////////////////////////
        // Run all the shortest path algorithms on denmark graph using 1000 vertices pairs //
        ////////////////////////////////////////////////////////////////////////////////////
        /// You need to have the folder csv as a subfolder of contraction-hierarchies folder
        /// Running the below method will result in a .csv file containing time, vertices
        /// and contracted edges for the different algorithms. The name of the file is
        /// the same as the number of pairs of vertices, in this case contracted16-1000.csv
        /// Uncomment the line below. The algorithms runs on contracted16 graph.
        // Tests.repeatedTestOnContractedGraph(1000, 50);
    }
}