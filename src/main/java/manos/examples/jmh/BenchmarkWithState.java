package manos.examples.jmh;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@State(Scope.Benchmark) //different states explained below
public class BenchmarkWithState {
    public static void main(String... args) throws IOException, RunnerException {
        Main.main(args);
    }
    @Param({ "100", "200", "300", "500", "1000" })
    int iterations;

    List<String> aList;

    @Setup(Level.Invocation)
    public void benchmark(){
        aList = new ArrayList<>();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(value = 1, warmups = 1)
    public void methodBenchmark(BenchmarkWithState plan){
        for (int i=0; i<plan.iterations; i++){
            aList.add(Integer.toString(i));
        }

        aList.forEach(Integer::parseInt);
    }




    /**
     * Benchmark
     *
     * <p>Benchmark state scope.</p>
     *
     * <p>With benchmark scope, all instances of the same type will be shared across all
     * worker threads.</p>
     *
     * <p>{@link Setup} and {@link TearDown} methods on this state object would be performed
     * by one of the worker threads, and only once per {@link Level}.
     * No other threads would ever touch the state object.</p>
     */

    /**
     * Group
     *
     * <p>Group state scope.</p>
     *
     * <p>With group scope, all instances of the same type will be shared across all
     * threads within the same group. Each thread group will be supplied with its own
     * state object.</p>
     *
     * <p>{@link Setup} and {@link TearDown} methods on this state object would be performed
     * by one of the group threads, and only once per {@link Level}.
     * No other threads would ever touch the state object.</p>
     *
     * @see Group
     */

    /**
     * Thread
     *
     * <p>Thread state scope.</p>
     *
     * <p>With thread scope, all instances of the same type are distinct, even if multiple
     * state objects are injected in the same benchmark</p>
     *
     * <p>{@link Setup} and {@link TearDown} methods on this state object would be performed
     * by single worker thread exclusively, and only once per {@link Level}.
     * No other threads would ever touch the state object.</p>
     *
     */

    //@Setup(Level.)


    /**
     * Trial
     *
     * Trial level: to be executed before/after each run of the benchmark.
     *
     * <p>Trial is the set of benchmark iterations.</p>
     */

    /**
     * Iteration
     *
     * Iteration level: to be executed before/after each iteration of the benchmark.
     *
     * <p>Iteration is the set of benchmark invocations.</p>
     */

    /**
     * Invocation level
     *
     * Invocation level: to be executed for each benchmark method execution.
     *
     * <p><b>WARNING: HERE BE DRAGONS! THIS IS A SHARP TOOL.
     * MAKE SURE YOU UNDERSTAND THE REASONING AND THE IMPLICATIONS
     * OF THE WARNINGS BELOW BEFORE EVEN CONSIDERING USING THIS LEVEL.</b></p>
     *
     * <p>This level is only usable for benchmarks taking more than a millisecond
     * per single {@link Benchmark} method invocation. It is a good idea to validate
     * the impact for your case on ad-hoc basis as well.</p>
     *
     * <p>WARNING #1: Since we have to subtract the setup/teardown costs from
     * the benchmark time, on this level, we have to timestamp *each* benchmark
     * invocation. If the benchmarked method is small, then we saturate the
     * system with timestamp requests, which introduce artificial latency,
     * throughput, and scalability bottlenecks.</p>
     *
     * <p>WARNING #2: Since we measure individual invocation timings with this
     * level, we probably set ourselves up for (coordinated) omission. That means
     * the hiccups in measurement can be hidden from timing measurement, and
     * can introduce surprising results. For example, when we use timings to
     * understand the benchmark throughput, the omitted timing measurement will
     * result in lower aggregate time, and fictionally *larger* throughput.</p>
     *
     * <p>WARNING #3: In order to maintain the same sharing behavior as other
     * Levels, we sometimes have to synchronize (arbitrage) the access to
     * {@link State} objects. Other levels do this outside the measurement,
     * but at this level, we have to synchronize on *critical path*, further
     * offsetting the measurement.</p>
     *
     * <p>WARNING #4: Current implementation allows the helper method execution
     * at this Level to overlap with the benchmark invocation itself in order
     * to simplify arbitrage. That matters in multi-threaded benchmarks, when
     * one worker thread executing {@link Benchmark} method may observe other
     * worker thread already calling {@link TearDown} for the same object.</p>
     */
}
