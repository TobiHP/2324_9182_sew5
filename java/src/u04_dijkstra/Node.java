package u04_dijkstra;

import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * @author Tobias Hernandez Perez, 5CN
 */
public class Node implements Comparable<Node> {
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

    public int getDistance() {
        return distance;
    }

    public TreeSet<Edge> getEdges() {
        return edges;
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
     *
     * @return Pfad als String
     */
    public String getPath() {
        if (previous == null) {
            return "no path available for " + getTotalDisplay();
        }
        if (previous == this) return id;
        return previous.getPath() + " --(" + distance + ")-> " + this.getId();
    }

    /**
     * Displays the current node in the following format
     * <p></p>
     * {id} [totalDistance: {distance}] {neighbors}
     *
     * @return
     */
    public String getTotalDisplay() {
        StringBuilder out = new StringBuilder();
        getEdges().forEach(e -> out.append(e).append(", "));
        out.delete(out.length() - 2, out.length() - 1);
        return id + " [totalDistance: " + (getDistance() == Integer.MAX_VALUE ? "?" : getDistance()) + "] " + out;
    }

    /**
     * Füegt dem Knoten einen Nachbarn hinzu
     *
     * @param neighbor Nachbar-Node
     * @param distance Distanz zum Nachbarn
     */
    public void addEdge(Node neighbor, int distance) {
        Edge e = new Edge(neighbor, distance);
        this.edges.add(e);
    }

    /**
     * → alle Nachbarn besuchen
     * <p>
     * kennt/braucht Interface IOfferDistance (=Teil von Graph)
     * <p>
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
        return Integer.compare(this.distance, n.distance);
    }
}
