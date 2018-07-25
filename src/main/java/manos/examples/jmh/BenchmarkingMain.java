package manos.examples.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;

public class BenchmarkingMain {
    public static void main(String... args) throws IOException, RunnerException {
        org.openjdk.jmh.Main.main(args);
    }


    @Benchmark
    /**
     * By using the @Fork annotation, we can set up how benchmark execution happens:
     * the value parameter controls how many times the benchmark will be executed,
     * and the warmup parameter controls how many times a benchmark will dry run before results are collected
     *
     * There is also a @Warmup annotation which defines only the warmup cycles
     */
    @Fork(value = 1, warmups = 2)
    //comment about Mode below
    @BenchmarkMode(Mode.Throughput)
    public void benchmark() {
        List<String> numbers =  IntStream.range(0, 1_000_000).mapToObj(i->Integer.toString(i)).collect(Collectors.toList());
        numbers.clear();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void benchmark1() {
        List<String> numbers =  new ArrayList<>(1_000_000);
        for (int i=0; i<1_000_000; i++){
            numbers.add(Integer.toString(i));
        }
        numbers.clear();
    }



    /**
     * Throughput
     *
     * <p>Throughput: operations per unit of time.</p>
     *
     * <p>Runs by continuously calling {@link Benchmark} methods,
     * counting the total throughput over all worker threads. This mode is time-based, and it will
     * run until the iteration time expires.</p>
     */


    /**
     * Average Time
     *
     * <p>Average time: average time per per operation.</p>
     *
     * <p>Runs by continuously calling {@link Benchmark} methods,
     * counting the average time to call over all worker threads. This is the inverse of {@link Mode#Throughput},
     * but with different aggregation policy. This mode is time-based, and it will run until the iteration time
     * expires.</p>
     */

    /**
     * Sample time
     *
     * <p>Sample time: samples the time for each operation.</p>
     *
     * <p>Runs by continuously calling {@link Benchmark} methods,
     * and randomly samples the time needed for the call. This mode automatically adjusts the sampling
     * frequency, but may omit some pauses which missed the sampling measurement. This mode is time-based, and it will
     * run until the iteration time expires.</p>
     */

    /**
     * SingleShotTime
     *
     * <p>Single shot time: measures the time for a single operation.</p>
     *
     * <p>Runs by calling {@link Benchmark} once and measuring its time.
     * This mode is useful to estimate the "cold" performance when you don't want to hide the warmup invocations, or
     * if you want to see the progress from call to call, or you want to record every single sample. This mode is
     * work-based, and will run only for a single invocation of {@link Benchmark}
     * method.</p>
     *
     * Caveats for this mode include:
     * <ul>
     *  <li>More warmup/measurement iterations are generally required.</li>
     *  <li>Timers overhead might be significant if benchmarks are small; switch to {@link #SampleTime} mode if
     *  that is a problem.</li>
     * </ul>
     */

    /**
     * All
     *
     * Meta-mode: all the benchmark modes.
     * This is mostly useful for internal JMH testing.
     */
}
