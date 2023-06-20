package ru.nsu.cjvth.primes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class RunnersTest {

    private static final List<Integer> input =
        List.of(5671, 74797, 17449, 67431, 56419, 60961, 69623, 60887, 55713, 7993, 8581, 9875,
            25867, 28933, 64507, 33923);
    private static final List<Boolean> expected =
        List.of(false, true, true, false, false, true, true, true, false, true, true, false, true,
            true, false, true);

    @Test
    void sequentialRunner() {
        assertIterableEquals(expected, SequentialRunner.checkPrime(input));
    }

    @Test
    void multipleThreadsRunner() {
        assertDoesNotThrow(
            () -> assertIterableEquals(expected, MultipleThreadsRunner.checkPrime(input, 4))
        );
    }

    @Test
    void parallelStreamRunner() {
        assertDoesNotThrow(
            () -> assertIterableEquals(expected, ParallelStreamRunner.checkPrime(input, 4))
        );
    }
}