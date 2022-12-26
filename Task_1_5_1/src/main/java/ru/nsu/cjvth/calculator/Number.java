package ru.nsu.cjvth.calculator;

import java.util.Iterator;

/**
 * Class that can parse and contain real number.
 */
public class Number implements Token {

    private final double real;

    /**
     * Constructor.
     *
     * @param number string representation of a number
     */
    public Number(String number) {
        real = Double.parseDouble(number);
    }

    /**
     * Constructor.
     *
     * @param real real part of the number
     */
    public Number(double real) {
        this.real = real;
    }

    /**
     * Get real part of a number.
     *
     * @return the real part
     */
    public double real() {
        return real;
    }

    /**
     * Check if string is a valid number.
     *
     * @param number string that may represent number
     * @return whether string is a valid number
     */
    public static boolean isValid(String number) {
        return number.matches("-?\\d+(\\.\\d+)?");
    }

    @Override
    public Number apply(Iterator<Token> iterator) {
        return this;
    }
}
