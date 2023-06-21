package ru.nsu.cjvth.primes;

import java.util.ArrayList;
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
     * @param numThreads amount of threads
     * @return list of booleans, true if the corresponding element of `numbers` is prime
     */
    public static List<Boolean> checkPrime(List<Long> numbers, int numThreads)
        throws InterruptedException {
        List<Boolean> result = new ArrayList<>(Collections.nCopies(numbers.size(), false));
        List<Thread> threads = new ArrayList<>(numThreads);
        for (int i = 0; i < numThreads; i++) {
            var t = new Thread(new CheckPrimeRunnable(numbers, result, i, numThreads));
            threads.add(t);
            t.start();
        }
        for (var t : threads) {
            t.join();
        }
        return result;
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static class CheckPrimeRunnable implements Runnable {

        private final List<Long> input;
        private final List<Boolean> result;

        private final int start;
        private final int step;

        public CheckPrimeRunnable(List<Long> input, List<Boolean> result, int start, int step) {
            this.input = input;
            this.result = result;
            this.start = start;
            this.step = step;
        }

        @Override
        public void run() {
            for (int i = start; i < input.size(); i += step) {
                long x;
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
