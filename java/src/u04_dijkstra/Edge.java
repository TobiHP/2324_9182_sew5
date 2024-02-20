package u04_dijkstra;

/**
 * @author Tobias Hernandez Perez, 5CN
 */
public class Edge {
    private final Node neighbor;
    private final int distance;

    public Edge(Node neighbor, int distance) {
        this.distance = distance;
        this.neighbor = neighbor;
    }

    public Node getNeighbor() {
        return neighbor;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return neighbor.getId() + ":" + distance;
    }
}
