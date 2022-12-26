package ru.nsu.cjvth.calculator;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Console program to calculate an expression in prefix notation. Illegal operations such as
 * division by zero are treated as Infinity or NaN
 */
public class Calculator {

    /**
     * Main function.
     *
     * @param args cli arguments provided by environment
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: no arguments provided");
            return;
        }
        for (String arg : args) {
            if (!isValidToken(arg)) {
                System.out.println("Error: invalid token: " + arg);
                return;
            }
        }

        var iterator = Arrays.stream(args).map(Calculator::parseToken).iterator();
        Token first = iterator.next();
        try {
            Number number = first.apply(iterator);
            if (iterator.hasNext()) {
                System.out.println("Error: more operands than operators needed");
            } else {
                System.out.println(number.real());
            }
        } catch (NoSuchElementException e) {
            System.out.println("Error: not enough operands");
        }
    }

    /**
     * Convert string to suitable token: number or operation.
     *
     * @param token string representation of token
     * @return token from given string
     */
    public static Token parseToken(String token) {
        if (Number.isValid(token)) {
            return new Number(token);
        } else if (Operation.isValid(token)) {
            return new Operation(token);
        } else {
            return null;
        }
    }

    /**
     * Checks that string represents a valid token.
     *
     * @param token string to check
     * @return whether it is a valid token
     */
    public static boolean isValidToken(String token) {
        return Number.isValid(token) || Operation.isValid(token);
    }
}