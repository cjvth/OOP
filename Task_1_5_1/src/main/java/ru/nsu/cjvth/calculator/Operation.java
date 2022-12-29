package ru.nsu.cjvth.calculator;

import java.util.Iterator;

/**
 * Class for handling string-represented mathematical operations.
 */
public abstract class Operation implements Token {

    public abstract Number calculate(Number... args);

    public abstract int arity();

    protected void assertArgs(Number... args) {
        if (args.length != arity()) {
            throw new IllegalArgumentException(
                "Number of arguments differs from the operation's arity");
        }
    }

    @Override
    public Number apply(Iterator<Token> iterator) {
        Number[] args = new Number[arity()];
        for (int i = 0; i < arity(); i++) {
            Token arg = iterator.next();
            args[i] = arg.apply(iterator);
        }
        return calculate(args);
    }
}
