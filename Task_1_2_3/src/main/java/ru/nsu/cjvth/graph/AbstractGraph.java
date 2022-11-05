package ru.nsu.cjvth.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

abstract class AbstractGraph<N, V> implements Graph<N, V> {

    protected final List<N> vertexOrder = new LinkedList<>();

    protected abstract boolean bellmanFord(Map<N, Double> dist,
        Function<Double, Double> distHandler);

    @Override
    public Map<N, Double> calculateDistancesFrom(N selectedVertex) {
        Map<N, Double> dist = new HashMap<>();
        for (N v : vertexOrder) {
            dist.put(v, Double.POSITIVE_INFINITY);
        }
        dist.put(selectedVertex, 0.);
        boolean iter1 = bellmanFord(dist, (x) -> x);
        if (iter1) {
            bellmanFord(dist, (x) -> Double.NEGATIVE_INFINITY);
        }
        return dist;
    }

    @Override
    public void sortByDistanceFrom(N selectedVertex) {
        Map<N, Double> distances = calculateDistancesFrom(selectedVertex);
        var entries = new ArrayList<>(distances.entrySet());
        entries.sort(Entry.comparingByValue());
        for (int i = 0; i < entries.size(); i++) {
            vertexOrder.set(i, entries.get(i).getKey());
        }
    }
}
