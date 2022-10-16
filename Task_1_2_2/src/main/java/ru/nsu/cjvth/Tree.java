package ru.nsu.cjvth;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tree data structure. Supports adding and removing child nodes, iterating. Descendants are
 * accessible through references. Every node has a value.
 *
 * @param <E> type of the elements
 */
public class Tree<E> {

    private final LinkedList<Tree<E>> children;
    public E nodeValue;
    /**
     * Tree traverse algorithm used in iterator.
     */
    public IterationMethod iterationMethod;
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

    private void modified() {
        modifyCount++;
        if (parent != null) {
            parent.modified();
        }
    }

    /**
     * Iterate by children subtrees.
     *
     * @return iterator of the children list
     */
    public List<Tree<E>> childrenList() {
        //noinspection unchecked
        return (List<Tree<E>>) children.clone();
    }

    /**
     * Add another tree as a child node.
     *
     * @param anotherTree tree to be added, must be a root
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
     * Add element as a new child subtree.
     *
     * @param value element to be added
     */
    public void addElement(E value) {
        modified();
        Tree<E> subtree = new Tree<>(value);
        subtree.parent = this;
        children.add(subtree);
    }


    /**
     * Get the root ancestor of the node.
     *
     * @return the root
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
     *
     * @param descendant the subtree to be removed. Must be a descendant of the caller tree node
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
     * Search method for iterating the tree: Breadth-First and Depth-First.
     */
    public enum IterationMethod {
        BFS,
        DFS
    }
}