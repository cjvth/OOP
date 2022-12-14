package ru.nsu.cjvth;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tree data structure. Supports adding and removing child nodes, iterating. Descendants are
 * accessible through references. Every node has a value.
 *
 * @param <E> type of the elements
 */
public class Tree<E> implements Iterable<Tree<E>> {

    private final List<Tree<E>> children;
    private E nodeValue;
    /**
     * Tree traverse algorithm used in iterator.
     */
    private IterationMethod iterationMethod;
    private long modifyCount = 0;
    private Tree<E> parent;


    /**
     * Create a tree with the root only. Default iteration method - DFS
     *
     * @param rootValue value of the root node
     */
    public Tree(E rootValue) {
        this(rootValue, IterationMethod.DFS);
    }

    /**
     * Create a tree with the root only.
     *
     * @param rootValue value of the root node
     * @param im        method of traversing the tree used in iterator
     */
    public Tree(E rootValue, IterationMethod im) {
        nodeValue = rootValue;
        parent = null;
        children = new LinkedList<>();
        iterationMethod = im;
    }

    /**
     * Create a tree with the root only. Default iteration method - DFS
     *
     * @param rootValue    value of the root node
     * @param rootChildren child subtrees of the root node
     */
    public Tree(E rootValue, Collection<Tree<E>> rootChildren) {
        this(rootValue, rootChildren, IterationMethod.DFS);
    }

    /**
     * Create a tree with the root only.
     *
     * @param rootValue    value of the root node
     * @param rootChildren child subtrees of the root node
     * @param im           method of traversing the tree used in iterator
     */
    public Tree(E rootValue, Collection<Tree<E>> rootChildren, IterationMethod im) {
        for (Tree<E> i : rootChildren) {
            if (i == null) {
                throw new NullPointerException();
            }
        }
        nodeValue = rootValue;
        parent = null;
        children = new LinkedList<>(rootChildren);
        iterationMethod = im;
    }

    public E getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(E nodeValue) {
        modified();
        this.nodeValue = nodeValue;
    }

    private void modified() {
        modifyCount++;
        if (parent != null) {
            parent.modified();
        }
    }

    /**
     * Return a list with child subtrees of the node.
     */
    public List<Tree<E>> childrenList() {
        return new ArrayList<>(children);
    }

    /**
     * Add another tree as a child node.
     */
    public void addSubtree(Tree<E> anotherTree) {
        if (anotherTree.parent != null) {
            throw new IllegalArgumentException("Subtree to add must be a root");
        }
        modified();
        children.add(anotherTree);
        anotherTree.parent = this;
    }

    /**
     * Add element as a new child subtree. The child subtree gets the same iteration method as
     * caller node
     *
     * @param value value of the created node
     */
    public Tree<E> addElement(E value) {
        modified();
        Tree<E> subtree = new Tree<>(value, iterationMethod);
        subtree.parent = this;
        children.add(subtree);
        return subtree;
    }


    /**
     * Return the root ancestor of the node.
     */
    public Tree<E> findRoot() {
        Tree<E> root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    /**
     * Remove a descendant subtree from the tree.
     */
    public void removeSubtree(Tree<E> descendant) {
        Tree<E> backSearch = descendant;
        while (backSearch.parent != this && backSearch.parent != null) {
            backSearch = backSearch.parent;
        }
        if (backSearch.parent != this) {
            throw new NoSuchElementException();
        } else {
            var iter = descendant.parent.children.iterator();
            while (iter.hasNext()) {
                Tree<E> elem = iter.next();
                if (elem == descendant) {
                    modified();
                    iter.remove();
                    descendant.parent = null;
                    return;
                }
            }
            throw new IllegalStateException();
        }
    }

    /**
     * Remove element of the node, but add its children as its parent's children.
     *
     * @param descendant the subtree, whose element is removed. Must be a descendant of the caller
     */
    public void removeElement(Tree<E> descendant) {
        Tree<E> backSearch = descendant;
        while (backSearch.parent != this && backSearch.parent != null) {
            backSearch = backSearch.parent;
        }
        if (backSearch.parent != this) {
            throw new NoSuchElementException();
        } else {
            var iter = descendant.parent.children.iterator();
            int index = 0;
            while (iter.hasNext()) {
                Tree<E> elem = iter.next();
                if (elem == descendant) {
                    descendant.parent.modified();
                    iter.remove();
                    for (Tree<E> i : descendant.children) {
                        descendant.parent.children.add(index, i);
                        i.parent = descendant.parent;
                        index++;
                    }
                    descendant.children.clear();
                    descendant.parent = null;
                    return;
                }
                index++;
            }
            throw new IllegalStateException();
        }
    }

    /**
     * Return iterator of the tree. You can change iteration method for the traversal
     */
    @Override
    public Iterator<Tree<E>> iterator() {
        if (iterationMethod == IterationMethod.DFS) {
            return new TreeIteratorDfs(this);
        } else {
            return new TreeIteratorBfs(this);
        }
    }

    public IterationMethod getIterationMethod() {
        return iterationMethod;
    }

    public void setIterationMethod(IterationMethod iterationMethod) {
        this.iterationMethod = iterationMethod;
    }

    /**
     * Search method for iterating the tree: Breadth-First and Depth-First.
     */
    public enum IterationMethod {
        BFS,
        DFS
    }

    private class TreeIteratorDfs implements Iterator<Tree<E>> {

        private final Deque<Iterator<Tree<E>>> stack;
        private final Tree<E> root;
        private final long savedModifyCount;

        public TreeIteratorDfs(Tree<E> tree) {
            this.root = tree;
            stack = new ArrayDeque<>();
            savedModifyCount = tree.modifyCount;
            stack.addLast(List.of(tree).iterator());
        }

        @Override
        public boolean hasNext() {
            while (!stack.getLast().hasNext() && stack.size() > 1) {
                stack.removeLast();
            }
            return stack.getLast().hasNext();
        }

        @Override
        public Tree<E> next() {
            if (root.modifyCount != savedModifyCount) {
                throw new ConcurrentModificationException();
            }
            while (!stack.getLast().hasNext() && stack.size() > 1) {
                stack.removeLast();
            }
            Tree<E> elem = stack.getLast().next();
            if (elem.children.size() > 0) {
                stack.addLast(elem.children.iterator());
            }
            return elem;
        }

    }

    private class TreeIteratorBfs implements Iterator<Tree<E>> {

        private final Deque<Iterator<Tree<E>>> queue;
        private final Tree<E> root;
        private final long savedModifyCount;

        private TreeIteratorBfs(Tree<E> tree) {
            this.root = tree;
            this.queue = new ArrayDeque<>();
            this.savedModifyCount = tree.modifyCount;
            queue.add(List.of(tree).iterator());
        }

        @Override
        public boolean hasNext() {
            while (!queue.getFirst().hasNext() && queue.size() > 1) {
                queue.pop();
            }
            return queue.getFirst().hasNext();
        }

        @Override
        public Tree<E> next() {
            if (root.modifyCount != savedModifyCount) {
                throw new ConcurrentModificationException();
            }
            while (!queue.getFirst().hasNext() && queue.size() > 1) {
                queue.pop();
            }
            Tree<E> elem = queue.getFirst().next();
            if (elem.children.size() > 0) {
                queue.add(elem.children.iterator());
            }
            return elem;
        }
    }
}