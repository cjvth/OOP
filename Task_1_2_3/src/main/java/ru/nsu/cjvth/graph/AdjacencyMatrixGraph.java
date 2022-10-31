package ru.nsu.cjvth.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * Directed graph, edges are stored in a table at the intersection of two vertexes' row and col.
 */
public class AdjacencyMatrixGraph<N, V> implements Graph<N, V> {

    private final List<N> vertexOrder = new LinkedList<>();
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
    public void putEdge(N vertex1, N vertex2, double weight) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (vertex1 == vertex2) {
            throw new IllegalArgumentException();
        }
        adjacencyMatrix.set(vertex1, vertex2, weight);
    }

    @Override
    public void putEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (vertex1 == vertex2) {
            throw new IllegalArgumentException();
        }
        if (adjacencyMatrix.get(vertex1, vertex2) == null) {
            adjacencyMatrix.set(vertex1, vertex2, 0.);
        }
    }

    @Override
    public Double getEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (vertex1 == vertex2) {
            return null;
        }
        return adjacencyMatrix.get(vertex1, vertex2);
    }

    @Override
    public void removeEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (vertex1 == vertex2) {
            return;
        }
        adjacencyMatrix.set(vertex1, vertex2, null);
    }

    @Override
    public Map<N, Double> calculateDistancesFrom(N selectedVertex) {
        Map<N, Double> dist = new HashMap<>();
        for (var v : vertexOrder) {
            dist.put(v, Double.POSITIVE_INFINITY);
        }
        dist.put(selectedVertex, 0.);
        boolean changed = true;
        for (int i = 0; i < vertexOrder.size() && changed; i++) {
            changed = false;
            for (var v1 : vertexOrder) {
                for (var v2 : vertexOrder) {
                    Double edge = adjacencyMatrix.get(v1, v2);
                    if (edge != null) {
                        Double newDist = dist.get(v1) + edge;
                        if (dist.get(v2) > newDist) {
                            dist.put(v2, newDist);
                            changed = true;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < vertexOrder.size() && changed; i++) {
            changed = false;
            for (var v1 : vertexOrder) {
                for (var v2 : vertexOrder) {
                    Double edge = adjacencyMatrix.get(v1, v2);
                    if (edge != null) {
                        Double newDist = dist.get(v1) + edge;
                        if (dist.get(v2) > newDist) {
                            dist.put(v2, Double.NEGATIVE_INFINITY);
                            changed = true;
                        }
                    }
                }
            }
        }
        return dist;
    }

    @Override
    public void sortByDistanceFrom(N selectedVertex) {
        Map<N, Double> distances = calculateDistancesFrom(selectedVertex);
        var entries = new ArrayList<>(distances.entrySet());
        entries.sort(Entry.comparingByValue());
        for (int i = 0; i < entries.size(); i++) {
            vertexOrder.set(i, entries.get(i).getKey());
        }
    }
}
