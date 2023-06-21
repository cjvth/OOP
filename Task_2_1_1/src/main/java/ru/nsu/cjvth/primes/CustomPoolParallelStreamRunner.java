package ru.nsu.cjvth.primes;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * Class to check prime numbers using parallel stream inside
 * custom thread pool with specific amount of numbers.
 */
public class CustomPoolParallelStreamRunner {
    /**
     * Find prime numbers in a list.
     *
     * @param numbers     list of numbers
     * @param parallelism parallelism level
     * @return list of booleans, true if the corresponding element of `numbers` is prime
     */
    public static List<Boolean> checkPrime(List<Long> numbers, int parallelism)
        throws ExecutionException, InterruptedException {
        var pool = new ForkJoinPool(parallelism);
        return pool.submit(
            () -> numbers.parallelStream().map(IsPrime::isPrime)
        ).get().collect(Collectors.toList());
    }
}
