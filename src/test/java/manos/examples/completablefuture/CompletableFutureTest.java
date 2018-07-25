package manos.examples.completablefuture;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class CompletableFutureTest {



    @Test
    public void creating_completed_completableFuture(){
        //creates an already completed CompletableFuture with a predefined result.
        // Usually this may act as the starting stage in your computation
        CompletableFuture completableFuture = CompletableFuture.completedFuture("manos");
        Assert.assertTrue(completableFuture.isDone());

        //The getNow(null) returns the result if completed (which is obviously the case), otherwise returns null (the argument).
        Assert.assertEquals("manos", completableFuture.getNow(null));
    }

    @Test
    public void running_a_simple_asynchronous_stage(){
        //RunAsync -> runnable, executor. In the version with no executor Common Fork Join Pool is used
        CompletableFuture completableFuture = CompletableFuture.runAsync(()->{
            Assert.assertFalse(Thread.currentThread().isDaemon());
            sleepForMs(100);
        }, service);
        Assert.assertFalse(completableFuture.isDone());
        sleepForMs(300);
        Assert.assertTrue(completableFuture.isDone());
        completableFuture.join();
    }

    @Test
    public void applying_a_function_on_previous_stage(){
        /**
         * the behavioral keywords in thenApply
         * 1.   then, which means that the action of this stage happens when the current stage completes normally (without an exception).
         *          In this case, the current stage is already completed with the value “message”.
         * 2.   Apply, which means the returned stage will apply a Function on the result of the previous stage.
         *
         * The execution of the Function will be blocking, which means that getNow() will only be reached when the uppercase operation is done.
         */
        CompletableFuture completableFuture = CompletableFuture.completedFuture("manos").thenApply(str->{
           Assert.assertFalse(Thread.currentThread().isDaemon());
           sleepForMs(200);
           return str.toUpperCase();
        });

        Assert.assertTrue(completableFuture.isDone());
        sleepForMs(100);
        Assert.assertEquals("MANOS", completableFuture.getNow(null));
    }

    @Test
    public void asynchronously_applying_a_function_on_previous_stage(){
        CompletableFuture completableFuture = CompletableFuture.completedFuture("manos").thenApplyAsync(str->{
            Assert.assertFalse(Thread.currentThread().isDaemon());
            sleepForMs(100);
            return str.toUpperCase();
        }, service);
        Assert.assertFalse(completableFuture.isDone());
        Assert.assertNull(completableFuture.getNow(null));
        sleepForMs(200);
        Assert.assertTrue(completableFuture.isDone());
        Assert.assertEquals("MANOS", completableFuture.getNow(null));
    }

    @Test
    public void consuming_result_of_previous_stage(){
        StringBuilder sb = new StringBuilder();
        CompletableFuture completableFuture = CompletableFuture.completedFuture("thenAccept message")
                .thenAccept(sb::append);
        Assert.assertEquals("thenAccept message", sb.toString());
    }

    @Test
    public void consuming_result_of_previous_stage_async(){
        StringBuilder sb = new StringBuilder();
        CompletableFuture completableFuture = CompletableFuture.completedFuture("thenAccept message")
                .thenAcceptAsync(s->{
                    sleepForMs(100);
                    sb.append(s);
                }, service);

        Assert.assertFalse(completableFuture.isDone());
        sleepForMs(200);
        completableFuture.join();
        Assert.assertEquals("thenAccept message", sb.toString());
    }

    @Test
    public void completing_a_computation_with_exception(){
        CompletableFuture<String> completableFutureWithoutExceptionHandling = CompletableFuture.completedFuture("manos")
                .thenApplyAsync(s -> {
                    sleepForMs(100);
                    return s.toUpperCase();
                }, service);
        CompletableFuture completableFutureWithExceptionHandling =
                completableFutureWithoutExceptionHandling.handle((str, throwable)-> throwable == null? "nullThrowable":"notNullThrowable");

        //we will finish future with exception in order to show that completableFutureWithExceptionHandling
        //will handle the exception and the completableFutureWithoutExceptionHandling will just throw a
        //CompletionException with the thrown RuntimeException as cause.

        //complete exceptionally will throw the exception in the future but we will not notice it until we ask for the result
        //bunt the completable future "knows" that it has completed exceptionally
        completableFutureWithoutExceptionHandling.completeExceptionally(new RuntimeException("Completed Exceptionally"));
        Assert.assertTrue(completableFutureWithoutExceptionHandling.isCompletedExceptionally());
        Assert.assertFalse(completableFutureWithExceptionHandling.isCompletedExceptionally());

        try {
            completableFutureWithoutExceptionHandling.join();
        }catch (CompletionException e){
            //the cause of completionException is the RuntimeException we throw above;
            Assert.assertEquals("Completed Exceptionally", e.getCause().getMessage());
        }

        Assert.assertEquals("notNullThrowable", completableFutureWithExceptionHandling.join());
    }

    @Test
    public void cancelling_computation(){
        /**
         * Very close to exceptional completion, we can cancel a computation via the cancel(boolean mayInterruptIfRunning) method from the Future interface.
         * For CompletableFuture, the boolean parameter is !!!not used because the implementation does !!!not employ interrupts to do the cancelation.
         * Instead, cancel() is !!!equivalent to completeExceptionally(new CancellationException()).
         */

        CompletableFuture<String> completableFuture = CompletableFuture.completedFuture("manos")
                .thenApplyAsync(s -> {
                    sleepForMs(100);
                    return s.toUpperCase();
                }, service);


        boolean isCompletedExceptionally = completableFuture.isCompletedExceptionally();
        Assert.assertFalse(isCompletedExceptionally);

        CompletableFuture cancelledCompletableFuture = completableFuture.exceptionally(throwable-> "cancelled message");

        boolean isCancelled = completableFuture.cancel(true);
        isCompletedExceptionally = completableFuture.isCompletedExceptionally();//is true because of we trigger the cancel method above
        Assert.assertTrue(isCancelled);
        Assert.assertTrue(isCompletedExceptionally);
    }

    @Test
    public void applying_a_function_to_result_of_either_of_two_completed_stages(){
        CompletableFuture<String> completableFuture1 = CompletableFuture.completedFuture("manos from 1")
                .thenApplyAsync(s->{
                    sleepForMs(100);
                    return s.toUpperCase();
                }, service);

        //The below example creates a CompletableFuture that applies a Function to the result of either of
        // two previous stages (no guarantees on which one will be passed to the Function).
        CompletableFuture<String> completableFuture2 = CompletableFuture.completedFuture("manos from 2")
                .thenApplyAsync(s->{
                    sleepForMs(50);
                    return s.toUpperCase();
                }, service);
        CompletableFuture<String> completableFutureFromEither = completableFuture1.applyToEither(
                completableFuture2,
                s -> s + " from apply to either"
        );

        String result = completableFutureFromEither.join();
        Assert.assertTrue(result.equals("MANOS FROM 1 from apply to either")|| result.equals("MANOS FROM 2 from apply to either"));
    }

    @Test
    public void accepting_a_function_to_result_of_either_of_two_completed_stages(){
        CompletableFuture<String> completableFuture1 = CompletableFuture.completedFuture("manos from 1")
                .thenApplyAsync(s->{
                    sleepForMs(100);
                    return s.toUpperCase();
                }, service);

        CompletableFuture<String> completableFuture2 = CompletableFuture.completedFuture("manos from 2")
                .thenApplyAsync(s->{
                    sleepForMs(50);
                    return s.toUpperCase();
                }, service);
        StringBuilder sb = new StringBuilder();
        CompletableFuture<Void> completableFutureFromEither = completableFuture1.acceptEither(
                completableFuture2,
                s -> sb.append(s + " from apply to either")
        );

        completableFutureFromEither.join();
        String result = sb.toString();
        Assert.assertTrue(result.equals("MANOS FROM 1 from apply to either")|| result.equals("MANOS FROM 2 from apply to either"));
    }

    @Test
    public void running_runnable_upon_completion_of_both_stages(){
        StringBuilder sb = new StringBuilder();

        CompletableFuture<Void> completableFuture1 = CompletableFuture.completedFuture("manos from")
                .thenAcceptAsync(s->{
                    sleepForMs(100);
                    sb.append(s+" 1 ");
                }, service);

        CompletableFuture<Void> completableFuture2 = CompletableFuture.completedFuture("manos from")
                .thenAcceptAsync(s->{
                    sleepForMs(100);
                    sb.append(s+" 2 ");
                }, service);


        CompletableFuture<Void> completableFutureAfterBoth = completableFuture1.runAfterBoth(completableFuture2, ()->{
            sb.append("done");
        });
        completableFutureAfterBoth.join();
        String result = sb.toString();
        Assert.assertTrue(result.equals("manos from 1 manos from 2 done")||result.equals("manos from 2 manos from 1 done"));
    }

    @Test
    public void accepting_results_of_both_stages_in_a_biConsumer(){
        StringBuilder sb = new StringBuilder();
        CompletableFuture<String> completableFuture1 = CompletableFuture.completedFuture("manos from 1 ")
                .thenApplyAsync(s->{
                    sleepForMs(100);
                    return s.toUpperCase();
                }, service);

        CompletableFuture<String> completableFuture2 = CompletableFuture.completedFuture("manos from 2 ")
                .thenApplyAsync(s->{
                    sleepForMs(50);
                    return s.toUpperCase();
                }, service);

        CompletableFuture<Void> completableFutureFromBoth = completableFuture1.thenAcceptBoth(completableFuture2,
                (resultFrom1, resultFrom2)-> sb.append(resultFrom1).append(resultFrom2));

        completableFutureFromBoth.join();
        String result = sb.toString();
        Assert.assertTrue(result.equals("MANOS FROM 1 MANOS FROM 2 ")||result.equals("MANOS FROM 2 MANOS FROM 1 "));
    }

    @Test
    public void applying_a_bi_function_on_result_of_both_stages() throws ExecutionException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        CompletableFuture<String> completableFuture1 = CompletableFuture.completedFuture("manos from 1 ")
                .thenApplyAsync(s->{
                    sleepForMs(100);
                    return s.toUpperCase();
                });

        CompletableFuture<String> completableFuture2 = CompletableFuture.completedFuture("manos from 2 ")
                .thenApplyAsync(s->{
                    sleepForMs(400);
                    return s.toUpperCase();
                });

        CompletableFuture<String> combinedCompletableFuture = completableFuture1.thenCombine(completableFuture2,
                (result1, result2)->result1+result2);//-> this happen in the main thread;

        Assert.assertEquals("MANOS FROM 1 MANOS FROM 2 ", combinedCompletableFuture.get()/*or .join()*/);
    }

    @Test
    public void composing_completable_futures() throws ExecutionException, InterruptedException {
        StringBuilder sb = new StringBuilder();
        CompletableFuture<String> completableFuture1 = CompletableFuture.completedFuture("manos from 1 ")
                .thenApplyAsync(s->{
                    sleepForMs(ThreadLocalRandom.current().nextInt(400));
                    return s.toUpperCase();
                });
        CompletableFuture<String> completableFuture2 = CompletableFuture.completedFuture("manos from 2 ")
                .thenApplyAsync(s->{
                    sleepForMs(ThreadLocalRandom.current().nextInt(400));
                    return s.toUpperCase();
                });

        /**
         * takes the result from cf1 and use it in another completableFuture
         * in this case cf2
         * thenCompose return a completable future
         */
        String result = completableFuture1
                // thenCompose (string -> completable future)
                .thenCompose(resultFrom1 -> completableFuture2.thenApply(s->s + resultFrom1))
                .get();
        Assert.assertEquals("MANOS FROM 2 MANOS FROM 1 ", result);
    }

    @Test
    public void creating_a_stage_that_completes_when_any_of_several_stages_completes(){
        CompletableFuture<String>[] futureArray = IntStream.rangeClosed(1, 5)
                .mapToObj(i->CompletableFuture.completedFuture("manos from " + i)
                        .thenApplyAsync(s->{
                            sleepForMs(ThreadLocalRandom.current().nextInt(400));
                            return s.toUpperCase();
                        })).toArray(CompletableFuture[]::new);

        CompletableFuture.anyOf(futureArray)
            .whenComplete((result, throwable)->{
                if (throwable == null){
                    Assert.assertTrue(((String)result).startsWith("MANOS FROM "));
                } else {
                    throw new RuntimeException(throwable);
                }
            }).join();
    }

    @Test
    public void creating_a_stage_that_completes_when_all_stages_completes(){
        CompletableFuture<String>[] futureArray = IntStream.rangeClosed(1, 5)
                .mapToObj(i->CompletableFuture.completedFuture("manos from " + i)
                        .thenApplyAsync(s->{
                            sleepForMs(ThreadLocalRandom.current().nextInt(400));
                            return s.toUpperCase();
                        })).toArray(CompletableFuture[]::new);

        List<String> resultList = new ArrayList<>();
        CompletableFuture.allOf(futureArray)
        .whenComplete((cf, throwable)->
            Arrays.stream(futureArray).forEach(f->resultList.add(f.getNow(null)))
        )
        .join();

        Assert.assertEquals(5, resultList.size());
        for(String result: resultList) {
            Assert.assertTrue(result.matches("MANOS FROM [12345]{1}"));
        }
    }



    private static void sleepForMs(long ms){
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static ExecutorService service = Executors.newFixedThreadPool(4);
    @AfterClass
    public static void afterClass(){
        service.shutdown();
        try {
            if (!service.awaitTermination(1000, TimeUnit.MILLISECONDS)){
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }
    }
}
