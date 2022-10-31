package ru.nsu.cjvth.graph;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
        graph.putVertex("2", 143223);
        graph.putVertex("3", 3);
        graph.putVertex("2", 2);
        assertEquals(1, graph.getVertexValue("1"));
        assertEquals(2, graph.getVertexValue("2"));
        assertEquals(3, graph.getVertexValue("3"));
        assertNull(graph.getVertexValue("4"));
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
    default void testRemoveUnknownVertex() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex("1", 1);
        graph.putVertex("2", 2);
        assertDoesNotThrow(() -> graph.removeVertex("3"));
        graph.removeVertex("2");
        assertDoesNotThrow(() -> graph.removeVertex("2"));
    }

    @Test
    default void testPutGetEdge() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex("1", 1);
        graph.putVertex("2", 2);
        graph.putVertex("3", 3);
        graph.putEdge("1", "2", 2);
        graph.putEdge("3", "2", 123421);
        graph.putEdge("2", "3");
        graph.putEdge("3", "2", 6);
        graph.putEdge("1", "2");
        assertNull(graph.getEdge("1", "1"));
        assertEquals(2, graph.getEdge("1", "2"));
        assertNull(graph.getEdge("1", "3"));
        assertNull(graph.getEdge("2", "1"));
        assertNull(graph.getEdge("2", "2"));
        assertEquals(0, graph.getEdge("2", "3"));
        assertNull(graph.getEdge("3", "1"));
        assertEquals(6, graph.getEdge("3", "2"));
        assertNull(graph.getEdge("3", "3"));
    }

    @Test
    default void testEdgeBetweenUnknownVertexes() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        graph.putEdge(1, 2, 10);
        assertThrows(NoSuchElementException.class, () -> graph.putEdge(1, 3, 100));
        assertThrows(NoSuchElementException.class, () -> graph.putEdge(7, 2));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge(23, 25));
        assertThrows(NoSuchElementException.class, () -> graph.removeEdge(0, 0));
    }

    @Test
    default void testEdgeBetweenSameVertexes() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        graph.putEdge(1, 2, 10);
        assertThrows(IllegalArgumentException.class, () -> graph.putEdge(1, 1, 100));
        assertThrows(IllegalArgumentException.class, () -> graph.putEdge(2, 2));
        assertNull(graph.getEdge(1, 1));
        assertDoesNotThrow(() -> graph.removeEdge(2, 2));
    }

    @Test
    default void testPutEdgeRemoveVertex() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        graph.putVertex(3, 3);
        graph.putEdge(1, 2);
        graph.putEdge(1, 3);
        graph.putEdge(2, 3);
        graph.putEdge(3, 1);
        graph.putEdge(3, 2);
        graph.removeVertex(2);
        assertNull(graph.getEdge(1, 1));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge(1, 2));
        assertNotNull(graph.getEdge(1, 3));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge(2, 1));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge(2, 2));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge(2, 3));
        assertNotNull(graph.getEdge(3, 1));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge(3, 2));
        assertNull(graph.getEdge(3, 3));
    }

    @Test
    default void testRemoveEdge() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        graph.putVertex(3, 1);
        graph.putEdge(2, 1);
        graph.putEdge(1, 2);
        graph.putEdge(1, 2);
        graph.putEdge(2, 1, 100);
        graph.removeEdge(1, 2);
        assertNull(graph.getEdge(1, 2));
        assertEquals(100., graph.getEdge(2, 1));
        graph.putEdge(1, 3);
        graph.putEdge(3, 1);
        graph.removeEdge(1, 3);
        assertNull(graph.getEdge(1, 3));
        assertNotNull(graph.getEdge(3, 1));
        graph.removeEdge(3, 1);
        assertNull(graph.getEdge(3, 1));
    }

    @Test
    default void testRemoveAndPutEdge() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        graph.putEdge(1, 2);
        graph.putEdge(2, 1);
        graph.removeEdge(1, 2);
        assertNull(graph.getEdge(1, 2));
        assertNotNull(graph.getEdge(2, 1));
        graph.putEdge(1, 2);
        assertNotNull(graph.getEdge(1, 2));
        assertNotNull(graph.getEdge(2, 1));
        graph.removeEdge(2, 1);
        assertNotNull(graph.getEdge(1, 2));
        assertNull(graph.getEdge(2, 1));
        graph.putEdge(2, 1);
        assertNotNull(graph.getEdge(1, 2));
        assertNotNull(graph.getEdge(2, 1));
    }

    @Test
    default void testPutEdgeUnknownVertexes() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        assertDoesNotThrow(() -> graph.putEdge(1, 2));
        assertThrows(NoSuchElementException.class, () -> graph.putEdge(1, "2"));
        assertThrows(NoSuchElementException.class, () -> graph.putEdge("1", 2));
        assertThrows(NoSuchElementException.class, () -> graph.putEdge("1", "2"));
    }

    @Test
    default void testGetEdgeUnknownVertexes() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        graph.putEdge(1, 2);
        assertDoesNotThrow(() -> graph.getEdge(1, 2));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge(1, "2"));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge("1", 2));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge("1", "2"));
    }

    @Test
    default void testRemoveEdgeUnknownVertexes() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        graph.putEdge(1, 2);
        assertDoesNotThrow(() -> graph.removeEdge(1, 2));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge(1, "2"));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge("1", 2));
        assertThrows(NoSuchElementException.class, () -> graph.getEdge("1", "2"));
    }

    @Test
    default void testRemoveNotExistedEdge() {
        Graph<Object, Integer> graph = createGraph();
        graph.putVertex(1, 1);
        graph.putVertex(2, 2);
        graph.putVertex(3, 3);
        graph.putEdge(1, 2);
        assertDoesNotThrow(() -> graph.removeEdge(1, 2));
        assertDoesNotThrow(() -> graph.removeEdge(1, 2));
        assertDoesNotThrow(() -> graph.removeEdge(1, 3));
    }

    @ParameterizedTest(name = "Testing with file {0}")
    @ValueSource(strings = {"common1.in", "common2.in",
        "negativeCycle1.in", "negativeCycle2.in"})
    default void testCalculateDistances(String fileName) throws IOException {
        Graph<Object, Integer> graph = createGraph();
        try (var scanner = new Scanner(Paths.get("tests/calculateDistances/".concat(fileName)))) {
            int vertexesCount = scanner.nextInt();
            for (int i = 0; i < vertexesCount; i++) {
                graph.putVertex(i, 0);
            }
            int edgesCount = scanner.nextInt();
            for (int i = 0; i < edgesCount; i++) {
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                double weight = scanner.nextDouble();
                graph.putEdge(from, to, weight);
            }
            int calculations = scanner.nextInt();
            for (int i = 0; i < calculations; i++) {
                int from = scanner.nextInt();
                Map<Object, Double> distances = graph.calculateDistancesFrom(from);
                for (int j = 0; j < vertexesCount; j++) {
                    String stringDist = scanner.next();
                    double dist;
                    switch (stringDist) {
                        case "+":
                            dist = Double.POSITIVE_INFINITY;
                            break;
                        case "-":
                            dist = Double.NEGATIVE_INFINITY;
                            break;
                        default:
                            dist = Double.parseDouble(stringDist);
                            break;
                    }
                    assertEquals(dist, distances.get(j));
                }
            }
        }
    }

    @ParameterizedTest(name = "Testing with file {0}")
    @ValueSource(strings = {"common1.in", "common2.in",
        "negativeCycle1.in", "negativeCycle2.in"})
    default void testSortByDistanceFrom(String fileName) throws IOException {
        Graph<Object, Integer> graph = createGraph();
        try (var scanner = new Scanner(Paths.get("tests/calculateDistances/".concat(fileName)))) {
            int vertexesCount = scanner.nextInt();
            for (int i = 0; i < vertexesCount; i++) {
                graph.putVertex(i, 0);
            }
            int edgesCount = scanner.nextInt();
            for (int i = 0; i < edgesCount; i++) {
                int from = scanner.nextInt();
                int to = scanner.nextInt();
                double weight = scanner.nextDouble();
                graph.putEdge(from, to, weight);
            }
            for (int i = 0; i < vertexesCount; i++) {
                Map<Object, Double> distances = graph.calculateDistancesFrom(i);
                graph.sortByDistanceFrom(i);
                Object prev = null;
                Object cur;
                for (Object vertex : graph.vertexes()) {
                    cur = vertex;
                    if (prev != null) {
                        assertTrue(distances.get(prev) <= distances.get(cur));
                    }
                    prev = cur;
                }
            }
        }
    }
}
