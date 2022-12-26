package ru.nsu.cjvth.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NumberTest {

    @Test
    void isValid() {
        assertTrue(Number.isValid("123"));
        assertTrue(Number.isValid("-23"));
        assertTrue(Number.isValid("12.3"));
        assertTrue(Number.isValid("0.00001"));
        assertTrue(Number.isValid("-11111.1"));
        assertFalse(Number.isValid("1.1.2"));
        assertFalse(Number.isValid("--5"));
        assertFalse(Number.isValid("1/2"));
    }

    @Test
    void stringConstructor() {
        assertEquals(-23, new Number("-23").real());
        assertEquals(12.3, new Number("12.3").real());
    }

    @Test
    void stringConstructorInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Number("-23.3.4"));
        assertThrows(IllegalArgumentException.class, () -> new Number("--434"));
    }

    @Test
    void doubleConstructor() {
        assertEquals(1.000, new Number(1).real());
        assertEquals(-12.5, new Number(-12.5).real());
    }

}