package u04_dijkstra;

import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge e1, Edge e2) {
        if (!e1.getNeighbor().equals(e2.getNeighbor())) return 0;
        return Integer.compare(e1.getDistance(), e2.getDistance());
    }
}
