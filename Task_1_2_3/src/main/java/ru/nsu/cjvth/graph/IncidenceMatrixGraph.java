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
    public void putEdge(N from, N to, double weight) {
        if (!vertexValues.containsKey(from) || !vertexValues.containsKey(to)) {
            throw new NoSuchElementException();
        }
        if (from == to) {
            throw new IllegalArgumentException();
        }
        for (long edge : edgeValues.keySet()) {
            if (incidenceMatrix.get(from, edge) != null
                && incidenceMatrix.get(to, edge) != null) {
                Edge edgeObject = edgeValues.get(edge);
                if (edgeObject.vertex1 == from) {
                    edgeObject.forward = weight;
                } else {
                    edgeObject.backward = weight;
                }
                return;
            }
        }
        lastEdge++;
        edgeValues.put(lastEdge, new Edge(from, to, weight));
        incidenceMatrix.set(from, lastEdge, true);
        incidenceMatrix.set(to, lastEdge, true);
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
            if (incidenceMatrix.get(from, edge) != null
                && incidenceMatrix.get(to, edge) != null) {
                Edge edgeObject = edgeValues.get(edge);
                if (edgeObject.vertex1 == from) {
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
        edgeValues.put(lastEdge, new Edge(from, to, 0.));
        incidenceMatrix.set(from, lastEdge, true);
        incidenceMatrix.set(to, lastEdge, true);
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
            if (incidenceMatrix.get(from, edge) != null
                && incidenceMatrix.get(to, edge) != null) {
                Edge edgeObject = edgeValues.get(edge);
                if (edgeObject.vertex1 == from) {
                    return edgeObject.forward;
                } else {
                    return edgeObject.backward;
                }
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
            if (incidenceMatrix.get(from, edge) != null
                && incidenceMatrix.get(to, edge) != null) {
                Edge edgeObject = edgeValues.get(edge);
                if (edgeObject.vertex1 == from) {
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

    protected boolean bellmanFord(Map<N, Double> dist, Function<Double, Double> distHandler) {
        boolean changed = true;
        for (int i = 0; i < vertexOrder.size() && changed; i++) {
            changed = false;
            for (N v1 : vertexOrder) {
                for (long e : edgeValues.keySet()) {
                    if (incidenceMatrix.get(v1, e) != null) {
                        for (N v2 : vertexOrder) {
                            if (v1 != v2 && incidenceMatrix.get(v2, e) != null) {
                                Edge edgeObject = edgeValues.get(e);
                                if (edgeObject.vertex1 == v1 && edgeObject.forward != null) {
                                    Double newDist = dist.get(v1) + edgeObject.forward;
                                    if (dist.get(v2) > newDist) {
                                        dist.put(v2, distHandler.apply(newDist));
                                        changed = true;
                                    }
                                } else if (edgeObject.backward != null) {
                                    Double newDist = dist.get(v1) + edgeObject.backward;
                                    if (dist.get(v2) > newDist) {
                                        dist.put(v2, distHandler.apply(newDist));
                                        changed = true;
                                    }
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

    private class Edge {

        final N vertex1;
        final N vertex2;
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
