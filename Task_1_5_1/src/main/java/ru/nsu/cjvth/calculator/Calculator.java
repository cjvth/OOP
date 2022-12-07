package ru.nsu.cjvth.calculator;

import java.util.Map;
import java.util.function.BiFunction;

public class Calculator {

    private static final Map<String, BiFunction<Number, Number, Number>> biFunctions = Map.of(
        "+", null,
        "-", null,
        "*", null,
        "/", null,
        "sin", null,
        "cos", null,
        "log", null,
        "pow", null
    );

    public static void main(String[] args) {
        for (String arg : args) {
            if (!biFunctions.containsKey(arg) || !Number.isValid(arg)) {
                throw new IllegalArgumentException("Bad cli arguments");
            }
        }
    }
}