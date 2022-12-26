package ru.nsu.cjvth.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class OperationTest {

    @Test
    void isValid() {
        assertTrue(Operation.isValid("sin"));
        assertTrue(Operation.isValid("**"));
        assertTrue(Operation.isValid("/"));
        assertFalse(Operation.isValid("no"));
    }

    @Test
    void constructorInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Operation("fin"));
    }

    Number fast(String... args) {
        var iterator = Arrays.stream(args).map(Calculator::parseToken).iterator();
        Token first = iterator.next();
        return first.apply(iterator);
    }

    @Test
    void add() {
        Number res = fast("+", "1", "2.4");
        assertEquals(3.4, res.real());
    }

    @Test
    void sub() {
        Number res = fast("-", "1", "2.4");
        assertEquals(-1.4, res.real());
    }

    @Test
    void mul() {
        Number res = fast("*", "3", "7");
        assertEquals(21, res.real());
    }

    @Test
    void div() {
        Number res = fast("/", "2", "2.5");
        assertEquals(0.8, res.real());
    }

    @Test
    void pow() {
        Number res = fast("^", "3.2", "1.5");
        assertEquals(5.7243340223994625, res.real());
    }

    @Test
    void sin() {
        Number res = fast("sin", "1");
        assertEquals(0.84147098480789650665, res.real());
    }

    @Test
    void cos() {
        Number res = fast("cos", "9");
        assertEquals(-0.91113026188467698837, res.real());
    }

    @Test
    void log() {
        Number res = fast("log", "8");
        assertEquals(2.07944154167983592825, res.real());
    }

    @Test
    void sqrt() {
        Number res = fast("sqrt", "13");
        assertEquals(3.60555127546398929312, res.real());
    }
}