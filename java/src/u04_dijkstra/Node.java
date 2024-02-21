package u04_dijkstra;

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

        pq.poll();
        if (distance < this.distance) {
            this.distance = distance;
            this.previous = previous;
            pq.add(this);
        }

//        Optional<Edge> previousEdge = edges.stream().filter(e -> e.getNeighbor().equals(previous)).findFirst();

        edges.stream()
                .sorted(new EdgeComparator())
                .forEach(e -> e.getNeighbor().visit(pq, this, this.distance + e.getDistance()));
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
