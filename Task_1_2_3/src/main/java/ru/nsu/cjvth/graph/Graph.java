package ru.nsu.cjvth.graph;

import java.util.List;
import java.util.Map;

/**
 * Directed graph. Supports adding named vertices and double-weighted edges between them.
 *
 * @param <N> Type of vertex names
 * @param <V> Type of vertex values
 */
public interface Graph<N, V> {

    /**
     * Return ordered list of vertices names.
     */
    List<N> vertexes();

    /**
     * Add a new named vertex or change the value of existing vertex.
     */
    void putVertex(N name, V value);

    /**
     * Get value of a vertex or `null` if no such vertex exists.
     */
    V getVertexValue(N vertex);

    /**
     * Remove vertex of given name from the graph if it exists.
     */
    void removeVertex(N vertex);

    /**
     * Add a new edge between two vertexes or set the value of existing vertex.
     *
     * @param weight weight of the edge
     */
    void putEdge(N vertex1, N vertex2, double weight);

    /**
     * Add a new edge between two vertexes with zero weight or not change existing edge.
     */
    void putEdge(N vertex1, N vertex2);

    /**
     * Get weight of an edge between two vertices.
     *
     * @return weight of the edge or `null` if it doesn't exist.
     */
    Double getEdge(N vertex1, N vertex2);

    /**
     * Remove the edge between two vertices if it exists.
     */
    void removeEdge(N vertex1, N vertex2);

    /**
     * Calculate the shortest path from selected vertex to all vertexes using Bellman-Ford
     * algorithm. If there's no path to some vertex, the result is `Double.POSITIVE_INFINITY`. If
     * you can reach it with a negative cycle, then `Double.NEGATIVE_INFINITY`.
     */
    Map<N, Double> calculateDistancesFrom(N selectedVertex);

    /**
     * Sort vertexes by distance from selected vertex. Vertexes that are
     * edges
     */
    void sortByDistanceFrom(N selectedVertex);
}