package ru.nsu.cjvth.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class DifferentVertexNameTypesGraphTest {

    @Test
    void testObjectIteration() {
        Graph<Object, Integer> graph = new AdjacencyMatrixGraph<>();
        graph.putVertex("1", 1);
        graph.putVertex("3", 3);
        graph.putVertex("2", 2);
        assertIterableEquals(List.of("1", "3", "2"), graph.vertexes());
    }

    @Test
    void testStringList() {
        Graph<String, Integer> graph = new AdjacencyMatrixGraph<>();
        graph.putVertex("1", 1);
        graph.putVertex("3", 3);
        graph.putVertex("2", 2);
        List<String> l = graph.vertexes();
        System.out.println(l);
    }

    @Test
    void testObjectString() {
        Graph<String, Integer> graph = new AdjacencyMatrixGraph<>();
        graph.putVertex("1", 1);
        graph.putVertex("3", 3);
        graph.putVertex("2", 2);
        assertIterableEquals(List.of("1", "3", "2"), graph.vertexes());
    }

    @Test
    void testIteratingObjects() {
        Graph<Object, Integer> graph = new AdjacencyMatrixGraph<>();
        graph.putVertex("1", 1);
        graph.putVertex("3", 3);
        graph.putVertex("2", 2);
        for (Object i : graph.vertexes()) {
            String j = (String) i;
            assertEquals(Integer.parseInt(j), graph.getVertexValue(j));
        }
    }

    @Test
    void testDifferentTypesNames() {
        Graph<Object, Integer> graph = new AdjacencyMatrixGraph<>();
        graph.putVertex("1", 1);
        graph.putVertex(2, 2);
        graph.putVertex(3., 3);
        int count = 0;
        for (Object i : graph.vertexes()) {
            if (i == "1") {
                assertEquals(1, graph.getVertexValue(i));
                count++;
            } else if (i == (Integer) 2) {
                assertEquals(2, graph.getVertexValue(i));
                count++;
            } else if ((Double) i == 3.) {
                assertEquals(3, graph.getVertexValue(i));
                count++;
            }
        }
        assertEquals(3, graph.vertexes().size());
        assertEquals(3, count);
    }
}
