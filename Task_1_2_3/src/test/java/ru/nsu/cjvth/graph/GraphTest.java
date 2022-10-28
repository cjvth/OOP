package ru.nsu.cjvth.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;

interface GraphTest {

    Graph<Object, Integer> createGraph();

    @Test
    default void testPutVertex() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex("1", 1);
        graph.putVertex("3", 3);
        graph.putVertex("2", 2);
        assertIterableEquals(List.of("1", "3", "2"), graph.vertexes());
    }

    @Test
    default void testGetVertexValue() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex("1", 1);
        graph.putVertex("3", 3);
        graph.putVertex("2", 2);
        assertEquals(1, graph.getVertexValue("1"));
        assertEquals(2, graph.getVertexValue("2"));
        assertEquals(3, graph.getVertexValue("3"));
    }

    @Test
    default void testRemoveVertex() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex("1", 1);
        graph.putVertex("3", 3);
        graph.putVertex("2", 2);
        graph.removeVertex("1");
        List<Object> l = graph.vertexes();
        assertEquals(2, l.size());
        assertFalse(l.contains("1"));
        assertNull(graph.getVertexValue("1"));
    }

    @Test
    default void testAddGetEdge() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex("1", 1);
        graph.putVertex("2", 2);
        graph.putVertex("3", 3);
        graph.putEdge("1", "2", 2);
        graph.putEdge("1", "1", 1);
        graph.putEdge("2", "3");
        graph.putEdge("3", "2", 6);
        assertEquals(1, graph.getEdge("1", "1"));
        assertEquals(2, graph.getEdge("1", "2"));
        assertNull(graph.getEdge("1", "3"));
        assertNull(graph.getEdge("2", "1"));
        assertNull(graph.getEdge("2", "2"));
        assertEquals(0, graph.getEdge("2", "3"));
        assertNull(graph.getEdge("3", "1"));
        assertEquals(6, graph.getEdge("3", "2"));
        assertNull(graph.getEdge("3", "3"));
    }
}
