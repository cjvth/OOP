package ru.nsu.cjvth.primes;

/**
 * Check if given number is prime.
 */
public class CheckPrime {

    /**
     * Check if given number is prime.
     *
     * @param x a number
     * @return whether x is a prime number
     */
    public static boolean isPrime(int x) {
        if (x <= 1) {
            return false;
        }
        int sqrt = (int) Math.sqrt(x);
        for (int i = 2; i <= sqrt; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }
}