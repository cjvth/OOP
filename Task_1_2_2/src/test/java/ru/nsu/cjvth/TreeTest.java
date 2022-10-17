package ru.nsu.cjvth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import ru.nsu.cjvth.Tree.IterationMethod;

class TreeTest {

    @Test
    void testConstructor() {
        Tree<Integer> t = new Tree<>(10);
        assertEquals(10, t.getNodeValue());
        assertEquals(IterationMethod.DFS, t.getIterationMethod());
    }

    @Test
    void testConstructorIterationMethod() {
        Tree<Integer> t = new Tree<>(10, IterationMethod.BFS);
        assertEquals(10, t.getNodeValue());
        assertEquals(IterationMethod.BFS, t.getIterationMethod());
    }

    @Test
    void testConstructorChildren() {
        List<Tree<Integer>> list = List.of(new Tree<>(1), new Tree<>(2));
        Tree<Integer> t = new Tree<>(0, list);
        assertIterableEquals(list, t.childrenList());
        assertEquals(IterationMethod.DFS, t.getIterationMethod());
    }

    @Test
    void testConstructorChildrenEmpty() {
        Tree<Integer> t = new Tree<>(0, new ArrayList<>());
        assertEquals(0, t.childrenList().size());
    }

    @Test
    void testConstructorChildrenNullCollection() {
        assertThrows(NullPointerException.class,
            () -> new Tree<>(0, (List<Tree<Integer>>) null));
    }

    @Test
    void testConstructorChildrenNullElements() {
        List<Tree<Integer>> l = new ArrayList<>();
        l.add(new Tree<>(1));
        l.add(null);
        l.add(new Tree<>(3));
        assertThrows(NullPointerException.class, () -> new Tree<>(0, l));
    }

    @Test
    void testConstructorChildrenIterationMethod() {
        List<Tree<Integer>> list = new ArrayList<>();
        Tree<Integer> t = new Tree<>(0, list, IterationMethod.BFS);
        assertIterableEquals(list, t.childrenList());
        assertEquals(IterationMethod.BFS, t.getIterationMethod());
    }

    @Test
    void testSetIterationMethod() {
        Tree<Integer> t = new Tree<>(0, IterationMethod.BFS);
        assertEquals(IterationMethod.BFS, t.getIterationMethod());
        t.setIterationMethod(IterationMethod.DFS);
        assertEquals(IterationMethod.DFS, t.getIterationMethod());
    }

    @Test
    void testAddSubtree() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = new Tree<>(1);
        Tree<Integer> t2 = new Tree<>(2);
        t.addSubtree(t1);
        t.addSubtree(t2);
        List<Tree<Integer>> l = t.childrenList();
        assertEquals(2, l.size());
        assertEquals(t1, l.get(0));
        assertEquals(t2, l.get(1));
    }

    @Test
    void testAddSubtreeNull() {
        Tree<Integer> t = new Tree<>(0);
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> t.addSubtree(null));
    }

    @Test
    void testAddSubtreeNonRoot() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = new Tree<>(1);
        Tree<Integer> t2 = new Tree<>(2);
        t1.addSubtree(t2);
        assertThrows(IllegalArgumentException.class, () -> t.addSubtree(t2));
    }

    @Test
    void testAddElement() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(2);
        assertEquals(2, t1.getNodeValue());
        assertEquals(1, t.childrenList().size());
        assertEquals(2, t.childrenList().get(0).getNodeValue());
    }

    @Test
    void testAddElementNull() {
        Tree<Integer> t = new Tree<>(0);
        assertDoesNotThrow(() -> t.addElement(null));
        assertEquals(1, t.childrenList().size());
        assertNull(t.childrenList().get(0).getNodeValue());
    }

    @Test
    void testFindRoot() {
        final Tree<Integer> t = new Tree<>(0);
        final Tree<Integer> t1 = new Tree<>(1);
        final Tree<Integer> t2 = new Tree<>(2);
        final Tree<Integer> t3 = new Tree<>(3);
        final Tree<Integer> t4 = new Tree<>(4);
        t.addSubtree(t1);
        t.addSubtree(t2);
        t2.addSubtree(t3);
        t.addSubtree(t4);
        assertEquals(t, t3.findRoot());
    }

    @Test
    void testRemoveSubtree() {
        final Tree<Integer> t = new Tree<>(0);
        final Tree<Integer> t1 = new Tree<>(1);
        final Tree<Integer> t2 = new Tree<>(2);
        final Tree<Integer> t3 = new Tree<>(3);
        final Tree<Integer> t4 = new Tree<>(4);
        final Tree<Integer> t5 = new Tree<>(5);
        t.addSubtree(t1);
        t.addSubtree(t2);
        t2.addSubtree(t3);
        t.addSubtree(t4);
        t3.addSubtree(t5);
        t.removeSubtree(t3);
        assertIterableEquals(List.of(t1, t2, t4), t.childrenList());
        assertIterableEquals(List.of(), t2.childrenList());
        assertEquals(t3, t3.findRoot());
    }

    @Test
    void testRemoveSubtreeNull() {
        Tree<Integer> t = new Tree<>(0);
        assertThrows(NullPointerException.class, () -> t.removeSubtree(null));
    }

    @Test
    void testRemoveSubtreeNonDescendant() {
        final Tree<Integer> t = new Tree<>(0);
        final Tree<Integer> t1 = new Tree<>(1);
        final Tree<Integer> t2 = new Tree<>(2);
        final Tree<Integer> t3 = new Tree<>(3);
        t.addSubtree(t1);
        t.addSubtree(t2);
        t2.addSubtree(t3);
        assertThrows(NoSuchElementException.class, () -> t2.removeSubtree(t1));
    }

    @Test
    void testRemoveElement() {
        final Tree<Integer> t = new Tree<>(0);
        final Tree<Integer> t1 = new Tree<>(1);
        final Tree<Integer> t2 = new Tree<>(2);
        final Tree<Integer> t3 = new Tree<>(3);
        final Tree<Integer> t4 = new Tree<>(4);
        final Tree<Integer> t5 = new Tree<>(5);
        final Tree<Integer> t6 = new Tree<>(6);
        t.addSubtree(t1);
        t.addSubtree(t2);
        t2.addSubtree(t3);
        t2.addSubtree(t4);
        t3.addSubtree(t5);
        t.addSubtree(t6);
        t.removeElement(t2);
        assertIterableEquals(List.of(t1, t3, t4, t6), t.childrenList());
        assertIterableEquals(List.of(t5), t3.childrenList());
        assertIterableEquals(List.of(), t2.childrenList());
        assertEquals(t2, t2.findRoot());
    }

    @Test
    void testRemoveElementNull() {
        Tree<Integer> t = new Tree<>(0);
        assertThrows(NullPointerException.class, () -> t.removeElement(null));
    }

    @Test
    void testRemoveElementNonDescendant() {
        final Tree<Integer> t = new Tree<>(0);
        final Tree<Integer> t1 = new Tree<>(1);
        final Tree<Integer> t2 = new Tree<>(2);
        final Tree<Integer> t3 = new Tree<>(3);
        t.addSubtree(t1);
        t.addSubtree(t2);
        t2.addSubtree(t3);
        assertThrows(NoSuchElementException.class, () -> t2.removeElement(t1));
    }
}