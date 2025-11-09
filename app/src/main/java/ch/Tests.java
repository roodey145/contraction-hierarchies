package ch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
public class Tests {

    public static void TestNonContractedDenmarkGraph() throws FileNotFoundException {
        Graph graph = Main.getGraph("denmark", false);

        long from = 115724;
        long to = 4214353078l;

        Result<Integer> result = Dijkstra.shortestPath(graph, from, to);
        System.out.println("Expected: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result = BidirectionalDijkstra.shortestPath2(graph, from, to);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        System.out.println("============================\n\n");
    }

    public static void TestContractedDenmarkGraph() throws FileNotFoundException {
        Graph graph = Main.getGraph("contracted16", true);

        long from = 115724;
        long to = 4214353078l;

        Result<Integer> result = Dijkstra.shortestPath(graph, from, to);
        System.out.println("Expected: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result = BidirectionalDijkstra.shortestPathPriority(graph, from, to);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        result = BidirectionalDijkstra.shortestPath2(graph, from, to);
        System.out.println("Actual: " + result.result + ", Visited: " + (result.relaxed) + ", Time: " + (result.time / 1000));
        System.out.println("============================\n\n");
    }

    public static void repeatedTestOnNonContractedGraph(int reptation, int seed) throws FileNotFoundException, UnsupportedEncodingException {
        Graph graph = Main.getGraph("denmark", false);
        long[] ids = Main.getIds();

        Random rand = new Random(seed);

        // int nrOfParis = reptation;
        int[][] pairs = new int[reptation][];

        for (int i = 0; i < reptation; i++) {
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
        int uniResult;

        int biRelaxed2 = 0;
        long biTime2 = 0;
        int biResult2;

        Result<Integer> resultDijk;
        Result<Integer> resultBi;

        Logger logger = new Logger(Main.currentPath + "/csv/nonContracted-" + reptation);
        // Print the header
        logger.println("I, From, To, Dijkstra Time, Dijkstra Relaxed, Bidirectional Dijkstra Time, Bidirectional Dijkstra Relaxed");

        for (int i = 0; i < pairs.length; i++) {
            long from = ids[pairs[i][0]];
            long to = ids[pairs[i][1]];

            resultDijk = Dijkstra.shortestPath(graph, from, to);
            // System.out.println(result.time);
            uniRelaxed += resultDijk.relaxed / reptation;
            uniTime += resultDijk.time / 1000;
            uniResult = resultDijk.result;

            resultBi = BidirectionalDijkstra.shortestPath2(graph, from, to);
            // System.out.println(result.time);
            biRelaxed2 += resultBi.relaxed / reptation;
            biTime2 += resultBi.time / 1000;
            biResult2 = resultBi.result;

            // System.out.println("Uni Result: " + uniResult);
            if (uniResult != biResult2) {
                System.out.println("Incorrect Result - Bi2: " + biResult2 + ", Uni: " + uniResult);
                break;
            }
            System.out.println("I: " + i + " - (UniTime: " + (uniTime / (i + 1)) + ", UniRelaxed: " + (uniRelaxed) + ")"
                    + " (BiTime: " + (biTime2 / (i + 1)) + ", BiRelaxed: " + (biRelaxed2) + ")");
            logger.println(i + "," + from + "," + to + "," 
                                + resultDijk.time + "," + resultDijk.relaxed + "," 
                                + resultBi.time + "," + resultBi.relaxed);
        }

        System.out.println("Avg Uni Relaxed: " + (uniRelaxed) + ", Avg Uni Time: " + (uniTime / reptation));
        System.out.println("Avg Bi Relaxed: " + (biRelaxed2) + ", Avg Bi Time: " + (biTime2 / reptation));

        logger.close();
    }

    public static void repeatedTestOnContractedGraph(int reptation, int seed) throws FileNotFoundException, UnsupportedEncodingException {
        Graph graph = Main.getGraph("contracted16", true);
        long[] ids = Main.getIds();

        Random rand = new Random(seed);

        // int nrOfParis = reptation;
        int[][] pairs = new int[reptation][];

        for (int i = 0; i < reptation; i++) {
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
        int uniResult;

        int chRelaxed = 0;
        long chTime = 0;
        int chResult;

        int biRelaxed2 = 0;
        long biTime2 = 0;
        int biResult2;

        Result<Integer> resultDijk;
        Result<Integer> resultBi;
        Result<Integer> resultCH;

        Logger logger = new Logger(Main.currentPath + "/csv/contracted-" + reptation);
        // Print the header
        logger.println("I,From,To,Dijkstra Time,Dijkstra Relaxed,Bidirectional Dijkstra Time,Bidirectional Dijkstra Relaxed,Contraction Hierarchy Time,Contraction Hierarchy Relaxed");

        for (int i = 0; i < pairs.length; i++) {
            long from = ids[pairs[i][0]];
            long to = ids[pairs[i][1]];

            resultDijk = Dijkstra.shortestPath(graph, from, to);
            // System.out.println(result.time);
            uniRelaxed += resultDijk.relaxed / reptation;
            uniTime += resultDijk.time / 1000;
            uniResult = resultDijk.result;

            resultCH = BidirectionalDijkstra.shortestPathPriority(graph, from, to);
            // System.out.println(result.time);
            chRelaxed += resultCH.relaxed / reptation;
            chTime += resultCH.time / 1000;
            chResult = resultCH.result;

            resultBi = BidirectionalDijkstra.shortestPath2(graph, from, to);
            // System.out.println(result.time);
            biRelaxed2 += resultBi.relaxed / reptation;
            biTime2 += resultBi.time / 1000;
            biResult2 = resultBi.result;

            // System.out.println("Uni Result: " + uniResult);
            if (uniResult != chResult || uniResult != biResult2) {
                System.out.println("Incorrect Result - CH: " + chResult + ", Bi2: " + biResult2 + ", Uni: " + uniResult);
                break;
            }
            System.out.println("I: " + i + " - (UniTime: " + (uniTime / (i + 1)) + ", UniRelaxed: " + (uniRelaxed) + ")"
                    + " (CHTime: " + (chTime / (i + 1)) + ", CHRelaxed: " + (chRelaxed) + ")"
                    + " (BiTime: " + (biTime2 / (i + 1)) + ", BiRelaxed: " + (biRelaxed2) + ")"
            );
            logger.println(i + "," + from + "," + to + "," 
                                + resultDijk.time + "," + resultDijk.relaxed + "," 
                                + resultBi.time + "," + resultBi.relaxed + ","
                                + resultCH.time + "," + resultCH.relaxed);
        }

        System.out.println("Avg Uni Relaxed: " + (uniRelaxed) + ", Avg Uni Time: " + (uniTime / reptation));
        System.out.println("Avg Bi Relaxed: " + (biRelaxed2) + ", Avg Bi Time: " + (biTime2 / reptation));
        System.out.println("Avg CH Relaxed: " + (chRelaxed) + ", Avg CH Time: " + (chTime / reptation));
        logger.close();
    }
}
