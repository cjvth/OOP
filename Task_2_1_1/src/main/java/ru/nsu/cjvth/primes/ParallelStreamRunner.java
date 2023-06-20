package ru.nsu.cjvth.primes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to check prime numbers using default parallel stream.
 */
public class ParallelStreamRunner {
    /**
     * Find prime numbers in a list.
     *
     * @param numbers list of numbers
     * @return list of booleans, true if the corresponding element of `numbers` is prime
     */
    public static List<Boolean> checkPrime(List<Integer> numbers) {
        return numbers.parallelStream().map(IsPrime::isPrime).collect(Collectors.toList());
    }
}
