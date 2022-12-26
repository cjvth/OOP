package ru.nsu.cjvth.calculator;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    @Test
    void isValidToken() {
        assertTrue(Calculator.isValidToken("/"));
        assertFalse(Calculator.isValidToken("no"));
        assertTrue(Calculator.isValidToken("-11111.1"));
        assertFalse(Calculator.isValidToken("1.1.2"));
    }

    Number fast(String... args) {
        var iterator = Arrays.stream(args).map(Calculator::parseToken).iterator();
        Token first = iterator.next();
        return first.apply(iterator);
    }

    @Test
    void mainIsSafe() {
        assertThrows(NullPointerException.class, () -> fast(":-("));
        assertDoesNotThrow(() -> Calculator.main(new String[]{":-("}));
        //noinspection Convert2MethodRef
        assertThrows(NoSuchElementException.class, () -> fast());
        assertDoesNotThrow(() -> Calculator.main(new String[]{}));
        assertThrows(NoSuchElementException.class, () -> fast("+"));
        assertDoesNotThrow(() -> Calculator.main(new String[]{"+"}));
        assertDoesNotThrow(() -> Calculator.main(new String[]{"+", "1", "2"}));
    }

    @Test
    void wikipediaExamples() {
        assertEquals(-7, fast("* - 5 6 7".split("\\s+")).real());
        assertEquals(-37, fast("- 5 * 6 7".split("\\s+")).real());
    }

    @Test
    void rowExamples() {
        assertEquals(15, fast("+ + + + 1 2 3 4 5".split("\\s+")).real());
        assertEquals(15, fast("+ 1 + 2 + 3 + 4 5".split("\\s+")).real());
    }
}