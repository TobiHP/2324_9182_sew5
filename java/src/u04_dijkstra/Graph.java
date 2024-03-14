package u04_dijkstra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tobias Hernandez Perez, 5CN
 */
public class Graph {
    /**
     * Wird vom Dijkstra-Algorithmus benötigt
     * <p>
     * Sortierung: distance + Name der Node (zwei verschiedene Implentierungs-
     * Varianten möglich: in Node oder als Comperator)
     */
    private final PriorityQueue<Node> pq;

    /**
     * Liste mit allen Knoten des Graphen
     */
    private final List<Node> nodes;

    public Graph() {
        this.pq = new PriorityQueue<>();
        this.nodes = new LinkedList<>();
    }

    /**
     * Liest eine Adjazenz-Matrix aus einer csv-Datei ein.
     * <p>
     * Füllt die nodes-Liste und deren Nachbarn/Kanten (edges)
     * <p>
     * Im Fehlerfall: Exception mit sinnvollem Text
     *
     * @param file csv-Datei
     */
    private void readGraphFromAdjacencyMatrixFile(Path file) throws IOException {
        // todo exceptions
        List<String> allLines = Files.readAllLines(file);
        if (allLines.get(0).split(";").length != allLines.size())
            throw new IllegalArgumentException("The graph provided is invalid!");
        String[] nodeNames = allLines.get(0).substring(1).split(";", -1);
        for (String nodeName : nodeNames) {
            nodes.add(new Node(nodeName));
        }
        AtomicInteger counter = new AtomicInteger();
        allLines.subList(1, allLines.size()).forEach(l -> {
            String[] split = l.split(";", -1);
            if (split.length - 1 != nodeNames.length)
                throw new IllegalArgumentException("The graph provided is invalid!");
            if (!split[0].equals(nodeNames[counter.get()]))
                throw new IllegalArgumentException("The graph provided is invalid!");
            Optional<Node> optionalNode = nodes.stream().filter(n -> n.getId().equals(split[0])).findFirst();
            if (optionalNode.isEmpty()) throw new IllegalArgumentException("The graph provided is invalid!");

            Node curNode = optionalNode.get();

            for (int i = 1; i < split.length; i++) {
                int index = i - 1;
                Optional<Node> neighbor = nodes.stream().filter(n -> n.getId().equals(nodeNames[index])).findFirst();
                if (neighbor.isPresent() && !split[i].isEmpty()) {
                    curNode.addEdge(neighbor.get(), Integer.parseInt(split[i]));
                }
            }
            counter.getAndIncrement();
        });
    }

    /**
     * Liefert einen String mit den kürzesten Pfaden für alle Knoten
     *
     * @return
     */
    private String getAllPaths() {
        StringBuilder allPaths = new StringBuilder();
        for (Node n : nodes) {
            if (n.getDistance() == 0) {
                allPaths.append(n.getPath()).append(": is start node\n");
            } else {
                allPaths.append(n.getPath()).append("\n");
            }
        }
        return allPaths.toString();
    }

    /**
     * Baut einen Dijkstra-Graphen auf
     * Startnode in PQ eintragen
     * solange etwas in der PQ ist:
     * herausnehmen + visit
     *
     * @param startNodeId ID des Startknoten
     */
    private void calcWithDijkstra(String startNodeId) {
        Optional<Node> startNodeOpt = nodes.stream().filter(n -> n.getId().equals(startNodeId)).findFirst();
        if (startNodeOpt.isPresent()) {
            Node startNode = startNodeOpt.get();
            startNode.change(startNode, 0);
            pq.add(startNode);
            while (!pq.isEmpty()) {
                pq.poll().visit(pq);
            }
        } else {
            throw new IllegalArgumentException("The given start node " + startNodeId + " does not exist!");
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        nodes.forEach(n -> {
            if (n.getDistance() == 0) {
                out.append(n.getId()).append(" ----> is start node ");
            } else {
                out.append(n.getTotalDisplay());
            }
            out.append("\n");
        });

        return out.toString();
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        try {
            graph.readGraphFromAdjacencyMatrixFile(Paths.get("res/dijkstra/Graph_A-M.csv"));
//            graph.readGraphFromAdjacencyMatrixFile(Paths.get("res/dijkstra/Graph_A-H.csv"));
//            graph.readGraphFromAdjacencyMatrixFile(Paths.get("res/dijkstra/kaputt_Graph_A-H_f.csv"));
//            graph.readGraphFromAdjacencyMatrixFile(Paths.get("res/dijkstra/big.csv"));
//            graph.readGraphFromAdjacencyMatrixFile(Paths.get("res/dijkstra/unzusammenhaengend_Graph_A-M.csv"));
//            graph.readGraphFromAdjacencyMatrixFile(Paths.get("res/dijkstra/Graph_12_with_names.csv"));
//            System.out.println(graph);
//            System.out.println(graph.getAllPaths());
            graph.calcWithDijkstra("A");
//            System.out.println(graph);
            System.out.println(graph.getAllPaths());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
