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

    private static Number pow(Number a, Number b) {
        //a^b = e^log_e(r)(c+id)+iθ(c+id)
        double r = Math.sqrt(a.real() * a.real() + a.im() * a.im());
        double phi = Math.atan2(a.im(), a.real());
        double powerReal = Math.log(r) * b.real() - phi * b.im();
        double powerIm = Math.log(r) * b.im() + phi * b.real();
        double nr = Math.exp(powerReal);
        double real = nr * Math.cos(powerIm);
        double im = nr * Math.sin(powerIm);
        return new Number(real, im);
    }

    private static Number div(Number a, Number b) {
        return new Number(
            (a.real() * b.real() + a.im() * b.im()) / (b.real() * b.real()
                + b.im() * b.im()),
            (a.im() * b.real() - a.real() * b.im()) / (b.real() * b.real()
                + b.im() * b.im()));
    }

    @Override
    public Number apply(Iterator<Token> iterator) {
        Number a;
        Number b;
        //noinspection EnhancedSwitchMigration
        switch (mnemonic) {
            case "sin": {
                a = iterator.next().apply(iterator);
                Number left = pow(new Number(Math.E, 0), new Number(-a.im(), a.real()));
                Number right = pow(new Number(Math.E, 0), new Number(a.im(), -a.real()));
                return div(new Number(left.real() - right.real(), left.im() - right.im()),
                    new Number(0, 2));
            }
            case "cos": {
                a = iterator.next().apply(iterator);
                Number left = pow(new Number(Math.E, 0), new Number(-a.im(), a.real()));
                Number right = pow(new Number(Math.E, 0), new Number(a.im(), -a.real()));
                return div(new Number(left.real() + right.real(), left.im() + right.im()),
                    new Number(2, 0));
            }
            case "log":
            case "ln": {
                a = iterator.next().apply(iterator);
                double r = Math.sqrt(a.real() * a.real() + a.im() * a.im());
                double phi = Math.atan2(a.im(), a.real());
                return new Number(Math.log(r), phi);
            }
            case "sqrt": {
                a = iterator.next().apply(iterator);
                double r = Math.sqrt(a.real() * a.real() + a.im() * a.im());
                double phi = Math.atan2(a.im(), a.real());
                double sqrtR = Math.sqrt(r);
                return new Number(sqrtR * Math.cos(phi / 2), sqrtR * Math.sin(phi / 2));
            }
            case "pow":
            case "**":
            case "^": {
                a = iterator.next().apply(iterator);
                b = iterator.next().apply(iterator);
                return pow(a, b);
            }
            case "+":
                a = iterator.next().apply(iterator);
                b = iterator.next().apply(iterator);
                return new Number(a.real() + b.real(), a.im() + b.im());
            case "-":
                a = iterator.next().apply(iterator);
                b = iterator.next().apply(iterator);
                return new Number(a.real() - b.real(), a.im() - b.im());
            case "/":
                a = iterator.next().apply(iterator);
                b = iterator.next().apply(iterator);
                return div(a, b);
            case "*":
                a = iterator.next().apply(iterator);
                b = iterator.next().apply(iterator);
                return new Number(a.real() * b.real() - a.im() * b.im(),
                    a.im() * b.real() + a.real() * b.im());
            case "from_deg":
                a = iterator.next().apply(iterator);
                return new Number(Math.toRadians(a.real()), (Math.toRadians(a.im())));
            case "to_deg":
                a = iterator.next().apply(iterator);
                return new Number(Math.toDegrees(a.real()), (Math.toDegrees(a.im())));
            case "pi":
                return new Number(Math.PI, 0);
            case "e":
                return new Number(Math.E, 0);
            default:
                return null;
        }
    }
}
