package ru.nsu.cjvth.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Directed graph, edges are stored in a table at the intersection of two vertexes' row and col.
 */
public class AdjacencyMatrixGraph<N, V> extends AbstractGraph<N, V> {

    private final Map<N, V> vertexValues = new HashMap<>();
    private final KeyTable<Double> adjacencyMatrix = new KeyTable<>();


    @Override
    public List<N> vertexes() {
        return new ArrayList<>(vertexOrder);
    }

    @Override
    public void putVertex(N name, V value) {
        if (!vertexValues.containsKey(name)) {
            vertexOrder.add(name);
            adjacencyMatrix.addRow(name);
        }
        vertexValues.put(name, value);
    }

    @Override
    public V getVertexValue(N vertex) {
        return vertexValues.get(vertex);
    }

    @Override
    public void removeVertex(N vertex) {
        vertexOrder.remove(vertex);
        vertexValues.remove(vertex);
        adjacencyMatrix.removeRow(vertex);
        adjacencyMatrix.removeCol(vertex);
    }

    @Override
    public void putEdge(N from, N to, double weight) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            throw new IllegalArgumentException();
        }
        adjacencyMatrix.set(from, to, weight);
    }

    @Override
    public void putEdge(N from, N to) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            throw new IllegalArgumentException();
        }
        if (adjacencyMatrix.get(from, to) == null) {
            adjacencyMatrix.set(from, to, 0.);
        }
    }

    @Override
    public Double getEdge(N from, N to) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            return null;
        }
        return adjacencyMatrix.get(from, to);
    }

    @Override
    public void removeEdge(N from, N to) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            return;
        }
        adjacencyMatrix.set(from, to, null);
    }

    protected boolean bellmanFord(Map<N, Double> dist, Function<Double, Double> distHandler) {
        boolean changed = true;
        for (int i = 0; i < vertexOrder.size() && changed; i++) {
            changed = false;
            for (N v1 : vertexOrder) {
                for (N v2 : vertexOrder) {
                    Double edge = adjacencyMatrix.get(v1, v2);
                    if (edge != null) {
                        Double newDist = dist.get(v1) + edge;
                        if (dist.get(v2) > newDist) {
                            dist.put(v2, distHandler.apply(newDist));
                            changed = true;
                        }
                    }
                }
            }
        }
        return changed;
    }
}
