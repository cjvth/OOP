package ru.nsu.cjvth.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Directed graph, edges are stored in a table at the intersection of vertex's row and edge's col.
 */
public class IncidenceMatrixGraph<N, V> extends AbstractGraph<N, V> {

    private final List<N> vertexOrder = new LinkedList<>();
    private final Map<N, V> vertexValues = new HashMap<>();
    private final Map<Long, Double> edgeValues = new HashMap<>();
    private final KeyTable<Integer> incidenceMatrix = new KeyTable<>();
    private long lastEdge = 0;

    @Override
    public List<N> vertexes() {
        return new ArrayList<>(vertexOrder);
    }

    @Override
    public void putVertex(N name, V value) {
        if (!vertexValues.containsKey(name)) {
            vertexOrder.add(name);
            incidenceMatrix.addRow(name);
        }
        vertexValues.put(name, value);
    }

    @Override
    public V getVertexValue(N vertex) {
        return vertexValues.get(vertex);
    }

    @Override
    public void removeVertex(N vertex) {
        vertexValues.remove(vertex);
        vertexOrder.remove(vertex);
        Iterator<Long> edgeIterator = edgeValues.keySet().iterator();
        while (edgeIterator.hasNext()) {
            long edge = edgeIterator.next();
            if (incidenceMatrix.get(vertex, edge) != null) {
                incidenceMatrix.removeCol(edge);
                edgeIterator.remove();
            }
        }
        incidenceMatrix.removeRow(vertex);
    }

    @Override
    public void putEdge(N from, N to, double weight) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            throw new IllegalArgumentException();
        }
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(from, edge) == -1
                && incidenceMatrix.get(to, edge) == 1) {
                edgeValues.put(edge, weight);
                return;
            }
        }
        lastEdge++;
        edgeValues.put(lastEdge, weight);
        incidenceMatrix.set(from, lastEdge, -1);
        incidenceMatrix.set(to, lastEdge, 1);
    }

    @Override
    public void putEdge(N from, N to) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            throw new IllegalArgumentException();
        }
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(from, edge) == -1
                && incidenceMatrix.get(to, edge) == 1) {
                edgeValues.put(edge, 0.);
                return;
            }
        }
        lastEdge++;
        edgeValues.put(lastEdge, 0.);
        incidenceMatrix.set(from, lastEdge, -1);
        incidenceMatrix.set(to, lastEdge, 1);
    }

    @Override
    public Double getEdge(N from, N to) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            return null;
        }
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(from, edge) == -1
                && incidenceMatrix.get(to, edge) == 1) {
                return edgeValues.get(edge);
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
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(from, edge) == -1
                && incidenceMatrix.get(to, edge) == 1) {
                edgeValues.remove(edge);
                incidenceMatrix.removeCol(edge);
                return;
            }
        }
    }

    protected boolean bellmanFord(Map<N, Double> dist, Function<Double, Double> distHandler) {
        boolean changed = true;
        for (int i = 0; i < vertexOrder.size() && changed; i++) {
            changed = false;
            for (N v1 : vertexOrder) {
                for (long edge : edgeValues.keySet()) {
                    if (incidenceMatrix.get(v1, edge) == -1) {
                        for (N v2 : vertexOrder) {
                            if (v1 != v2 && incidenceMatrix.get(v2, edge) == 1) {
                                Double newDist = dist.get(v1) + edgeValues.get(edge);
                                if (dist.get(v2) > newDist) {
                                    dist.put(v2, distHandler.apply(newDist));
                                    changed = true;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        return changed;
    }
}
