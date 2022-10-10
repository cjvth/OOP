package ru.nsu.cjvth;

/**
 * Tree data structure. Supports adding and removing child nodes, iterating. Descendants are
 * accessible through references. Every node has a value.
 *
 * @param <E> type of the elements
 */
public class Tree<E> {

    public E value;
    private Tree<E> parent;
    private SearchMethod searchMethod;

    /**
     * Create a tree with the root only.
     *
     * @param rootValue value of the root node
     */
    public Tree(E rootValue) {
        value = rootValue;
        parent = null;
    }

    /**
     * Search method for iterating the tree: Breadth-First and Depth-First.
     */
    public enum SearchMethod {
        BFS, DFS
    }


}