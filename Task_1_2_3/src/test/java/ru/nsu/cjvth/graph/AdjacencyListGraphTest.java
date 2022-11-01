package ru.nsu.cjvth.graph;

class AdjacencyListGraphTest implements GraphTest {

    @Override
    public Graph<Object, Integer> createGraph() {
        return new AdjacencyListGraph<>();
    }
}
