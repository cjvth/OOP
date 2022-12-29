package ru.nsu.cjvth.calculator;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that can parse and contain real number.
 */
public class Number implements Token {

    private final double real;
    private final double im;

    /**
     * Constructor.
     *
     * @param number string representation of a number
     */
    public Number(String number) {
        Pattern p = Pattern.compile("(?<number>(-?\\d+(\\.\\d+)?)?)(?<i>i?)");
        Matcher m = p.matcher(number);
        if (!m.matches()) {
            throw new IllegalArgumentException(
                "Not a valid real or imaginary number: " + number);
        }
        String numParse = m.group("number");
        double num;
        if (numParse.isEmpty()) {
            num = 1;
        } else {
            num = Double.parseDouble(numParse);
        }
        String i = m.group("i");
        if (i.isEmpty()) {
            real = num;
            im = 0;
        } else {
            real = 0;
            im = num;
        }
    }

    /**
     * Constructor.
     *
     * @param real real part of the number
     * @param imaginary imaginary part of the number
     */
    public Number(double real, double imaginary) {
        this.real = real;
        this.im = imaginary;
    }

    /**
     * Check if string is a valid number.
     *
     * @param number string that may represent number
     * @return whether string is a valid number
     */
    public static boolean isValid(String number) {
        return number.matches("(-?\\d+(\\.\\d+)?)?i?");
    }

    /**
     * Get real part of a complex number.
     *
     * @return the real part
     */
    public double real() {
        return real;
    }

    /**
     * Get imaginary part of a comblex number.
     *
     * @return the imaginary part
     */
    public double im() {
        return im;
    }

    @Override
    public Number apply(Iterator<Token> iterator) {
        return this;
    }
    
    @Override
    public String toString() {
        if (Double.isNaN(this.real()) || Double.isNaN(this.im())) {
            return "NaN";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.real != 0 || this.im == 0) {
                if (Math.round(this.real) == this.real) {
                    stringBuilder.append((int) this.real);
                } else {
                    stringBuilder.append(this.real);
                }
                if (this.im != 0) {
                    stringBuilder.append(" + ");
                }
            }
            if (this.im() != 0) {
                if (Double.isFinite(this.im) && !Double.toString(this.im)
                    .contains("E")) {
                    if (this.im != 1) {
                        if (Math.round(this.im) == this.im) {
                            stringBuilder.append((int) this.im);
                        } else {
                            stringBuilder.append(this.im);
                        }
                    }
                    stringBuilder.append("i");
                } else {
                    stringBuilder.append(this.im);
                    stringBuilder.append("*i");
                }
            }
            return stringBuilder.toString();
        }
    }
}
