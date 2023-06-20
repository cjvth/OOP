package ru.nsu.cjvth.primes;

import static ru.nsu.cjvth.primes.IsPrime.isPrime;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to check prime numbers sequentially.
 */
public class SequentialRunner {
    /**
     * Find prime numbers in a list.
     *
     * @param numbers list of numbers
     * @return list of booleans, true if the corresponding element of `numbers` is prime
     */
    public static List<Boolean> checkPrime(List<Integer> numbers) {
        List<Boolean> result = new ArrayList<>(numbers.size());
        for (int i : numbers) {
            result.add(isPrime(i));
        }
        return result;
    }
}
