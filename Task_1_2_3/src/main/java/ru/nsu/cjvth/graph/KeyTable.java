package ru.nsu.cjvth.graph;

import java.util.HashMap;
import java.util.Map;

class KeyTable<T> {

    private final Map<Object, Map<Object, T>> map;

    KeyTable() {
        map = new HashMap<>();
    }

    void addRow(Object rowName) {
        if (!map.containsKey(rowName)) {
            map.put(rowName, new HashMap<>());
        }
    }

    void removeRow(Object rowName) {
        map.remove(rowName);
    }

    void removeCol(Object colName) {
        map.forEach((key, value) -> value.remove(colName));
    }

    void set(Object row, Object col, T value) {
        map.get(row).put(col, value);
    }

    T get(Object row, Object col) {
        return map.get(row).get(col);
    }
}