package ru.nsu.cjvth.calculator;

import java.lang.Math;
import java.util.Iterator;
import java.util.List;

/**
 * Class for handling string-represented mathematical operations.
 */
public class Operation implements Token {

    private static final List<String> possibleOperations = List.of("sin", "cos", "log", "ln",
        "sqrt", "+", "-", "/", "*", "pow", "^", "**", "from_deg", "to_deg", "pi", "e");

    private final String mnemonic;

    /**
     * Constructor.
     *
     * @param mnemonic string with operation name, must be isValid(mnemonic) = true
     */
    public Operation(String mnemonic) {
        if (!isValid(mnemonic)) {
            throw new IllegalArgumentException("Operation has invalid mnemonic: " + mnemonic);
        }
        this.mnemonic = mnemonic;
    }

    /**
     * Checks if mnemonic is in supported operations: ["sin", "cos", "log" / "ln" "sqrt", "+", "-",
     * "/", "*", "pow" / "^" / "**", "from_deg" (from degrees to radians), "to_deg"(from radians to
     * degrees); or "pi" and "e" constants].
     *
     * @param mnemonic name of the operation
     * @return whether mnemonic is valid
     */
    public static boolean isValid(String mnemonic) {
        return possibleOperations.contains(mnemonic);
    }

    @Override
    public Number apply(Iterator<Token> iterator) {
        Number arg;
        Number arg2;
        //noinspection EnhancedSwitchMigration
        switch (mnemonic) {
            case "sin":
                arg = iterator.next().apply(iterator);
                return new Number(Math.sin(arg.real()));
            case "cos":
                arg = iterator.next().apply(iterator);
                return new Number(Math.cos(arg.real()));
            case "log", "ln":
                arg = iterator.next().apply(iterator);
                return new Number(Math.log(arg.real()));
            case "sqrt":
                arg = iterator.next().apply(iterator);
                return new Number(Math.sqrt(arg.real()));
            case "pow", "**", "^":
                arg = iterator.next().apply(iterator);
                arg2 = iterator.next().apply(iterator);
                return new Number(Math.pow(arg.real(), arg2.real()));
            case "+":
                arg = iterator.next().apply(iterator);
                arg2 = iterator.next().apply(iterator);
                return new Number(arg.real() + arg2.real());
            case "-":
                arg = iterator.next().apply(iterator);
                arg2 = iterator.next().apply(iterator);
                return new Number(arg.real() - arg2.real());
            case "/":
                arg = iterator.next().apply(iterator);
                arg2 = iterator.next().apply(iterator);
                return new Number(arg.real() / arg2.real());
            case "*":
                arg = iterator.next().apply(iterator);
                arg2 = iterator.next().apply(iterator);
                return new Number(arg.real() * arg2.real());
            case "from_deg":
                arg = iterator.next().apply(iterator);
                return new Number(Math.toRadians(arg.real()));
            case "to_deg":
                arg = iterator.next().apply(iterator);
                return new Number(Math.toDegrees(arg.real()));
            case "pi":
                return new Number(Math.PI);
            case "e":
                return new Number(Math.E);
            default:
                return null;
        }
    }
}
