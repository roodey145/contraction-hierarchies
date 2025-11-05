package ch;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

import ch.Graph.Vertex;

class Main {
    private static long[] ids;

    private static Graph readGraph(Scanner sc, boolean chGraph) {
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

    public static void main(String[] args) throws Exception {
        // File file = new File("C:\\Users\\abdu2\\Desktop\\ITU\\semester3\\Applied Algorithm\\contraction-hierarchies\\denmark.graph");
        // File file = new File("C:\\Users\\abdu2\\Desktop\\ITU\\semester3\\Applied Algorithm\\contraction-hierarchies\\contracted15.graph");
        File file = new File("C:\\Users\\abdu2\\Documents\\GitHub\\contraction-hierarchies\\contracted15.graph");
        
        Scanner sc = new Scanner(file);
        Graph graph = readGraph(sc, true);
        sc.close();
        System.out.println(graph.n + " " + graph.m);

        
        // ContractionHierachy ch = new ContractionHierachy();
        // ch.storeGraph(graph);
        // ch.preprocess();
        // System.out.println("Done PreProcessing");
        // graph.storeGraph("contracted15");
        
        // if(true) return;
        
        Result<Integer> result = BidirectionalDijkstra.shortestPathPriority(graph, 115724, 4214353078l);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result =  Dijkstra.shortestPath(graph, 115724, 4214353078l);
        System.out.println("Expected: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result = BidirectionalDijkstra.shortestPath2(graph, 115724, 4214353078l);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));

        if(true) return;

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

        int chRelaxed = 0;
        long chTime = 0;
        int chResult = 0;

        int biRelaxed2 = 0;
        long biTime2 = 0;
        int biResult2 = 0;

        for (int i = 0; i < pairs.length; i++) {
            long from = ids[pairs[i][0]];
            long to = ids[pairs[i][1]];
            result =  Dijkstra.shortestPath(graph, from, to);
            uniRelaxed += result.relaxed;
            uniTime += result.time / 1000;
            uniResult = result.result;

            result = BidirectionalDijkstra.shortestPathPriority(graph, from, to);
            chRelaxed += result.relaxed;
            chTime += result.time / 1000;
            chResult = result.result;


            // result = BidirectionalDijkstra.shortestPath2(graph, from, to);
            // biRelaxed2 += result.relaxed;
            // biTime2 += result.time / 1000;
            // biResult2 = result.result;

            // System.out.println("Uni Result: " + uniResult);

            if(uniResult != chResult /*|| uniResult != biResult2*/) {
                System.out.println("Incorrect Result - CH: " + chResult + ", Bi2: " + biResult2 + ", Uni: " + uniResult);
                break;
            }
            System.out.println("I: " + i + " - (UniTime: " + (uniTime / (i + 1)) + ", UniRelaxed: " + (uniRelaxed / (i + 1))
                                         + " (CHTime: " + (chTime / (i + 1)) + ", CHRelaxed: " + (chRelaxed / (i + 1))
                                        //  + " (BiTime2: " + (biTime2 / (i + 1)) + ", BiRelaxed2: " + (biRelaxed2 / (i + 1)) + ")"
                                         );
        }

        System.out.println("Avg Uni Relaxed: " + (uniRelaxed / 1000) + ", Avg Uni Time: " + (uniTime / 1000));
        // System.out.println("Avg Bi Relaxed: " + (biRelaxed2 / 1000) + ", Avg Bi Time: " + (biTime2 / 1000));
        System.out.println("Avg CH Relaxed: " + (chRelaxed / 1000) + ", Avg CH Time: " + (chTime / 1000));
    }
}