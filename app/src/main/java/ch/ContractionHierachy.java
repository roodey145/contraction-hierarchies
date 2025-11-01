package ch;

import java.util.PriorityQueue;

public class ContractionHierachy {
    
    Graph g;
    Graph conHierarchy;
    private long[] ids;
    private final PriorityQueue<PQElem> pq;

    public ContractionHierachy() {
        // To be filled out
        conHierarchy = new Graph();
        ids = Main.getIds();
        pq = new PriorityQueue<>();
    }

    public Result<Integer> query(long s, long t) {

        // To be filled out
        return new Result<Integer>(0, 0, 0);
    }

    public void storeGraph(Graph g) {
        // To be filled out
        this.g = g;
    }

    public void preprocess() throws Exception {
        // Order ids
        for(long id : ids) {
            pq.add(new PQElem(rank(id), id));
        }

        int initialSize = pq.size();
        int step = (int)(initialSize * 0.15); // Every 15% reorder

        System.out.println("Initial Vertices Rank!");

        int contractedVertices = 0;
        int cyclicContractedV = 0;
        int reportingStep = 1000;
        int smallStep = 10;
        PQElem elem;
        int tRank;
        int reorder = 0;

        while(!pq.isEmpty()) {
            // Get the top element of the queue
            elem = pq.poll();
            
            // Recompute rank
            tRank = rank(elem.v);

            // If vertex is the lowest rank then proceed
            if(!pq.isEmpty() && pq.peek().key < tRank) {
                // Rank is higher now. Re-insert updated element and skip it for now
                // pq.add(new PQElem(tRank, elem.v));
                // Update element weight
                elem.updateKey(tRank);
                pq.add(elem);

                if((++reorder) >= ids.length) {
                    System.out.println("Size: " + pq.size() + ", Reordered: " + reorder);
                    reorder = 0;
                }
                // System.out.print("|");
                continue;
            }

            if(pq.size() % step == 0 && pq.size() != initialSize) {
                orderQueue();
            }

            // Contract vertex
            g.contract(elem.v, ++contractedVertices);
            reorder = 0;

            // System.out.print(".");

            if((++cyclicContractedV) == reportingStep) {
                cyclicContractedV = 0;
                System.out.println("\nCurrent Progress: " + (contractedVertices / reportingStep) + "/" + (ids.length / reportingStep));
            }

            if(cyclicContractedV % smallStep == 0 && cyclicContractedV != 0) {
                System.out.print("-");
            }
        }
    }


    // Reorder priority queue
    public void orderQueue() throws Exception {
        Object[] elements = pq.toArray();
        pq.clear();
        PQElem e;
        for(Object elem : elements) {
            e = (PQElem) elem;
            e.key = rank(e.v);
            pq.add(e);
        }

        System.out.println("Reordered");
    }
    

    public int rank(long id) throws Exception {
        // Vertex with many neighbours would be negative
        // And the more edges that are removed, the lower the edge difference
        // this might result in e.g. -5 - 10 i.e. -15. This number is negated
        // due to the Priority queue as it starts from the lowest ranked edges
        // and we want to start with useless edges first.
        return /*-*/(/*g.getEdgeDifference(id) - g.degree(id) - g.avgWeight(id) */ g.getEdgeDifference(id) /* - g.degree(id)+ g.avgWeight(id)*/);
    }
}