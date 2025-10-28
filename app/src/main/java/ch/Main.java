package ch;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

class Main {
    private static long[] ids;

    private static Graph readGraph(Scanner sc) {
        int n = sc.nextInt();
        int m = sc.nextInt();

        Graph g = new Graph();

        long id;
        float x, y;
        ids = new long[n];
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


        int seed = 5;
        Random rand = new Random(seed);

        int nrOfParis = 1000;
        int[][] pairs = new int[nrOfParis][];

        for(int i = 0; i < nrOfParis; i++) {
            int from = rand.nextInt(ids.length);
            int to;

            do { 
                to = rand.nextInt(ids.length);
            } while (to == from);

            pairs[i] = new int[]{
                from, to
            };
        }
        
        int uniRelaxed = 0;
        long uniTime = 0;
        int uniResult = 0;

        int biRelaxed = 0;
        long biTime = 0;
        int biResult = 0;

        for (int i = 0; i < pairs.length; i++) {
            long from = ids[pairs[i][0]];
            long to = ids[pairs[i][1]];
            result =  Dijkstra.shortestPath(graph, from, to);
            uniRelaxed += result.relaxed;
            uniTime += result.time / 1000;
            uniResult = result.result;

            result = BidirectionalDijkstra.shortestPath(graph, from, to);
            biRelaxed += result.relaxed;
            biTime += result.time / 1000;
            biResult = result.result;

            if(uniResult != biResult) {
                System.out.println("Incorrect Result - Bi: " + biResult + ", Uni: " + uniResult);
            }
            System.out.println("I: " + i + " - (UniTime: " + (uniTime / (i + 1)) + ", UniRelaxed: " + (uniRelaxed / (i + 1))
                                         + " (BiTime: " + (biTime / (i + 1)) + ", BiRelaxed: " + (biRelaxed / (i + 1)));
        }

        System.out.println("Avg Uni Relaxed: " + (uniRelaxed / 1000) + ", Avg Uni Time: " + (uniTime / 1000));
        System.out.println("Avg Bi Relaxed: " + (biRelaxed / 1000) + ", Avg Bi Time: " + (biTime / 1000));


    }
}