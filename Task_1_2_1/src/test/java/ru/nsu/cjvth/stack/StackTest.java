package ru.nsu.cjvth.stack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class StackTest {

    @Test
    void testGenericsWorkWithArrays() {
        Stack<Integer> s = new Stack<>(new Integer[]{0, 1, 2, 3, 4});
        Integer[] a = new Integer[]{0, 1, 2, 3, 4};
        Integer[] b = s.asArray(Integer.class);
        assertArrayEquals(a, b);
        assertEquals(5, b.length);
        for (int i = 0; i < b.length; i++) {
            assertEquals(a[i], b[i]);
        }
    }

    @Test
    void testGenericsWorkWithEmptyArrays() {
        Stack<Integer> s = new Stack<>(new Integer[]{});
        Integer[] a = new Integer[]{};
        Integer[] b = s.asArray(Integer.class);
        assertArrayEquals(a, b);
        assertEquals(0, b.length);
    }


    @Test
    void testDefaultConstructor() {
        Stack<Integer> s = new Stack<>();
        assertArrayEquals(new Integer[]{}, s.asArray(Integer.class));
        assertEquals(0, s.asArray(Integer.class).length);
    }

    @Test
    void testArrayConstructor() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        Stack<Integer> s = new Stack<>(arr);
        assertArrayEquals(arr, s.asArray(Integer.class));
    }

    @Test
    void testEmptyArrayConstructor() {
        Integer[] arr = new Integer[]{};
        Stack<Integer> s = new Stack<>(arr);
        assertArrayEquals(arr, s.asArray(Integer.class));
    }

    @Test
    void testStackCopyConstructor() {
        Stack<Integer> s1 = new Stack<>(new Integer[]{1, 2, 3, 4});
        Stack<Integer> s2 = new Stack<>(s1);
        assertArrayEquals(s1.asArray(Integer.class), s2.asArray(Integer.class));
    }

    @Test
    void testEmptyStackCopyConstructor() {
        Stack<Integer> s1 = new Stack<>(new Integer[]{});
        Stack<Integer> s2 = new Stack<>(s1);
        assertEquals(0, s2.asArray(Integer.class).length);
    }


    @SuppressWarnings("ConstantConditions")
    @Test
    void testNullArrayConstructor() {
        Integer[] i = null;
        assertThrows(NullPointerException.class, () -> new Stack<>(i));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testNullStackConstructor() {
        Stack<Integer> s2 = null;
        assertThrows(NullPointerException.class, () -> new Stack<>(s2));
    }

    @Test
    void testPushNull() {
        Stack<Integer> s = new Stack<>();
        Integer i = null;
        assertDoesNotThrow(() -> s.push(i));
    }

    //    @SuppressWarnings("ConstantConditions")
    //    @Test
    //    void testPushNullArray() {
    //        Stack<Integer> s = new Stack<>();
    //        Integer[] i = null;
    //        assertThrows(NullPointerException.class, () -> s.pushMultiple(i));
    //    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testPushNullStack() {
        Stack<Integer> s = new Stack<>();
        Stack<Integer> s2 = null;
        assertThrows(NullPointerException.class, () -> s.pushStack(s2));
    }


    @Test
    void testPush() {
        Stack<Short> s = new Stack<>();
        for (short i = 0; i < 10000; i++) {
            s.push(i);
        }
        Short[] a = s.asArray(Short.class);
        assertEquals(10000, a.length);
        for (short i = 0; i < a.length; i++) {
            assertEquals(i, a[i]);
        }
    }

    //    @Test
    //    void testPushMultiple() {
    //
    //    }

    @Test
    void testPushStack() {
        Stack<Short> s = new Stack<>();
        for (short i = 0; i < 10000; ) {
            Short[] a = new Short[100];
            for (int j = 0; j < 100; i++, j++) {
                a[j] = i;
            }
            Stack<Short> s1 = new Stack<>(a);
            s.pushStack(s1);
        }
        Short[] a = s.asArray(Short.class);
        assertEquals(10000, a.length);
        for (short i = 0; i < a.length; i++) {
            assertEquals(i, a[i]);
        }
    }


}