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

    List<N> vertexOrder;
    Map<N, V> vertexValues;
    KeyTable<Double> edgeTable;

    AdjacencyMatrixGraph() {
        vertexOrder = new LinkedList<>();
        vertexValues = new HashMap<>();
        edgeTable = new KeyTable<>();
    }

    @Override
    public List<N> vertexes() {
        return new ArrayList<>(vertexOrder);
    }

    @Override
    public void putVertex(N name, V value) {
        if (!vertexValues.containsKey(name)) {
            vertexOrder.add(name);
            edgeTable.addRow(name);
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
        edgeTable.removeRow(vertex);
        edgeTable.removeCol(vertex);
    }

    @Override
    public void putEdge(N vertex1, N vertex2, double weight) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        edgeTable.set(vertex1, vertex2, weight);
    }

    @Override
    public void putEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (edgeTable.get(vertex1, vertex2) == null) {
            edgeTable.set(vertex1, vertex2, 0.);
        }
    }

    @Override
    public Double getEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        return edgeTable.get(vertex1, vertex2);
    }

    @Override
    public void removeEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        edgeTable.set(vertex1, vertex2, null);
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
                    Double edge = edgeTable.get(v1, v2);
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
                    Double edge = edgeTable.get(v1, v2);
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
