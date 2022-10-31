package ru.nsu.cjvth.graph;

class IncidenceMatrixGraphTest implements GraphTest {

    @Override
    public Graph<Object, Integer> createGraph() {
        return new IncidenceMatrixGraph<>();
    }
}
