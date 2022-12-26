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
    void isValidImaginary() {
        assertTrue(Number.isValid("2i"));
        assertTrue(Number.isValid("i"));
        assertTrue(Number.isValid("-12.3i"));
        assertFalse(Number.isValid("--5i"));
        assertFalse(Number.isValid("0.1.2i"));
    }

    @Test
    void stringConstructor() {
        assertEquals(-23, new Number("-23").real());
        assertEquals(0, new Number("-23").im());
        assertEquals(12.3, new Number("12.3").real());
        assertEquals(0, new Number("12.3").im());
    }

    @Test
    void stringConstructorImaginary() {
        assertEquals(0, new Number("-23.3i").real());
        assertEquals(-23.3, new Number("-23.3i").im());
        assertEquals(0, new Number("i").real());
        assertEquals(1, new Number("i").im());
    }

    @Test
    void stringConstructorInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Number("-23.3.4"));
        assertThrows(IllegalArgumentException.class, () -> new Number("--434"));
    }

    @Test
    void stringConstructorInvalidImaginary() {
        assertThrows(IllegalArgumentException.class, () -> new Number("-23.3.4i"));
        assertThrows(IllegalArgumentException.class, () -> new Number("ii"));
    }

    @Test
    void doubleConstructor() {
        assertEquals(1.000, new Number(1, 0).real());
        assertEquals(0, new Number(1, 0).im());
        assertEquals(-12.5, new Number(-12.5, 0).real());
        assertEquals(0, new Number(-12.5, 0).im());
    }

    @Test
    void doubleConstructorComplex() {
        assertEquals(1.000, new Number(1, 7).real());
        assertEquals(7, new Number(1, 7).im());
    }

}