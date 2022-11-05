package ru.nsu.cjvth.graph;

import java.util.HashMap;
import java.util.Map;

class KeyTable<T> {

    private final Map<Object, Map<Object, T>> map;

    public KeyTable() {
        map = new HashMap<>();
    }

    public void addRow(Object rowName) {
        if (!map.containsKey(rowName)) {
            map.put(rowName, new HashMap<>());
        }
    }

    public void removeRow(Object rowName) {
        map.remove(rowName);
    }

    public void removeCol(Object colName) {
        map.forEach((key, value) -> value.remove(colName));
    }

    public void set(Object row, Object col, T value) {
        map.get(row).put(col, value);
    }

    public T get(Object row, Object col) {
        return map.get(row).get(col);
    }
}
