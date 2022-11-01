package ru.nsu.cjvth.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * Directed graph, edges from a vertex are stored in a list for that vertex. Recommended
 * implementation for Graph as most efficient
 */
public class AdjacencyListGraph<N, V> implements Graph<N, V> {

    private final List<N> vertexOrder = new LinkedList<>();
    private final Map<N, V> vertexValues = new HashMap<>();
    private final Map<N, List<Edge>> adjacencyLists = new HashMap<>();

    @Override
    public List<N> vertexes() {
        return new ArrayList<>(vertexOrder);
    }

    @Override
    public void putVertex(N name, V value) {
        if (!vertexValues.containsKey(name)) {
            vertexOrder.add(name);
            adjacencyLists.put(name, new LinkedList<>());
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
        adjacencyLists.remove(vertex);
    }

    @Override
    public void putEdge(N from, N to, double weight) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            throw new IllegalArgumentException();
        }
        for (Edge edge : adjacencyLists.get(from)) {
            if (edge.to == to) {
                edge.weight = weight;
                return;
            }
        }
        adjacencyLists.get(from).add(new Edge(to, weight));
    }

    @Override
    public void putEdge(N from, N to) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            throw new IllegalArgumentException();
        }
        for (Edge edge : adjacencyLists.get(from)) {
            if (edge.to == to) {
                return;
            }
        }
        adjacencyLists.get(from).add(new Edge(to, 0.));
    }

    @Override
    public Double getEdge(N from, N to) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            return null;
        }
        for (Edge edge : adjacencyLists.get(from)) {
            if (edge.to == to) {
                return edge.weight;
            }
        }
        return null;
    }

    @Override
    public void removeEdge(N from, N to) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            return;
        }
        adjacencyLists.get(from).removeIf(e -> e.to == to);
    }

    @Override
    public Map<N, Double> calculateDistancesFrom(N selectedVertex) {
        Map<N, Double> dist = new HashMap<>();
        for (N v : vertexOrder) {
            dist.put(v, Double.POSITIVE_INFINITY);
        }
        dist.put(selectedVertex, 0.);
        boolean changed = true;
        for (int i = 0; i < vertexOrder.size() && changed; i++) {
            changed = false;
            for (N v1 : vertexOrder) {
                for (Edge edge : adjacencyLists.get(v1)) {
                    Double newDist = dist.get(v1) + edge.weight;
                    if (dist.get(edge.to) > newDist) {
                        dist.put(edge.to, newDist);
                        changed = true;
                    }
                }
            }
        }
        for (int i = 0; i < vertexOrder.size() && changed; i++) {
            changed = false;
            for (N v1 : vertexOrder) {
                for (Edge edge : adjacencyLists.get(v1)) {
                    Double newDist = dist.get(v1) + edge.weight;
                    if (dist.get(edge.to) > newDist) {
                        dist.put(edge.to, Double.NEGATIVE_INFINITY);
                        changed = true;
                    }
                }
            }
        }
        return dist;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void sortByDistanceFrom(N selectedVertex) {
        Map<N, Double> distances = calculateDistancesFrom(selectedVertex);
        var entries = new ArrayList<>(distances.entrySet());
        entries.sort(Entry.comparingByValue());
        for (int i = 0; i < entries.size(); i++) {
            vertexOrder.set(i, entries.get(i).getKey());
        }
    }

    private class Edge {
        N to;
        double weight;

        public Edge(N to, double weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}
