package u04_dijkstra;

import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * @author Tobias Hernandez Perez, 5CN
 */
public class Node implements Comparable<Node>{
    private final String id;
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
        this.distance = Integer.MAX_VALUE;
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
        if (previous == this) return this.getId();
        return previous.getPath() + " - " + this.getId();
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
     * → alle Nachbarn besuchen
     *
     *  kennt/braucht Interface IOfferDistance (=Teil von Graph)
     *
     * offerDistance(node2change, newPrevious, newDistance) –
     * (neu) eintragen in PQ – dort: Wann wird eingetragen?
     */
    public void visit(PriorityQueue<Node> pq) {
        if (isVisited) return;
        isVisited = true;

        edges.stream()
                .sorted(new EdgeComparator())
                .forEach(e -> {
                    Node neighbor = e.getNeighbor();
                    if (!neighbor.equals(this.previous)) {
                        int newDist = this.distance + e.getDistance();
                        if (newDist < neighbor.distance) {
                            neighbor.change(this, newDist);
                            pq.remove(neighbor);
                            pq.add(neighbor);
                        }
                    }
                });
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
