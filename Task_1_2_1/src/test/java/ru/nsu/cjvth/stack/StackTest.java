package ru.nsu.cjvth.stack;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


    @Test
    void testCount() {
        Stack<Integer> s = new Stack<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        s.push(9);
        s.push(10);
        s.pushStack(new Stack<>(new Integer[]{11, 12, 13, 14}));
        assertEquals(14, s.count());
    }


    @Test
    void testPop() {
        Short[] a = new Short[10000];
        for (short i = 0; i < 10000; i++) {
            a[i] = i;
        }
        Stack<Short> s = new Stack<>(a);
        for (short i = 9999; i >= 0; i--) {
            assertEquals(i, s.pop());
        }
        assertEquals(0, s.count());
    }

    @Test
    void testPopEmpty() {
        Short[] a = new Short[100];
        for (short i = 0; i < 100; i++) {
            a[i] = i;
        }
        Stack<Short> s = new Stack<>(a);
        for (short i = 99; i >= 0; i--) {
            assertDoesNotThrow(s::pop);
        }
        assertEquals(0, s.count());
        assertThrows(IndexOutOfBoundsException.class, s::pop);
    }

    @Test
    void testPopStack() {
        Short[] a = new Short[10000];
        for (short i = 0; i < 10000; i++) {
            a[i] = i;
        }
        Stack<Short> s = new Stack<>(a);
        for (short i = 9999; i >= 0; ) {
            Stack<Short> s1 = s.popStack(100);
            for (int j = 99; j >= 0; i--, j--) {
                assertEquals(i, s1.pop());
            }
        }
    }

    @Test
    void testPopStackNotEnough() {
        Short[] a = new Short[150];
        for (short i = 0; i < 150; i++) {
            a[i] = i;
        }
        Stack<Short> s = new Stack<>(a);
        for (int i = 0; i < 150 - 20; i += 20) {
            s.popStack(20);
        }
        assertTrue(s.count() > 0 && s.count() < 20);
        assertThrows(IndexOutOfBoundsException.class, () -> s.popStack(20));
    }

    @Test
    void testPopStackExactlySame() {
        Stack<Integer> s = new Stack<>(new Integer[]{1, 2, 3});
        int n = s.count();
        Stack<Integer> s1 = s.popStack(n);
        assertEquals(n, s1.count());
        assertEquals(0, s.count());
    }

    @Test
    void testPopStackZero() {
        Stack<Integer> s = new Stack<>();
        s.push(1);
        s.push(2);
        Stack<Integer> s1 = s.popStack(0);
        assertEquals(0, s1.count());
        assertEquals(2, s.count());
    }

    @Test
    void testPopStackZeroZero() {
        Stack<Integer> s = new Stack<>();
        Stack<Integer> s1 = s.popStack(0);
        assertEquals(0, s1.count());
        assertEquals(0, s.count());
    }

    @Test
    void testPopStackNegative() {
        Stack<Integer> s = new Stack<>(new Integer[]{1, 2, 3});
        assertThrows(NegativeArraySizeException.class, () -> s.popStack(-1));
    }
}