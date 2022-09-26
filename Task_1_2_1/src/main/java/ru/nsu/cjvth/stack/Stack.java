package ru.nsu.cjvth.stack;

import java.util.Arrays;

/**
 * Stack of any non-primitive type. You can add and get one or multiple elements at once.
 *
 * @param <T> Type of elements
 */
public class Stack<T> {

    private T[] array;
    private int size;

    /**
     * Initialize empty stack.
     */
    public Stack() {
        size = 0;
        //noinspection unchecked
        array = (T[]) new Object[8];
    }

    /**
     * Initialize stack with first items.
     *
     * @param contents Array of values to be put in stack
     */
    public Stack(T[] contents) {
        size = contents.length;
        array = Arrays.copyOf(contents, size * 2);
    }


    /**
     * Initialize stack with getting first items from anotherStack.
     *
     * @param otherStack Another stack that with values to be put the new stack
     */
    public Stack(Stack<T> otherStack) {
        size = otherStack.size;
        array = Arrays.copyOf(otherStack.array, size * 2);
    }

    public int size() {
        return size;
    }

    public T[] asArray() {
        return Arrays.copyOf(array, size);
    }

    /**
     * Add one item on top of the stack.
     *
     * @param item Item to be added to the stack
     */
    public void push(T item) {
        if (size == array.length) {
            array = Arrays.copyOf(array, size * 2);
        }
        array[size++] = item;
    }

    /**
     * Adds multiple items on top of the stack.
     *
     * @param items Array of items to be added to the stack
     */
    public void pushArray(T[] items) {
        if (size + items.length > array.length) {
            array = Arrays.copyOf(array, (size + items.length) * 2);
        }
        System.arraycopy(items, 0, array, size, items.length);
        size += items.length;
    }

    /**
     * Add items from another stack on top of this stack.
     *
     * @param stack Stack with items to be added
     */
    public void pushStack(Stack<T> stack) {
        pushArray(stack.array);
    }

}
