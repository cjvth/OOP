package ru.nsu.cjvth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TreeTest {
    @Test
    void testConstructor() {
        Tree<Integer> a = new Tree<>(10);
        assertEquals(10, a.value);
    }
}