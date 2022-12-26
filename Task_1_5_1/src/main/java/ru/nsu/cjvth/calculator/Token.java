package ru.nsu.cjvth.calculator;

import java.util.Iterator;

/**
 * Interface for both Number and Operation to let them be put in stack.
 */
public interface Token {

    /**
     * Convert operation or number into a number, recursively parsing arguments if there are some.
     *
     * @param iterator iterator in an iterable of some prefix notation. Must be in state where last
     *                 .next() method call returned exactly this token
     * @return same number or evaluation for operation and its artuments
     */
    Number apply(Iterator<Token> iterator);
}
