package ru.nsu.cjvth.stack;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StackTest {

    @Test
    void testDefaultConstructor() {
        Stack<Integer> s = new Stack<>();
        assertArrayEquals(new Integer[]{}, s.asArray());
        Integer[] i = s.asArray();
        assertEquals(0, i.length);
    }

    @Test
    void testArrayConstructor() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        Stack<Integer> s = new Stack<>(arr);
        assertArrayEquals(arr, s.asArray());
    }

    @Test
    void testEmptyArrayConstructor() {
        Integer[] arr = new Integer[]{};
        Stack<Integer> s = new Stack<>(arr);
        assertArrayEquals(arr, s.asArray());
    }

    @Test
    void testOtherStackConstructor() {
        Stack<Integer> s1 = new Stack<>(new Integer[]{1, 2, 3, 4});
        Stack<Integer> s2 = new Stack<>(s1);
        assertArrayEquals(s1.asArray(), s2.asArray());
    }

    @Test
    void testEmptyOtherStackConstructor() {
        Stack<Integer> s1 = new Stack<>(new Integer[]{});
        Stack<Integer> s2 = new Stack<>(s1);
        assertEquals(0, s2.asArray().length);
    }


    @Test
    void testNullArrayConstructor() {
        Integer[] i = null;
        assertThrows(NullPointerException.class, () -> new Stack<Integer>(i));
    }

    @Test
    void testNullStackConstructor() {
        Stack<Integer> s2 = null;
        assertThrows(NullPointerException.class, () -> new Stack<Integer>(s2));
    }

    @Test
    void testPushNull() {
        Stack<Integer> s = new Stack<>();
        Integer i = null;
        assertDoesNotThrow(() -> s.push(i));
    }

    @Test
    void testPushNullArray() {
        Stack<Integer> s = new Stack<>();
        Integer[] i = null;
        assertThrows(NullPointerException.class, () -> s.pushArray(i));
    }

    @Test
    void testPushNullStack() {
        Stack<Integer> s = new Stack<>();
        Stack<Integer> s2 = null;
        assertThrows(NullPointerException.class, () -> s.pushStack(s2));
    }
}