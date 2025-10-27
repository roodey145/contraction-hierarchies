package ch;

import java.io.File;
import java.util.Scanner;

class Main {

    private static Graph readGraph(Scanner sc) {
        int n = sc.nextInt();
        int m = sc.nextInt();

        Graph g = new Graph();

        long id;
        float x, y;
        long[] ids = new long[n];
        // int step = 1000;

        for (int i = 0; i < n; i++) {
            id = sc.nextLong();

            ids[i] = id;
            x = Float.parseFloat(sc.next());
            y = Float.parseFloat(sc.next());

            g.addVertex(id, new Graph.Vertex(x, y));
            // if (i % step == 0) {
            //     System.out.println((i / step) + " of " + (n / step));
            // }
        }

        long from, to;
        int weight;

        for (int i = 0; i < m; i++) {
            from = sc.nextLong();
            to = sc.nextLong();
            weight = sc.nextInt();
            g.addUndirectedEdge(from, to, weight);

            // if (i % step == 0) {
            //     System.out.println((i / step) + " of " + (m / step));
            // }
        }

        return g;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\abdu2\\Desktop\\ITU\\semester3\\Applied Algorithm\\contraction-hierarchies\\denmark.graph");

        Scanner sc = new Scanner(file);
        Graph graph = readGraph(sc);
        sc.close();
        System.out.println(graph.n + " " + graph.m);

        Result<Integer> result =  Dijkstra.shortestPath(graph, 115724, 4214353078l);
        System.out.println("Expected: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result = BidirectionalDijkstra.shortestPath(graph, 115724, 4214353078l);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));

    }
}