package ru.nsu.cjvth.primes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class RunnersTest {

    private static final List<Long> input =
        List.of(5671L, 74797L, 17449L, 67431L, 56419L, 60961L, 69623L, 60887L, 55713L, 7993L, 8581L,
            9875L, 25867L, 28933L, 64507L, 33923L);

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
        assertIterableEquals(expected, ParallelStreamRunner.checkPrime(input));
    }
}