package manos.examples.executors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ExecutorServiceTest {
    ExecutorService singleThreadExecutor;
    @Before
    public void setExecutorService(){
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }
    @After
    public void shutdownExecutors(){
        shutDownExecutor(singleThreadExecutor);
    }
    private void shutDownExecutor(ExecutorService service){
        service.shutdown();
        try {
            if (!service.awaitTermination(1, TimeUnit.SECONDS))
                service.shutdownNow();
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
    }

    @Test
    public void executor_with_a_single_pass() throws ExecutionException, InterruptedException {
        //non blocking call
        Future<String> executionFuture = singleThreadExecutor.submit(() ->
                IntStream
                        .rangeClosed(1, 100_000)
                        .mapToObj(Integer::valueOf)
                        .map(i-> i.toString())
                        .filter(str->str.equals("100000"))
                        .findFirst().get());
        Assert.assertFalse(executionFuture.isDone());

        //blocking call
        String result = executionFuture.get();

        Assert.assertTrue(executionFuture.isDone());
        Assert.assertEquals("100000", result);
    }
}
