package ru.nsu.cjvth.primes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class IsPrimeTest {

    @ParameterizedTest
    @ValueSource(ints = {-100, -3, -2, -1, 0, 1, 2})
    void isPrimeSpecial(int x) {
        assertEquals(x > 1, IsPrime.isPrime(x));
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 7, 19, 929, 8543, 1254863})
    void isPrimePrime(int x) {
        assertTrue(IsPrime.isPrime(x));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 6, 21, 343, 1854214})
    void isPrimeNonPrime(int x) {
        assertFalse(IsPrime.isPrime(x));
    }
}