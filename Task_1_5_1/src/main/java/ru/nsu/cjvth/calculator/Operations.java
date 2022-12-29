package ru.nsu.cjvth.calculator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Contains many operations and methods to generate them from string.
 */
public class Operations {

    private static final Map<String, Class<? extends Operation>> possibleOperations =
        Map.ofEntries(
            Map.entry("+", Add.class),
            Map.entry("-", Sub.class),
            Map.entry("/", Div.class),
            Map.entry("*", Mul.class),
            Map.entry("pow", Pow.class),
            Map.entry("^", Pow.class),
            Map.entry("**", Pow.class),
            Map.entry("sin", Sin.class),
            Map.entry("cos", Cos.class),
            Map.entry("log", Log.class),
            Map.entry("ln", Log.class),
            Map.entry("sqrt", Sqrt.class),
            Map.entry("from_deg", FromDeg.class),
            Map.entry("to_deg", ToDeg.class),
            Map.entry("pi", Pi.class),
            Map.entry("e", Eul.class)
        );


    /**
     * Get operation corresponding the given string.
     *
     * @param string string of operation name, must be a key of
     *               {@link Operations#possibleOperations}
     * @return operation that matches given string
     */
    public static Operation parse(String string) {
        if (!possibleOperations.containsKey(string)) {
            throw new IllegalArgumentException("Unknown operation");
        }
        Operation op;
        try {
            op = possibleOperations.get(string).getConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                 | IllegalAccessException e) {
            throw new IllegalStateException(
                "Internal error: one of possible operations doesn't have a constructor");
        }
        return op;
    }

    /**
     * Check that string represents some operation from {@link Operations#possibleOperations} keys.
     *
     * @param string string of operation name
     * @return whether the string represents some operation
     */
    public static boolean isOperation(String string) {
        return possibleOperations.containsKey(string);
    }

    /**
     * Addition of two numbers.
     */
    public static class Add extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            Number b = args[1];
            return new Number(a.real() + b.real(), a.im() + b.im());
        }

        @Override
        public int arity() {
            return 2;
        }
    }

    /**
     * Subtraction of two numbers.
     */
    public static class Sub extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            Number b = args[1];
            return new Number(a.real() - b.real(), a.im() - b.im());
        }

        @Override
        public int arity() {
            return 2;
        }
    }

    /**
     * Division of two numbers.
     */
    public static class Div extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            Number b = args[1];
            return new Number(
                (a.real() * b.real() + a.im() * b.im()) / (b.real() * b.real()
                    + b.im() * b.im()),
                (a.im() * b.real() - a.real() * b.im()) / (b.real() * b.real()
                    + b.im() * b.im()));
        }

        @Override
        public int arity() {
            return 2;
        }
    }

    /**
     * Multiplication of two numbers.
     */
    public static class Mul extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            Number b = args[1];
            return new Number(a.real() * b.real() - a.im() * b.im(),
                a.im() * b.real() + a.real() * b.im());
        }

        @Override
        public int arity() {
            return 2;
        }
    }

    /**
     * Exponentiation of two numbers.
     */
    public static class Pow extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            Number b = args[1];
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

        @Override
        public int arity() {
            return 2;
        }
    }

    /**
     * Sine of a number in radians.
     */
    public static class Sin extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            Div div = new Div();
            Pow pow = new Pow();
            Number left = pow.calculate(new Number(Math.E, 0), new Number(-a.im(), a.real()));
            Number right = pow.calculate(new Number(Math.E, 0), new Number(a.im(), -a.real()));
            return div.calculate(new Number(left.real() - right.real(), left.im() - right.im()),
                new Number(0, 2));
        }

        @Override
        public int arity() {
            return 1;
        }
    }

    /**
     * Cosine of a number in radians.
     */
    public static class Cos extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            Pow pow = new Pow();
            Div div = new Div();
            Number left = pow.calculate(new Number(Math.E, 0), new Number(-a.im(), a.real()));
            Number right = pow.calculate(new Number(Math.E, 0), new Number(a.im(), -a.real()));
            return div.calculate(new Number(left.real() + right.real(), left.im() + right.im()),
                new Number(2, 0));
        }

        @Override
        public int arity() {
            return 1;
        }
    }

    /**
     * Logarithm of a number.
     */
    public static class Log extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            double r = Math.sqrt(a.real() * a.real() + a.im() * a.im());
            double phi = Math.atan2(a.im(), a.real());
            return new Number(Math.log(r), phi);
        }

        @Override
        public int arity() {
            return 1;
        }
    }

    /**
     * Square root of a number.
     */
    public static class Sqrt extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            double r = Math.sqrt(a.real() * a.real() + a.im() * a.im());
            double phi = Math.atan2(a.im(), a.real());
            double sqrtR = Math.sqrt(r);
            return new Number(sqrtR * Math.cos(phi / 2), sqrtR * Math.sin(phi / 2));
        }

        @Override
        public int arity() {
            return 1;
        }
    }

    /**
     * Converting from degrees to radians.
     */
    public static class FromDeg extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            return new Number(Math.toRadians(a.real()), (Math.toRadians(a.im())));
        }

        @Override
        public int arity() {
            return 1;
        }
    }

    /**
     * Converting from radians to degrees.
     */
    public static class ToDeg extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            Number a = args[0];
            return new Number(Math.toDegrees(a.real()), (Math.toDegrees(a.im())));
        }

        @Override
        public int arity() {
            return 1;
        }
    }

    /**
     * Constant pi.
     */
    public static class Pi extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            return new Number(Math.PI, 0);
        }

        @Override
        public int arity() {
            return 0;
        }
    }

    /**
     * Constant e, Euler's number.
     */
    public static class Eul extends Operation {

        @Override
        public Number calculate(Number... args) {
            super.assertArgs(args);
            return new Number(Math.E, 0);
        }

        @Override
        public int arity() {
            return 0;
        }
    }
}
