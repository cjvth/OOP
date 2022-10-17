package ru.nsu.cjvth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import ru.nsu.cjvth.Tree.IterationMethod;

class TreeIteratorTest {

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
        testIteratorAddElementConcurrentModification(IterationMethod.DFS);
    }

    @Test
    void testDfsIteratorAddSubtreeConcurrentModification() {
        testIteratorAddSubtreeConcurrentModification(IterationMethod.DFS);
    }

    @Test
    void testDfsIteratorSetNodeValueConcurrentModification() {
        testIteratorSetNodeValueConcurrentModification(IterationMethod.DFS);
    }

    @Test
    void testDfsIteratorRemoveElementConcurrentModification() {
        testIteratorRemoveElementConcurrentModification(IterationMethod.DFS);
    }

    @Test
    void testDfsIteratorRemoveSubtreeConcurrentModification() {
        testIteratorRemoveSubtreeConcurrentModification(IterationMethod.DFS);
    }

    @Test
    void testDfsIteratorChildrenListConcurrentModification() {
        testIteratorChildrenListConcurrentModification(IterationMethod.DFS);
    }

    @Test
        // Used to test how iterator pops sub-iterators stack elements without hasNext()
    void testDfsIteratorNoHasNext() {
        Tree<Integer> t = new Tree<>(0);
        Tree<Integer> t1 = t.addElement(1);
        t1.addElement(2);
        t.addElement(3);
        var iter = t.iterator();
        assertEquals(0, iter.next().getNodeValue());
        assertEquals(1, iter.next().getNodeValue());
        assertEquals(2, iter.next().getNodeValue());
        assertEquals(3, iter.next().getNodeValue());
        assertThrows(NoSuchElementException.class, iter::next);
    }


    @Test
    void testBfsIterator() {
        Tree<Integer> t = new Tree<>(0, IterationMethod.BFS);
        Tree<Integer> t1 = t.addElement(1);
        Tree<Integer> t2 = t1.addElement(2);
        Tree<Integer> t3 = t1.addElement(3);
        Tree<Integer> t4 = t2.addElement(4);
        Tree<Integer> t5 = t.addElement(5);
        Tree<Integer> t6 = t.addElement(6);
        Tree<Integer> t7 = t6.addElement(7);
        Tree<Integer> t8 = t7.addElement(8);
        Tree<Integer> t9 = t6.addElement(9);
        assertIterableEquals(List.of(t6, t7, t9, t8), t6);
        assertIterableEquals(List.of(t, t1, t5, t6, t2, t3, t7, t9, t4, t8), t);
    }

    @Test
    void testBfsIteratorAddElementConcurrentModification() {
        testIteratorAddElementConcurrentModification(IterationMethod.BFS);
    }

    @Test
    void testBfsIteratorAddSubtreeConcurrentModification() {
        testIteratorAddSubtreeConcurrentModification(IterationMethod.BFS);
    }

    @Test
    void testBfsIteratorSetNodeValueConcurrentModification() {
        testIteratorSetNodeValueConcurrentModification(IterationMethod.BFS);
    }

    @Test
    void testBfsIteratorRemoveElementConcurrentModification() {
        testIteratorRemoveElementConcurrentModification(IterationMethod.BFS);
    }

    @Test
    void testBfsIteratorRemoveSubtreeConcurrentModification() {
        testIteratorRemoveSubtreeConcurrentModification(IterationMethod.BFS);
    }

    @Test
    void testBfsIteratorChildrenListConcurrentModification() {
        testIteratorChildrenListConcurrentModification(IterationMethod.BFS);
    }

    @Test
        // Used to test how iterator pops sub-iterators stack elements without hasNext()
    void testBfsIteratorNoHasNext() {
        Tree<Integer> t = new Tree<>(0, IterationMethod.BFS);
        Tree<Integer> t1 = t.addElement(1);
        t1.addElement(2);
        t.addElement(3);
        var iter = t.iterator();
        assertEquals(0, iter.next().getNodeValue());
        assertEquals(1, iter.next().getNodeValue());
        assertEquals(3, iter.next().getNodeValue());
        assertEquals(2, iter.next().getNodeValue());
        assertThrows(NoSuchElementException.class, iter::next);
    }


    void testIteratorAddElementConcurrentModification(IterationMethod im) {
        Tree<Integer> t = new Tree<>(0, im);
        Tree<Integer> t1 = t.addElement(1);
        t1.addElement(2);
        Tree<Integer> t3 = t1.addElement(3);
        var iter = t.iterator();
        assertDoesNotThrow(iter::next);
        assertDoesNotThrow(iter::next);
        t3.addElement(4);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    void testIteratorAddSubtreeConcurrentModification(IterationMethod im) {
        Tree<Integer> t = new Tree<>(0, im);
        Tree<Integer> t1 = t.addElement(1);
        Tree<Integer> t2 = new Tree<>(2, im);
        var iter = t.iterator();
        t1.addSubtree(t2);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    void testIteratorSetNodeValueConcurrentModification(IterationMethod im) {
        Tree<Integer> t = new Tree<>(0, im);
        Tree<Integer> t1 = t.addElement(1);
        var iter = t.iterator();
        t1.setNodeValue(11);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    void testIteratorRemoveElementConcurrentModification(IterationMethod im) {
        Tree<Integer> t = new Tree<>(0, im);
        Tree<Integer> t1 = t.addElement(1);
        Tree<Integer> t2 = t1.addElement(2);
        t1.addElement(3);
        t2.addElement(4);
        var iter = t.iterator();
        assertDoesNotThrow(iter::next);
        t1.removeElement(t2);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    void testIteratorRemoveSubtreeConcurrentModification(IterationMethod im) {
        Tree<Integer> t = new Tree<>(0, im);
        Tree<Integer> t1 = t.addElement(1);
        Tree<Integer> t2 = t1.addElement(2);
        t1.addElement(3);
        t2.addElement(4);
        var iter = t.iterator();
        assertDoesNotThrow(iter::next);
        t1.removeSubtree(t2);
        assertThrows(ConcurrentModificationException.class, iter::next);
    }

    void testIteratorChildrenListConcurrentModification(IterationMethod im) {
        Tree<Integer> t = new Tree<>(0, im);
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
}