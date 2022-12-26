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
    void addComplex() {
        Number res = fast("+ + 1 2i + 3 4i".split("\\s+"));
        assertEquals(4, res.real());
        assertEquals(6, res.im());
    }

    @Test
    void sub() {
        Number res = fast("-", "1", "2.4");
        assertEquals(-1.4, res.real());
    }

    @Test
    void subComplex() {
        Number res = fast("- + 1 2i + 3 4i".split("\\s+"));
        assertEquals(-2, res.real());
        assertEquals(-2, res.im());
    }

    @Test
    void mul() {
        Number res = fast("*", "3", "7");
        assertEquals(21, res.real());
    }

    @Test
    void mulComplex() {
        Number res = fast("* + 1 2i + 3 4i".split("\\s+"));
        assertEquals(-5, res.real());
        assertEquals(10, res.im());
    }

    @Test
    void div() {
        Number res = fast("/", "2", "2.5");
        assertEquals(0.8, res.real());
    }

    @Test
    void divComplex() {
        Number res = fast("/ + 1 2i + 3 4i".split("\\s+"));
        assertEquals(0.44, res.real());
        assertEquals(0.08, res.im());
    }

    @Test
    void pow() {
        Number res = fast("^", "3.2", "1.5");
        assertTrue(Math.abs(5.72433402239946162281 - res.real()) < 0.0000001);
    }

    @Test
    void powComplex() {
        Number res = fast("^ + 2 3i + 4 5i".split("\\s+"));
        assertEquals(-0.75304583674855896297, res.real(), 0.0000001);
        assertEquals(-0.98642878864774534383, res.im(), 0.0000001);
    }

    @Test
    void sin() {
        Number res = fast("sin", "1");
        assertEquals(0.84147098480789650665, res.real());
    }

    @Test
    void sinComplex() {
        Number res = fast("sin + 1 1.5i".split("\\s+"));
        assertEquals(1.97948443561030023694, res.real(), 0.0000001);
        assertEquals(1.15045459942538595151, res.im(), 0.0000001);
    }

    @Test
    void cos() {
        Number res = fast("cos", "9");
        assertEquals(-0.91113026188467698837, res.real());
    }

    @Test
    void cosComplex() {
        Number res = fast("cos + 1 2i".split("\\s+"));
        assertEquals(2.03272300701966552944, res.real(), 0.0000001);
        assertEquals(-3.05189779915180005751, res.im(), 0.0000001);
    }

    @Test
    void log() {
        Number res = fast("log", "8");
        assertEquals(2.07944154167983592825, res.real());
    }

    @Test
    void logComplex() {
        Number res = fast("log + 2 3i".split("\\s+"));
        assertEquals(1.28247467873076836803, res.real(), 0.0000001);
        assertEquals(0.98279372324732906799, res.im(), 0.0000001);
    }

    @Test
    void sqrt() {
        Number res = fast("sqrt", "13");
        assertEquals(3.60555127546398929312, res.real());
    }

    @Test
    void sqrtComplex() {
        Number res = fast("sqrt + 4 5i".split("\\s+"));
        assertEquals(2.28069334166529813703, res.real(), 0.0000001);
        assertEquals(1.09615788950151900243, res.im(), 0.0000001);
    }

    @Test
    void fromDeg() {
        Number res = fast("from_deg", "56");
        assertEquals(0.97738438111682456308, res.real());
    }

    @Test
    void fromDegComplex() {
        Number res = fast("from_deg", "+", "60", "30i");
        assertEquals(Math.PI / 3, res.real());
        assertEquals(Math.PI / 6, res.im());
    }

    @Test
    void toDeg() {
        Number res = fast("to_deg", "1.5");
        assertEquals(85.9436692696234813152, res.real());
    }

    @Test
    void toDegComplex() {
        Number res = fast("to_deg", "+", "pi", "/", "*", "pi", "i", "2");
        assertEquals(180, res.real());
        assertEquals(90, res.im());
    }

    @Test
    void pi() {
        Number res = fast("pi");
        assertEquals(Math.PI, res.real());
    }

    @Test
    void eps1() {
        Number res = fast("e");
        assertEquals(Math.E, res.real());
    }
}