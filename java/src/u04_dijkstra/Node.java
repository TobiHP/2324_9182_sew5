package u04_dijkstra;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * @author Tobias Hernandez Perez, 5CN
 */
public class Node implements Comparable<Node>{
    private String id;
    private TreeSet<Edge> edges;
    private int distance;
    private Node previous;
    private boolean isVisited;

    public Node(String id) {
        this.id = id;
        this.edges = new TreeSet<>(new EdgeComparator());
        this.distance = Integer.MAX_VALUE;
        this.previous = null;
        this.isVisited = false;
    }

    public String getId() {
        return id;
    }

    /**
     * Initialisiert den Knoten, um den Dijkstra-Algorithumus neu zu starten
     */
    public void init() {
        this.edges = new TreeSet<>(new EdgeComparator());
        this.distance = -1;
        this.previous = null;
        this.isVisited = false;
    }

    /**
     * Zum Aendern von distance und previous
     */
    public void change(Node newPrevious, int newDistance) {
        previous = newPrevious;
        distance = newDistance;
    }

    /**
     * Liefert den String mit einer Pfad-Darstellung vom Start-Knoten bis zu diesem
     * @return Pfad als String
     */
    public String getPath() {
        return null;
    }

    /**
     * Füegt dem Knoten einen Nachbarn hinzu
     * @param neighbor Nachbar-Node
     * @param distance Distanz zum Nachbarn
     */
    public void addEdge(Node neighbor, int distance) {
        Edge e = new Edge(neighbor, distance);
        this.edges.add(e);
    }

    /**
     * TODO ?
     * → alle Nachbarn besuchen
     *
     *  kennt/braucht Interface IOfferDistance (=Teil von Graph)
     *
     * offerDistance(node2change, newPrevious, newDistance) –
     * (neu) eintragen in PQ – dort: Wann wird eingetragen?
     */
    public void visit(PriorityQueue<Node> pq, Node previous, int distance) {
        if (isVisited) return;

        isVisited = true;

//        if (this.equals(previous)) {
//            this.distance = 0;
//            return;
//        }

        edges.forEach(System.out::println);

        Optional<Edge> previousEdge = edges.stream().filter(e -> e.getNeighbor().equals(previous)).findFirst();
        System.out.println("Previous: " + (previousEdge.isPresent() ? previousEdge.get() : ""));

        System.out.println("hi");
        System.out.println(pq);
        edges.stream().sorted(new EdgeComparator()).forEach(e -> {
            int newDist = this.distance + distance;
            if (newDist < e.getNeighbor().distance) {
                pq.remove(e.getNeighbor());
                e.getNeighbor().distance = newDist;
                e.getNeighbor().previous = this;
                pq.add(e.getNeighbor());
            }

        });
//        this.previous = previous;
//        int minDist = Integer.MAX_VALUE;
//        for (Edge edge : edges) {
//            minDist = Math.min(edge.getDistance(), minDist);
//        }

    }

    @Override
    public String toString() {
        return id + ":" + distance;
    }

    @Override
    public int compareTo(Node n) {
        // todo auch nach namen sortieren?
        return Integer.compare(this.distance, n.distance);
    }
}
