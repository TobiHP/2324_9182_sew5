package u04_dijkstra;

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
        this.distance = -1;
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

    }

    /**
     * Zum Aendern von distance und previous
     */
    public void change() {

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
    public void visit() {

    }

    @Override
    public String toString() {
        return id + ":" + edges.toString();
    }

    @Override
    public int compareTo(Node n) {
        // todo auch nach namen sortieren?
        return Integer.compare(this.distance, n.distance);
    }
}
