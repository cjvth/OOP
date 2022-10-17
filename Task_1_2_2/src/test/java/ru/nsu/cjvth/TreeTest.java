package ru.nsu.cjvth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = new Tree<>(1);
        Tree<Integer> t2 = new Tree<>(2);
        Tree<Integer> t3 = new Tree<>(3);
        Tree<Integer> t4 = new Tree<>(4);
        t.addSubtree(t1);
        t.addSubtree(t2);
        t2.addSubtree(t3);
        t.addSubtree(t4);
        assertEquals(t, t3.findRoot());
    }

    @Test
    void testRemoveSubtree() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = new Tree<>(1);
        Tree<Integer> t2 = new Tree<>(2);
        Tree<Integer> t3 = new Tree<>(3);
        Tree<Integer> t4 = new Tree<>(4);
        Tree<Integer> t5 = new Tree<>(5);
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
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = new Tree<>(1);
        Tree<Integer> t2 = new Tree<>(2);
        Tree<Integer> t3 = new Tree<>(3);
        t.addSubtree(t1);
        t.addSubtree(t2);
        t2.addSubtree(t3);
        assertThrows(NoSuchElementException.class, () -> t2.removeSubtree(t1));
    }

    @Test
    void testRemoveElement() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = new Tree<>(1);
        Tree<Integer> t2 = new Tree<>(2);
        Tree<Integer> t3 = new Tree<>(3);
        Tree<Integer> t4 = new Tree<>(4);
        Tree<Integer> t5 = new Tree<>(5);
        Tree<Integer> t6 = new Tree<>(6);
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
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = new Tree<>(1);
        Tree<Integer> t2 = new Tree<>(2);
        Tree<Integer> t3 = new Tree<>(3);
        t.addSubtree(t1);
        t.addSubtree(t2);
        t2.addSubtree(t3);
        assertThrows(NoSuchElementException.class, () -> t2.removeElement(t1));
    }

    @Test
    void testDfsIterator() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        Tree<Integer> t2 = t1.addElement(2);
        Tree<Integer> t3 = t1.addElement(3);
        Tree<Integer> t4 = t2.addElement(4);
        Tree<Integer> t5 = t.addElement(5);
        Tree<Integer> t6 = t.addElement(6);
        Tree<Integer> t7 = t6.addElement(7);
        Tree<Integer> t8 = t7.addElement(8);
        Tree<Integer> t9 = t6.addElement(9);
        assertIterableEquals(List.of(t6, t7, t8, t9), t6);
        assertIterableEquals(List.of(t, t1, t2, t4, t3, t5, t6, t7, t8, t9), t);
    }

    @Test
    void testDfsIteratorAddElementConcurrentModification() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        t1.addElement(2);
        Tree<Integer> t3 = t1.addElement(3);
        var iter = t.iterator();
        assertDoesNotThrow(iter::next);
        assertDoesNotThrow(iter::next);
        t3.addElement(4);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    @Test
    void testDfsIteratorAddSubtreeConcurrentModification() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        Tree<Integer> t2 = new Tree<>(2);
        var iter = t.iterator();
        t1.addSubtree(t2);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    @Test
    void testDfsIteratorSetNodeValueConcurrentModification() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        var iter = t.iterator();
        t1.setNodeValue(11);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    @Test
    void testDfsIteratorRemoveElementConcurrentModification() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        Tree<Integer> t2 = t1.addElement(2);
        t1.addElement(3);
        t2.addElement(4);
        var iter = t.iterator();
        assertDoesNotThrow(iter::next);
        t1.removeElement(t2);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    @Test
    void testDfsIteratorRemoveSubtreeConcurrentModification() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        Tree<Integer> t2 = t1.addElement(2);
        t1.addElement(3);
        t2.addElement(4);
        var iter = t.iterator();
        assertDoesNotThrow(iter::next);
        t1.removeSubtree(t2);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    @Test
    void testDfsIteratorChildrenListConcurrentModification() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        t.addElement(2);
        t1.addElement(3);
        t1.addElement(4);
        var iter = t.iterator();
        var children = t1.childrenList();
        assertDoesNotThrow(iter::next);
        assertEquals(2, children.size());
        children.remove(0);
        assertEquals(1, children.size());
        assertDoesNotThrow(iter::next);
        children.get(0).setNodeValue(-1);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    @Test
    // Used to test how iterator pops sub-iterators stack elements without hasNext()
    void testDfsIteratorNoHasNext() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        t1.addElement(2);
        t.addElement(3);
        for (var i : t) {
            System.out.println(i.getNodeValue());
        }
        var iter = t.iterator();
        assertEquals(0, iter.next().getNodeValue());
        assertEquals(1, iter.next().getNodeValue());
        assertEquals(2, iter.next().getNodeValue());
        assertEquals(3, iter.next().getNodeValue());
        assertThrows(NoSuchElementException.class, iter::next);
    }
}