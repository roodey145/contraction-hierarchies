package ch;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ContractionHierachyTest {
    private Graph g;
    @Before
    public void setUp() {
        g = new Graph();
        for(int i = 0; i < 6; i++){
            g.addVertex(i, new   Graph.Vertex(i, i)); 
        }

        // v: 0
        
        // u1: 1
        // u2: 2

        // w1: 3
        // w2: 4
        // w3: 5
        
        // Connect v to all vertices
        g.addUndirectedEdge(0, 1, 1); // v -> u1 && u1 -> v
        g.addUndirectedEdge(0, 2, 1); // v -> u2 && u2 -> v

        g.addUndirectedEdge(0, 3, 2); // v -> w1 && w1 -> v
        g.addUndirectedEdge(0, 4, 3); // v -> w2 && w2 -> v
        g.addUndirectedEdge(0, 5, 4); // v -> w3 && w3 -> v

        // Connect w1
        g.addUndirectedEdge(3, 1, 3); // w1 -> u1 && u1 -> w1
        g.addUndirectedEdge(3, 4, 1); // w1 -> w2 && w2 -> w1

        // Connect w2
        g.addUndirectedEdge(4, 5, 1); // w2 -> w3 && w3 -> w2


    }

    @Test
    public void edgeDifferenceTest1() throws Exception {
        g = new Graph();
        g.addVertex(1, new   Graph.Vertex(0, 0));
        g.addVertex(2, new Graph.Vertex(1, 1));
        g.addVertex(3, new Graph.Vertex(2, 2));
        g.addVertex(4, new Graph.Vertex(3, 3));

        // Graph structure:
        // 1 --(4)-- 2 --(1)-- 3
        //  \                /
        //   (8)          (2)
        //     \          /
        //          4
        g.addUndirectedEdge(1, 2, 4);
        g.addUndirectedEdge(2, 3, 1);
        g.addUndirectedEdge(1, 4, 8);
        g.addUndirectedEdge(3, 4, 2);


        int edgeDifference = g.getEdgeDifference(1l);

        assertEquals(-2, edgeDifference);
    }


    @Test
    public void edgeDifferenceTest2() throws Exception {
        int edgeDifference = g.getEdgeDifference(0l);
        System.out.println("Edge Difference: " + edgeDifference);
        assertEquals(2, edgeDifference);
    }


    @Test
    public void edgeDifferenceTest3() throws Exception {
        int edgeDifference = g.getEdgeDifference(1l);
        System.out.println("Edge Difference: " + edgeDifference);
        assertEquals(-2, edgeDifference);
    }
}
