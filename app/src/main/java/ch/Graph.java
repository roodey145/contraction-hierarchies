package ch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        if (edges.size() > 10_000_000 || vertices.size() > 10_000_000) {
            System.out.println("M: " + this.m + ", N: " + this.n);
        }
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
        neighbours = cleanse(neighbours);

        int avgWeight = 0;

        for (Edge e : neighbours) {
            avgWeight += e.weight;
        }

        return avgWeight / (neighbours.isEmpty() ? 1 : neighbours.size());
    }

    public int contract(long v, int order) throws Exception {
        // if(contracted == null) contracted = new Graph();

        int added = 0;
        if (!exists(v)) {
            throw new Exception("Vertex does not exist!");
        }

        // Register the order in which the vertex has been contracted
        Vertex vertex = getVertex(v);
        vertex.registerRank(order);
        vertex.contract();

        List<Edge> neighbours = getNeighbours(v);
        // if (neighbours.size() > 1000) {
        //     System.out.println("Neighbours: " + neighbours);
        // }

        // Remove the edges connecting to removed vertices.
        neighbours = cleanse(neighbours);
        // Iterator<Edge> iter = neighbours.iterator();
        // while (iter.hasNext()) {
        //     Edge edge = iter.next();
        //     if (getVertex(edge.to).isRemoved()) {
        //         iter.remove();
        //     }
        // }
        int size = neighbours.size();
        int avgWeight = avgWeight(v);

        long from;
        long to;
        int e1Weight;
        int e2Weight;
        Result<Integer> result;

        // int iteration = 0;
        for (int outer = 0; outer < /*neighbours.size()*/ size - 1; outer++) {
            from = neighbours.get(outer).to;
            e1Weight = neighbours.get(outer).weight;

            for (int inner = outer + 1; inner < /*neighbours.size()*/ size; inner++) {
                to = neighbours.get(inner).to;
                e2Weight = neighbours.get(inner).weight;
                // iteration++;

                // if(neighbours.size() > size) {
                //     break; // To many edges
                // }
                // Check if there is another short path
                // result = BidirectionalDijkstra.shortestPathWeightLimited(this, from, to, e1Weight + e2Weight);
                result = BidirectionalDijkstra.shortestPathEdgesLimited(this, from, to, 15);

                if (/*result.result < 0 || */result.result >= e1Weight + e2Weight) {
                    // Add a shortcut
                    added++;
                    // contracted.addUndirectedEdge(from, to, v, e2Weight);
                    addUndirectedEdge(from, to, v, e1Weight + e2Weight);
                }
            }
        }

        return added - neighbours.size(); // Should probably return the rank
    }

    public int getEdgeDifference(long v) throws Exception {
        int added = 0;
        if (!exists(v)) {
            throw new Exception("Vertex does not exist!");
        }

        List<Edge> neighbours = getNeighbours(v);

        // Remove the edges connecting to removed vertices.
        neighbours = cleanse(neighbours);
        // Iterator<Edge> iter = neighbours.iterator();
        // while (iter.hasNext()) {
        //     Edge edge = iter.next();
        //     if (getVertex(edge.to).isRemoved()) {
        //         iter.remove();
        //     }
        // }

        long from;
        long to;
        int e1Weight;
        int e2Weight;

        int size = neighbours.size();
        Result<Integer> result;

        for (int outer = 0; outer < size - 1; outer++) {
            from = neighbours.get(outer).to;
            e1Weight = neighbours.get(outer).weight;

            for (int inner = outer + 1; inner < size; inner++) {
                to = neighbours.get(inner).to;
                e2Weight = neighbours.get(inner).weight;

                // Check if there is another short path
                // result = BidirectionalDijkstra.shortestPathWeightLimited(this, from, to, e1Weight + e2Weight);
                result = BidirectionalDijkstra.shortestPathEdgesLimited(this, from, to, 15);

                if (result.result < 0 || result.result == e1Weight + e2Weight) {
                    // Add a shortcut
                    added++;
                }
            }
        }

        // The lower the number is the better
        return added - size;
    }

    public List<Edge> cleanse(List<Edge> neighbours) {
        HashMap<Long, Edge> map = new HashMap<Long, Edge>();
        // Remove the edges connecting to removed vertices.
        Iterator<Edge> iter = neighbours.iterator();
        while (iter.hasNext()) {
            Edge edge = iter.next();
            if (getVertex(edge.to).isRemoved()/* || set.containsKey(edge.to)*/) {
                iter.remove();
            } else if (!map.containsKey(edge.to) || edge.weight < map.get(edge.to).weight) {
                // If vertex found for the first time or the previous one is smaller
                map.put(edge.to, edge);
            }
        }

        return map.values().stream().collect(Collectors.toList());
    }

    public void storeGraph() throws FileNotFoundException, UnsupportedEncodingException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        PrintWriter writer = new PrintWriter(s + "/contracted12.graph", "UTF-8");

        // Vertices and edges
        writer.println(n + " " + m);
        Set<Long> idsSet = vertices.keySet();
        Iterator<Long> ids = idsSet.iterator();
        long id;
        Vertex v;
        while (ids.hasNext()) {
            id = ids.next();
            v = getVertex(id);
            writer.println(id + " " + v.x + " " + v.y + " " + v.rank);
        }

        ids = edges.keySet().iterator();
        int step = 25000;
        int completedVertcies = 0;
        List<Edge> edgesList;
        while (ids.hasNext()) {
            id = ids.next();
            completedVertcies++;
            edgesList = getNeighbours(id);
            for (int i = 0; i < edgesList.size(); i++) {
                writer.println(id + " " + edgesList.get(i).to + " " + edgesList.get(i).weight + " " + edgesList.get(i).contracted);
            }

            if (completedVertcies >= step) {
                System.out.println("Percentage: " + ((float) completedVertcies / (float) idsSet.size()));
            }
        }

        writer.close();
    }
}
