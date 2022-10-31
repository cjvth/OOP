package ru.nsu.cjvth.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Directed graph, edges are stored in a table at the intersection of vertex's row and edge's col.
 */
public class IncidenceMatrixGraph<N, V> implements Graph<N, V> {

    private final List<N> vertexOrder = new LinkedList<>();
    private final Map<N, V> vertexValues = new HashMap<>();
    private final Map<Long, Edge> edgeValues = new HashMap<>();
    private final KeyTable<Boolean> incidenceMatrix = new KeyTable<>();
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
    public void putEdge(N vertex1, N vertex2, double weight) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (vertex1 == vertex2) {
            throw new IllegalArgumentException();
        }
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(vertex1, edge) != null
                && incidenceMatrix.get(vertex2, edge) != null) {
                Edge edgeObject = edgeValues.get(edge);
                if (edgeObject.vertex1 == vertex1) {
                    edgeObject.forward = weight;
                } else {
                    edgeObject.backward = weight;
                }
                return;
            }
        }
        lastEdge++;
        edgeValues.put(lastEdge, new Edge(vertex1, vertex2, weight));
        incidenceMatrix.set(vertex1, lastEdge, true);
        incidenceMatrix.set(vertex2, lastEdge, true);
    }

    @Override
    public void putEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (vertex1 == vertex2) {
            throw new IllegalArgumentException();
        }
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(vertex1, edge) != null
                && incidenceMatrix.get(vertex2, edge) != null) {
                Edge edgeObject = edgeValues.get(edge);
                if (edgeObject.vertex1 == vertex1) {
                    if (edgeObject.forward == null) {
                        edgeObject.forward = 0.;
                    }
                } else {
                    if (edgeObject.backward == null) {
                        edgeObject.backward = 0.;
                    }
                }
                return;
            }
        }
        lastEdge++;
        edgeValues.put(lastEdge, new Edge(vertex1, vertex2, 0.));
        incidenceMatrix.set(vertex1, lastEdge, true);
        incidenceMatrix.set(vertex2, lastEdge, true);
    }

    @Override
    public Double getEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (vertex1 == vertex2) {
            return null;
        }
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(vertex1, edge) != null
                && incidenceMatrix.get(vertex2, edge) != null) {
                Edge edgeObject = edgeValues.get(edge);
                if (edgeObject.vertex1 == vertex1) {
                    return edgeObject.forward;
                } else {
                    return edgeObject.backward;
                }
            }
        }
        return null;
    }

    @Override
    public void removeEdge(N vertex1, N vertex2) {
        if (!vertexValues.containsKey(vertex1) || !vertexValues.containsKey(vertex2)) {
            throw new NoSuchElementException();
        }
        if (vertex1 == vertex2) {
            return;
        }
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(vertex1, edge) != null
                && incidenceMatrix.get(vertex2, edge) != null) {
                Edge edgeObject = edgeValues.get(edge);
                if (edgeObject.vertex1 == vertex1) {
                    edgeObject.forward = null;
                    if (edgeObject.backward == null) {
                        edgeValues.remove(edge);
                        incidenceMatrix.removeCol(edge);
                    }
                } else {
                    edgeObject.backward = null;
                    if (edgeObject.forward == null) {
                        edgeValues.remove(edge);
                        incidenceMatrix.removeCol(edge);
                    }
                }
                return;
            }
        }
    }

    @Override
    public Map<N, Double> calculateDistancesFrom(N selectedVertex) {
        return null;
    }

    @Override
    public void sortByDistanceFrom(N selectedVertex) {

    }

    private class Edge {

        N vertex1;
        N vertex2;
        Double forward;
        Double backward;

        Edge(N vertex1, N vertex2, double forward) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.forward = forward;
            this.backward = null;
        }
    }
}
