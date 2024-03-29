package ru.nsu.cjvth.primes;

/**
 * Check if given number is prime.
 */
public class IsPrime {

    /**
     * Check if given number is prime.
     *
     * @param x a number
     * @return whether x is a prime number
     */
    public static boolean isPrime(long x) {
        if (x <= 1) {
            return false;
        }
        long sqrt = (long) Math.sqrt(x);
        for (long i = 2; i <= sqrt; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }
}