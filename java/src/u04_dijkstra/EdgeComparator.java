package u04_dijkstra;

import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge> {
    @Override
    public int compare(Edge e1, Edge e2) {
        if (e1.getDistance() != e2.getDistance()) return Integer.compare(e1.getDistance(), e2.getDistance());
        return e1.getNeighbor().getId().compareTo(e2.getNeighbor().getId());
    }
}
