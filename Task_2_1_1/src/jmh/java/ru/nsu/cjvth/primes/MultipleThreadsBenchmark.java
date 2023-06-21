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
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Benchmark MultipleThreadsRunner.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@Fork(value = 1, warmups = 0)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MultipleThreadsBenchmark {

    @Param({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"})
    int numThreads;

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
    public void multipleThreadsBenchmark(Blackhole bh) throws InterruptedException {
        bh.consume(MultipleThreadsRunner.checkPrime(data, numThreads));
    }
}
