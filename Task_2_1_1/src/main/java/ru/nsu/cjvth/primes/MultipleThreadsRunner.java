package ru.nsu.cjvth.primes;

import static ru.nsu.cjvth.primes.IsPrime.isPrime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class to check prime numbers using multiple threads.
 */
public class MultipleThreadsRunner {
    /**
     * Find prime numbers in a list.
     *
     * @param numbers    list of numbers
     * @param numThreads numThreads level
     * @return list of booleans, true if the corresponding element of `numbers` is prime
     */
    public static List<Boolean> checkPrime(List<Integer> numbers, int numThreads) {
        List<Boolean> result = new ArrayList<>(Collections.nCopies(numbers.size(), false));
        for (int i : numbers) {
            result.add(isPrime(i));
        }
        return result;
    }

    private record CheckPrimeRunnable(List<Integer> input, List<Boolean> result, int start,
                                      int step) implements Runnable {

        @Override
        public void run() {
            for (int i = start; i < input.size(); i += step) {
                int x;
                synchronized (input) {
                    x = input.get(i);
                }
                boolean res = IsPrime.isPrime(x);
                synchronized (result) {
                    result.set(i, res);
                }
            }
        }
    }
}
