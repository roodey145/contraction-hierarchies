package ch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Graph {
    int n, m;
    Graph contracted;

    public static class Vertex {
        float x, y;
        boolean contracted = false; // True means vertex is removed from graph
        int rank = 0;

        public Vertex(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void contract() {
            contracted = true;
        }

        public boolean isRemoved() {
            return contracted;
        }

        public void registerRank(int rank) {
            this.rank = rank;
        }

        public int getRank() {
            return rank;
        }
    }

    public class Edge {
        long to;
        int weight;
        long contracted; // only used by contraction hierachy, marks the vertex from which this edge resulted.

        public Edge(long to, int weight, long contracted) {
            this.to = to;
            this.weight = weight;
            this.contracted = contracted;
        }
    }

    private Map<Long, List<Edge>> edges;
    private Map<Long, Vertex> vertices;

    public Graph() {
        this.n = 0;
        this.m = 0;
        this.edges = new HashMap<>();
        this.vertices = new HashMap<>();
    }

    public void addVertex(long id, Vertex v) {
        this.vertices.put(id, v);
        this.n++;
    }

    public void addEdge(long from, long to, long contracted, int weight) {
        if (!this.edges.containsKey(from)) {
            this.edges.put(from, new ArrayList<>());
        }
        this.edges.get(from).add(new Edge(to, weight, contracted));
        this.m++;
    }

    public void addUndirectedEdge(long u, long v, long contracted, int weight) {
        addEdge(u, v, contracted, weight);
        addEdge(v, u, contracted, weight);
    }

    public void addUndirectedEdge(long u, long v, int weight) {
        addUndirectedEdge(u, v, -1, weight);
    }

    public List<Edge> getNeighbours(long u) {
        return this.edges.get(u);
    }

    public boolean exists(long u) {
        return this.edges.containsKey(u);
    }

    public Vertex getVertex(long id) {
        return this.vertices.get(id);
    }

    public int degree(long v) {
        return this.edges.get(v).size();
    }

    public int avgWeight(long v) {
        List<Edge> neighbours = getNeighbours(v);
        cleanse(neighbours);

        int avgWeight = 0;

        for(Edge e : neighbours) {
            avgWeight += e.weight;
        }

        return avgWeight / neighbours.size();
    }

    public int contract(long v, int order) throws Exception  {
        // if(contracted == null) contracted = new Graph();

        int added = 0;
        if (!exists(v)) {
            throw new Exception("Vertex does not exist!");
        }

        // Register the order in which the vertex has been contracted
        getVertex(v).registerRank(order);

        List<Edge> neighbours = getNeighbours(v);

        // Remove the edges connecting to removed vertices.
        Iterator<Edge> iter = neighbours.iterator();
        while(iter.hasNext()) {
            Edge edge = iter.next();
            if(getVertex(edge.to).isRemoved()) {
                iter.remove();
            }
        }

        long from;
        long to;
        int e1Weight;
        int e2Weight;

        for (int outer = 0; outer < neighbours.size() - 1; outer++) {
            from = neighbours.get(outer).to;
            e1Weight = neighbours.get(outer).weight;

            for (int inner = outer + 1; inner < neighbours.size(); inner++) {
                to = neighbours.get(inner).to;
                e2Weight = neighbours.get(inner).weight;

                // Check if there is another short path
                Result<Integer> result = BidirectionalDijkstra.shortestPathWeightLimited(this, from, to, e1Weight + e2Weight);

                if(/*result.result < 0 || */result.result >= e1Weight + e2Weight) {
                    // Add a shortcut
                    added++;
                    // contracted.addUndirectedEdge(from, to, v, e2Weight);
                    addUndirectedEdge(from, to, v, e2Weight);
                }
            }
        }

        return added - neighbours.size(); // Should probably return the rank
    }

    public int getEdgeDifference(long v) throws Exception  {
        int added = 0;
        if (!exists(v)) {
            throw new Exception("Vertex does not exist!");
        }

        List<Edge> neighbours = getNeighbours(v);

        // Remove the edges connecting to removed vertices.
        Iterator<Edge> iter = neighbours.iterator();
        while(iter.hasNext()) {
            Edge edge = iter.next();
            if(getVertex(edge.to).isRemoved()) {
                iter.remove();
            }
        }


        long from;
        long to;
        int e1Weight;
        int e2Weight;

        for (int outer = 0; outer < neighbours.size() - 1; outer++) {
            from = neighbours.get(outer).to;
            e1Weight = neighbours.get(outer).weight;

            for (int inner = outer + 1; inner < neighbours.size(); inner++) {
                to = neighbours.get(inner).to;
                e2Weight = neighbours.get(inner).weight;

                // Check if there is another short path
                Result<Integer> result = BidirectionalDijkstra.shortestPathWeightLimited(this, from, to, e1Weight + e2Weight);

                if(result.result < 0 || result.result == e1Weight + e2Weight) {
                    // Add a shortcut
                    added++;
                }
            }
        }

        // The lower the number is the better
        return added - neighbours.size();
    }

    
    public void cleanse(List<Edge> neighbours) {
        // Remove the edges connecting to removed vertices.
        Iterator<Edge> iter = neighbours.iterator();
        while(iter.hasNext()) {
            Edge edge = iter.next();
            if(getVertex(edge.to).isRemoved()) {
                iter.remove();
            }
        }
    }
}
