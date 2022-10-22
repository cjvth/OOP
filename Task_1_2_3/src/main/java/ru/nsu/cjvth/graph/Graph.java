package ru.nsu.cjvth.graph;

import java.util.List;

/**
 * Graph data type. Supports adding named vertices and double-weighted edges between them.
 *
 * @param <N> Type of vertex names
 * @param <E> Type of vertex values
 */
public interface Graph<N, E> {

    /**
     * Return ordered list of vertices names.
     */
    List<N> vertexes();

    /**
     * Add a new named vertex or change the value of existing vertex.
     */
    void pushVertex(N name, E value);

    /**
     * Get value of a vertex.
     */
    E getVertexValue(N vertex);

    /**
     * Add a new edge between two vertexes or set the value of existing vertex.
     *
     * @param weight weight of the edge
     */
    void pushEdge(N vertex1, N vertex2, double weight);

    /**
     * Add a new edge between two vertexes with zero weight or not change existing edge.
     */
    void pushEdge(N vertex1, N vertex2);

    /**
     * Get weight of an edge between two vertices.
     *
     * @return weight of the edge or `null` if it doesn't exist.
     */
    Double getEdge(N vertex1, N vertex2);

    /**
     * Remove an edge between two vertices.
     */
    void removeEdge(N vertex1, N vertex2);

    /**
     * Sort vertexes by distance from the selected vertex. Graph must not contain negative-weighted
     * edges
     */
    void sortByDistanceFrom(N selectedVertex);
}