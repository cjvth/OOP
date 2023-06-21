package ru.nsu.cjvth.primes;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Benchmark SequentialRunner and ParallelStreamRunner.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 0)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class NoParamsBenchmark {
    private static List<Long> data;

    /**
     * Read benchmarking data from resources/data.json.
     */
    @Setup
    public void readData() throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Long>>() {
        }.getType();
        data = gson.fromJson(new FileReader("src/jmh/resources/data.json"), type);
    }

    @Benchmark
    public void sequentialBenchmark(Blackhole bh) {
        bh.consume(SequentialRunner.checkPrime(data));
    }

    @Benchmark
    public void parallelStreamBenchmark(Blackhole bh) {
        bh.consume(ParallelStreamRunner.checkPrime(data));
    }
}
