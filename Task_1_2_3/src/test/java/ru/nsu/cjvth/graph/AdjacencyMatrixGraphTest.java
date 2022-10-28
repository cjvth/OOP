package ru.nsu.cjvth.graph;

class AdjacencyMatrixGraphTest implements
    GraphTest {

    @Override
    public Graph<Object, Integer> createGraph() {
        return new AdjacencyMatrixGraph<>();
    }
}
