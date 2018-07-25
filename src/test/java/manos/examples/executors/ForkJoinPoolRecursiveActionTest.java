package manos.examples.executors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * You can submit two types of tasks in a fork join pool.
 * A task that does not return any result (an "action"), and a task which does return a result (a "task").
 * These two types of tasks are represented by the <b>RecursiveAction</b> and <b><RecursiveTask/b> classes.
 * How to use both of these tasks and how to submit them will be covered in the following sections.
 */
public class ForkJoinPoolRecursiveActionTest {
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

    private class CustomRecursiveAction extends RecursiveAction{
        List<Integer> numbers;
        ConcurrentMap<Integer, Integer> resultMap;
        int low, high;
        public CustomRecursiveAction(List<Integer> numbers, int low, int high, ConcurrentMap<Integer, Integer> resultMap) {
            //System.out.println(Thread.currentThread().getName()+": Created recursive task with workload " + low + " " + high);
            this.numbers = numbers;
            this.resultMap = resultMap;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high-low>1024){
//                System.out.println("Splitting with low " + low + " and high " + high);
                (new CustomRecursiveAction(numbers, low, ((high-low)/2+low), resultMap)).fork();
                (new CustomRecursiveAction(numbers, ((high-low)/2+low), high, resultMap)).fork();
                //or ForkJoinTask.invokeAll( a list of tasks...)
            } else {
//                System.out.println("Executing with low " + low + " and high " + high);
                numbers.subList(low, high).forEach(i->resultMap.put(i, Math.negateExact(i)));
            }
        }
    }

    @Test
    public void invoke_test() throws InterruptedException {
        //setup
        final int numberOfElements = 1024*1024*5;
        List<Integer> numbers = IntStream.rangeClosed(1,numberOfElements).mapToObj(Integer::valueOf).collect(Collectors.toList());
        ConcurrentMap<Integer, Integer> concurrentResultMap = new ConcurrentHashMap<>();

        //run
        Instant sequentialStart = Instant.now();
        numbers.forEach(i->concurrentResultMap.put(i, Math.negateExact(i)));
        Instant sequentialFinished = Instant.now();

        concurrentResultMap.clear();

        Instant parallelStarted = Instant.now();
        forkJoinPool.invoke(new CustomRecursiveAction(numbers, 0, numbers.size(), concurrentResultMap));
        while(!forkJoinPool.isQuiescent()){
            TimeUnit.MILLISECONDS.sleep(1);
        }
        Instant parallelFinished = Instant.now();


        System.out.println("Parallel " + Duration.between(parallelStarted, parallelFinished).toMillis());
        System.out.println("Sequential " + Duration.between(sequentialStart, sequentialFinished).toMillis());

        //results
        Assert.assertEquals(numberOfElements, concurrentResultMap.size());
        concurrentResultMap.forEach((k, v) -> Assert.assertEquals((int)v, Math.negateExact(k)) );
    }
}
