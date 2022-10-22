package ru.nsu.cjvth.graph;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class KeyTableTest {
    @Test
    void testSimple() {
        KeyTable<Integer> kt = new KeyTable<>();
        kt.addRow(1);
        kt.addRow(2);
        kt.set(1, 1, 1);
        kt.set(2, 3, 6);
        kt.set(2, 2, 4);

        assertEquals(1, kt.get(1, 1));
        assertNull(kt.get(1, 2));
        assertNull(kt.get(1, 3));
        assertNull(kt.get(2, 1));
        assertEquals(4, kt.get(2, 2));
        assertEquals(6, kt.get(2, 3));
    }

    @Test
    void testRemove() {
        KeyTable<Integer> kt = new KeyTable<>();
        kt.addRow(1);
        kt.addRow(3);
        kt.addRow(2);
        kt.set(1, 1, 1);
        kt.set(1, 2, 2);
        kt.set(3, 1, 3);
        kt.set(3, 2, 6);
        kt.set(2, 3, 6);
        kt.set(2, 2, 4);

        kt.removeCol(2);
        kt.removeRow(3);

        assertEquals(1, kt.get(1, 1));
        assertNull(kt.get(1, 2));
        assertNull(kt.get(1, 3));
        assertThrows(NullPointerException.class, () -> kt.get(3, 1));
        assertNull(kt.get(2, 1));
        assertNull(kt.get(2, 2));
        assertEquals(6, kt.get(2, 3));
        kt.removeCol(1);
        kt.removeRow(1);
    }

    @Test
    void testDifferentTypes() {
        KeyTable<Integer> kt = new KeyTable<>();
        kt.addRow("A");
        KeyTable<Object> row = new KeyTable<>();
        kt.addRow(row);
        //noinspection StringOperationCanBeSimplified
        assertDoesNotThrow(() -> kt.set(new String("A"), 1, 0));
        assertDoesNotThrow(() -> kt.set("A", 2, 0));
        assertDoesNotThrow(() -> kt.set(row, 2, 0));
        kt.removeCol(2);
        assertEquals(0, kt.get("A", 1));
    }
}