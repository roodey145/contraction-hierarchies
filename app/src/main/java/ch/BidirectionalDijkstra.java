package ch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;


public class BidirectionalDijkstra {
    // To be made!
    


    public static Result<Integer> shortestPath(Graph g, long from, long to) {
        long start = System.nanoTime();
        // Left distances map focuses on the start
        final Map<Long, Integer> dl = new HashMap<>();
        dl.put(from, 0);

        // Right distances map focuses on the target i.e. to
        final Map<Long, Integer> dr = new HashMap<>();
        dr.put(to, 0);

        final Set<Long> settled = new HashSet<>();
        final PriorityQueue<PQElem> Ql = new PriorityQueue<>();
        Ql.add(new PQElem(0, from));
        final PriorityQueue<PQElem> Qr = new PriorityQueue<>();
        Qr.add(new PQElem(0, to));
        int relaxed = 0;
        int i; // 0 means left, 1 means right
        PriorityQueue<PQElem> Qi;
        Map<Long, Integer> dists;

        int d = Integer.MAX_VALUE;
        boolean firstSettle = true;
        boolean foundOptimistic = false;
        

        while(!foundOptimistic && (!Ql.isEmpty() || !Qr.isEmpty())) {
            if(!Ql.isEmpty() && Ql.peek().compareTo(Qr.peek()) <= 0) {
                // If first element in Ql is less than or equal to first element in Qr
                i = 0;
            } else{
                i = 1;
            }

            Qi = i == 0 ? Ql : Qr;
            PQElem minElm = Qi.poll(); // Gets the min element
            Long u = minElm.v;
            int dist = minElm.key;

            if(settled.contains(u)) {
                int QlMin = !Ql.isEmpty() ? Ql.peek().key : Integer.MAX_VALUE;
                int QrMin = !Qr.isEmpty() ? Qr.peek().key : Integer.MAX_VALUE;

                if(firstSettle) {
                    System.out.println("First Settle: " + d + ", Visited: " + (relaxed) + ", Time: " + ((System.nanoTime() - start) / 1000));
                    firstSettle = false;
                }

                if(dl.containsKey(u)){
                    int dlDist = dl.get(u);

                    if(dl.containsKey(u) && dlDist + dist < QlMin && dlDist + dist < QrMin) {
                        // Should be the correct solution 
                        System.out.println("Min Dist: " + (dlDist + dist));
                        break;
                    }
                }
                
                if(dr.containsKey(u)) {
                    int drDist = dr.get(u);

                    if(dr.containsKey(u) && drDist + dist < QlMin && drDist + dist < QrMin) {
                        // Should be the correct solution 
                        System.out.println("Min Dist: " + (drDist + dist));
                        break;
                    }
                }
    
                if(d < QlMin && d < QrMin) {
                    break;
                }

                if(d != Integer.MAX_VALUE) break; // Optimistic 

                // break;
            }

            settled.add(u);

            dists = i == 0 ? dl : dr;
            if(!g.exists(u)) break; // Doesn't exists
            for (Graph.Edge e : g.getNeighbours(u)) {
                relaxed++;
                long v = e.to;
                int w = e.weight;
                if (!dists.containsKey(v) || dists.get(v) > dist + w) {
                    dists.put(v, dist + w);
                    Qi.add(new PQElem(dist + w, v));
                }

                if(dl.containsKey(v) && dr.containsKey(v)) {
                    d = d < dl.get(v) + dr.get(v) ? d : dl.get(v) + dr.get(v);
                    // System.out.println("First Settle: " + d + ", Visited: " + (relaxed) + ", Time: " + ((System.nanoTime() - start) / 1000));
                    foundOptimistic = true;
                    break;
                    // System.out.println("D:" + d + ", Dl: " + dl.get(v) + ", Dr: " + dr.get(v));
                }

            }


        }

        long end = System.nanoTime();

        return new Result<>(end - start, relaxed, d);
    }



    public static void main(String[] args) {
        final PriorityQueue<PQElem> Ql = new PriorityQueue<>();
        Ql.add(new PQElem(0, 5));
        Ql.add(new PQElem(1, 5));
        Ql.add(new PQElem(5, 5));

        System.out.println((Ql.peek()).compareTo(Ql.poll()));
        System.out.println(Ql.poll().compareTo(Ql.poll()));
        // System.out.println(Ql.peek().key);
    }

}