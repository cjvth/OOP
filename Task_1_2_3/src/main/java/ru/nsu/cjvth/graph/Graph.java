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
     * Return order of vertices.
     *
     * @return ordered list of vertex names
     */
    List<N> vertexes();

    /**
     * Add a new vertex or change the value of existing vertex.
     *
     * @param name  name of the new or changed vertex
     * @param value new value of that vertex
     */
    void putVertex(N name, V value);

    /**
     * Get value of a vertex.
     *
     * @param vertex name of the vertex
     * @return value of the vertex or `null` if no such vertex exists
     */
    V getVertexValue(N vertex);

    /**
     * Remove vertex of given name and all edges incident with it from the graph if that edge
     * exists.
     *
     * @param vertex name of the vertex
     */
    void removeVertex(N vertex);

    /**
     * Add a new directed edge between two vertexes or set the value of existing edge.
     *
     * @param from   name of the vertex the edge directed from
     * @param to     name of the vertex the edge directed to
     * @param weight weight of the edge
     * @throws java.util.NoSuchElementException if no such one or both vertexes in the graph
     * @throws IllegalArgumentException         if from and to are same
     */
    void putEdge(N from, N to, double weight);

    /**
     * Add a new directed edge between two vertexes with zero weight or not change existing edge.
     *
     * @param from   name of the vertex the edge directed from
     * @param to     name of the vertex the edge directed to
     * @throws java.util.NoSuchElementException if no such one or both vertexes in the graph
     * @throws IllegalArgumentException         if from and to are same
     */
    void putEdge(N from, N to);

    /**
     * Get weight of an edge between two vertices.
     *
     * @param from   name of the vertex the edge directed from
     * @param to     name of the vertex the edge directed to
     * @return weight of the edge or `null` if it doesn't exist.
     * @throws java.util.NoSuchElementException if no such one or both vertexes in the graph
     */
    Double getEdge(N from, N to);

    /**
     * Remove the edge between two vertices if it exists.
     *
     * @param from   name of the vertex the edge directed from
     * @param to     name of the vertex the edge directed to
     * @throws java.util.NoSuchElementException if no such one or both vertexes in the graph
     */
    void removeEdge(N from, N to);

    /**
     * Calculate the shortest path from selected vertex to all vertexes using Bellman-Ford
     * algorithm.
     *
     * @param selectedVertex vertex from which the shortest paths are calculated
     * @return A map with all vertexes of the graph as keys and distances between selected vertex
     *         and them as values. If there's no path to some vertex, the result is
     *         `Double.POSITIVE_INFINITY`. If you can reach it with a negative cycle,
     *         then`Double.NEGATIVE_INFINITY`.
     */
    Map<N, Double> calculateDistancesFrom(N selectedVertex);

    /**
     * Sort vertexes by shortest path from the selected vertex. Vertexes that can be reached through
     * a negative-weight cycle are put in the beginning of the order. Vertexes that are not
     * reachable from the given vertex are put in the end.
     *
     * @param selectedVertex vertex from which the shortest paths are calculated
     */
    void sortByDistanceFrom(N selectedVertex);
}
