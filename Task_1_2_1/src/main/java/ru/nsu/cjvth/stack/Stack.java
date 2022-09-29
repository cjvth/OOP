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
        array = Arrays.copyOf(otherStack.array, size * 2);
    }

    public int size() {
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
        if (size == array.length) {
            array = Arrays.copyOf(array, size * 3 / 2);
        }
        array[size++] = item;
    }

    //    /**
    //     * Pushes multiple items on top of the stack.
    //     *
    //     * @param items Array of items to be added to the stack, first items are pushed first
    //     */
    //    public void pushMultiple(E[] items) {
    //        if (size + items.length > array.length) {
    //            array = Arrays.copyOf(array, (size + items.length) * 3 / 2);
    //        }
    //        System.arraycopy(items, 0, array, size, items.length);
    //        size += items.length;
    //    }

    /**
     * Pushes items from another stack on top of this stack.
     *
     * @param stack Stack with items to be added
     */
    public void pushStack(Stack<E> stack) {
        if (size + stack.size > array.length) {
            array = Arrays.copyOf(array, (size + stack.size) * 3 / 2);
        }
        System.arraycopy(stack.array, 0, array, size, stack.size);
        size += stack.size;
    }
}
