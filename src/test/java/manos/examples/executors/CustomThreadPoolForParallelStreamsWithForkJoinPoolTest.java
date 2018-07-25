package manos.examples.executors;

import mt.streams.StreamUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CustomThreadPoolForParallelStreamsWithForkJoinPoolTest {
    private static final int THREAD_POOL_SIZE = 4;
    ForkJoinPool forkJoinPool;
    @Before
    public void setForkJoinPool(){
        forkJoinPool = new ForkJoinPool(THREAD_POOL_SIZE);
    }
    @After
    public void shutdownForkJoinPool(){
        forkJoinPool.shutdown();
        try {
            if (!forkJoinPool.awaitTermination(1, TimeUnit.SECONDS)){
                forkJoinPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            forkJoinPool.shutdownNow();
        }
    }

    @Test
    public void use_fork_join_pool_instead_of_the_common_thread_pool() throws ExecutionException, InterruptedException {
        long numberOfThreadsCustom = forkJoinPool.submit(
                ()-> IntStream.rangeClosed(1, 50).parallel().mapToObj(Integer::valueOf).map(StreamUtils.unthrowFunction(this::getThreadNameAndPauseForAWhile)).distinct().count()
        ).get();

        long numberOfThreadsInCommonPool = IntStream.rangeClosed(1, 50).parallel().mapToObj(Integer::valueOf).map(StreamUtils.unthrowFunction(this::getThreadNameAndPauseForAWhile)).distinct().count();

        Assert.assertEquals(THREAD_POOL_SIZE, numberOfThreadsCustom);
        //fork join pool has as many threads as the available processors (on of them IS THE MAIN THREAD)
        Assert.assertEquals(Runtime.getRuntime().availableProcessors(), numberOfThreadsInCommonPool);
    }
    private String getThreadNameAndPauseForAWhile(int i) throws InterruptedException {
        Thread.sleep(100);
        return Thread.currentThread().getName();
    }
}
