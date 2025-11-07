package ch;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class Tests {
    public static void TestNonContractedDenmarkGraph() throws FileNotFoundException {
        Graph graph = Main.getGraph("denmark", false);

        long from = 115724;
        long to = 4214353078l;

        Result<Integer> result =  Dijkstra.shortestPath(graph, from, to);
        System.out.println("Expected: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result = BidirectionalDijkstra.shortestPath2(graph, from, to);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        System.out.println("============================\n\n");
    }


    public static void TestContractedDenmarkGraph() throws FileNotFoundException {
        Graph graph = Main.getGraph("contracted15", true);

        long from = 115724;
        long to = 4214353078l;

        Result<Integer> result =  Dijkstra.shortestPath(graph, from, to);
        System.out.println("Expected: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result = BidirectionalDijkstra.shortestPathPriority(graph, from, to);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result = BidirectionalDijkstra.shortestPath2(graph, from, to);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        System.out.println("============================\n\n");
    }


    public static void repeatedTest(int reptation) throws FileNotFoundException {
        Graph graph = Main.getGraph("contracted15", true);
        long[] ids = Main.getIds();

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

        Result<Integer> result;

        for (int i = 0; i < pairs.length; i++) {
            long from = ids[pairs[i][0]];
            long to = ids[pairs[i][1]];

            result =  Dijkstra.shortestPath(graph, from, to);
            uniRelaxed += result.relaxed / 1000;
            uniTime += result.time / 1000;
            uniResult = result.result;

            result = BidirectionalDijkstra.shortestPathPriority(graph, from, to);
            chRelaxed += result.relaxed / 1000;
            chTime += result.time / 1000;
            chResult = result.result;


            result = BidirectionalDijkstra.shortestPath2(graph, from, to);
            biRelaxed2 += result.relaxed / 1000;
            biTime2 += result.time / 1000;
            biResult2 = result.result;

            // System.out.println("Uni Result: " + uniResult);

            if(uniResult != chResult /*|| uniResult != biResult2*/) {
                System.out.println("Incorrect Result - CH: " + chResult + ", Bi2: " + biResult2 + ", Uni: " + uniResult);
                break;
            }
            System.out.println("I: " + i + " - (UniTime: " + (uniTime / (i + 1)) + ", UniRelaxed: " + (uniRelaxed) + ")"
                                         + " (CHTime: " + (chTime / (i + 1)) + ", CHRelaxed: " + (chRelaxed) + ")"
                                         + " (BiTime: " + (biTime2 / (i + 1)) + ", BiRelaxed: " + (biRelaxed2) + ")"
                                         );
        }

        System.out.println("Avg Uni Relaxed: " + (uniRelaxed) + ", Avg Uni Time: " + (uniTime / 1000));
        System.out.println("Avg Bi Relaxed: " + (biRelaxed2) + ", Avg Bi Time: " + (biTime2 / 1000));
        System.out.println("Avg CH Relaxed: " + (chRelaxed) + ", Avg CH Time: " + (chTime / 1000));
    }
}
