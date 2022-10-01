package ru.nsu.cjvth.stack;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Stack of any non-primitive type. You can add and get one or multiple elements at once.
 *
 * @param <E> Type of elements
 */
public class Stack<E> {

    private E[] array;
    private int size;

    /**
     * Initialize empty stack.
     */
    public Stack() {
        size = 0;
        //noinspection unchecked
        array = (E[]) new Object[8];
    }

    /**
     * Initialize stack with first items.
     *
     * @param contents Array of values to be put in stack
     */
    public Stack(E[] contents) {
        size = contents.length;
        array = Arrays.copyOf(contents, size * 2);
    }


    /**
     * Initialize stack with getting first items from anotherStack.
     *
     * @param otherStack Another stack that with values to be put the new stack
     */
    public Stack(Stack<E> otherStack) {
        size = otherStack.size;
        array = Arrays.copyOf(otherStack.array, size > 3 ? size * 2 : 8);
    }

    public int count() {
        return size;
    }

    /**
     * Get elements from stack as an array.
     *
     * @param className Name of the class of the stack's elements e.g. `Integer.class`. Needed
     *                  because of stupid generics
     * @return Array with elements from stack. First element is the bottom of the stack
     */
    public E[] asArray(Class<E> className) {
        @SuppressWarnings("unchecked") E[] newArray = (E[]) Array.newInstance(className, size);
        System.arraycopy(array, 0, newArray, 0, size);
        return newArray;
    }

    /**
     * Add one item on top of the stack.
     *
     * @param item Item to be added to the stack
     */
    public void push(E item) {
        if (size >= array.length) {
            array = Arrays.copyOf(array, size * 2);
        }
        array[size++] = item;
    }

    /**
     * Pushes items from another stack on top of this stack.
     *
     * @param stack Stack with items to be added
     */
    public void pushStack(Stack<E> stack) {
        if (size + stack.size >= array.length) {
            array = Arrays.copyOf(array, (size + stack.size) * 2);
        }
        System.arraycopy(stack.array, 0, array, size, stack.size);
        size += stack.size;
    }


    private void reduceCapacity() {
        if (size * 3 < array.length && size > 3) {
            array = Arrays.copyOf(array, size * 2);
        }
    }

    /**
     * Removes and returns one element from top of the stack.
     *
     * @return element from top of the stack
     */
    public E pop() {
        E ret = array[--size];
        reduceCapacity();
        return ret;
    }

    /**
     * Move elements from top of the stack to a new stack and return it.
     *
     * @param n number of elements to be moved
     * @return the new stack
     */
    public Stack<E> popStack(int n) {
        @SuppressWarnings("unchecked") E[] a = (E[]) new Object[n];
        System.arraycopy(array, size - n, a, 0, n);
        size -= n;
        reduceCapacity();
        return new Stack<>(a);
    }
}
