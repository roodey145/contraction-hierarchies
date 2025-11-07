package ch;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

import ch.Graph.Vertex;

class Main {
    private static final String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
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
                g.addUndirectedEdge(from, to, contracted, weight);
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
        Scanner sc = new Scanner(file);
        Graph graph = Main.readGraph(sc, contractedGraph);
        sc.close();
        System.out.println(graph.n + " " + graph.m);

        return graph;
    }

    public static void main(String[] args) throws Exception {
        // File file = new File("C:\\Users\\abdu2\\Desktop\\ITU\\semester3\\Applied Algorithm\\contraction-hierarchies\\denmark.graph");
        // File file = new File("C:\\Users\\abdu2\\Documents\\GitHub\\contraction-hierarchies\\contracted15.graph");
        
        Tests.TestNonContractedDenmarkGraph();
        Tests.TestContractedDenmarkGraph();
        
        Tests.repeatedTest(1000);
    }
}