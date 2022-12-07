package ru.nsu.cjvth.calculator;

public class Number {

    private final double real;

    public Number(String s) {
        real = Double.parseDouble(s);
    }

    public static boolean isValid(String s) {
        return s.matches("-?\\d+(\\.\\d+)?");
    }
}
