package manos.examples.executors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ForkJoinPoolRecursiveTaskTest {
    ForkJoinPool forkJoinPool;
    private static final int THREAD_POOL_SIZE = 7;
    @Before
    public void before(){
        forkJoinPool = new ForkJoinPool(THREAD_POOL_SIZE);
    }
    @After
    public void after(){
        forkJoinPool.shutdown();
        try {
            if (!forkJoinPool.awaitTermination(1, TimeUnit.SECONDS))
                forkJoinPool.shutdownNow();
        } catch (InterruptedException e) {
            forkJoinPool.shutdownNow();
        }
    }

    private class CustomRecursiveTask extends RecursiveTask<List<Integer>>{
        private final List<Integer> numbers;
        private final int low, high;

        public CustomRecursiveTask(List<Integer> numbers, int low, int high) {
            this.numbers = numbers;
            this.low = low;
            this.high = high;
        }

        @Override
        protected List<Integer> compute() {
            if (high-low>1024){
                ForkJoinTask<List<Integer>> task0 = (new CustomRecursiveTask(numbers, low, ((high-low)/2+low))).fork();
                ForkJoinTask<List<Integer>> task1 = (new CustomRecursiveTask(numbers, ((high-low)/2+low), high)).fork();

                List<Integer> result0 = task0.join();
                List<Integer> result1 = task1.join();

                result0.addAll(result1);
                return result0;
            } else {
                return numbers.subList(low, high).stream().map(Math::negateExact).collect(Collectors.toList());
            }
        }
    }

    @Test
    public void invoke_test() throws InterruptedException {
        //setup
        final int numberOfElements = 1024*1024*10;
        List<Integer> numbers = IntStream.rangeClosed(1,numberOfElements).mapToObj(Integer::valueOf).collect(Collectors.toList());

        //run
        Instant sequentialStart = Instant.now();
        List<Integer> numbersChangedSeq = numbers.stream().map(Math::negateExact).collect(Collectors.toList());
        Instant sequentialFinished = Instant.now();


        Instant parallelStarted = Instant.now();
        List<Integer> numbersChangedParallel = forkJoinPool.invoke(new CustomRecursiveTask(numbers, 0, numbers.size()));
        Instant parallelFinished = Instant.now();


        System.out.println("Parallel " + Duration.between(parallelStarted, parallelFinished).toMillis());
        System.out.println("Sequential " + Duration.between(sequentialStart, sequentialFinished).toMillis());

        //results
        Assert.assertEquals(numberOfElements, numbersChangedParallel.size());
        Assert.assertEquals(numbersChangedSeq.size(), numbersChangedParallel.size());
        Set<Integer> uniqueChangedNumbers = numbersChangedParallel.stream().collect(Collectors.toSet());
        numbers.forEach(i -> Assert.assertTrue(uniqueChangedNumbers.contains(Math.negateExact(i))));
    }
}
