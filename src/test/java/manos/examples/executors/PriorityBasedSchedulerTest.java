package manos.examples.executors;

import mt.streams.StreamUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class PriorityBasedSchedulerTest {

    private class PriorityJobScheduler {
        private final PriorityBlockingQueue<PriorityJob> priorityQueue;
        private final List<Future<String>> resultFutures = new ArrayList<>();
        private volatile boolean run = true;
        PriorityJobScheduler(int queueSize) {
            priorityQueue = new PriorityBlockingQueue<>(queueSize, (pj0, pj1)-> pj1.priority.val.compareTo(pj0.priority.val) );
            priorityJobScheduler.execute(()->{
                while (run){
                    try {
                        PriorityJob job = priorityQueue.take();
                        resultFutures.add(priorityJobSchedulerExecutor.submit(job));
                    } catch (InterruptedException e) {
                        run = false;
                    }
                }
            });
        }
        void scheduleJob(PriorityJob job){
            priorityQueue.add(job);
        }

        List<Future<String>> getResultFutures() {
            return resultFutures;
        }
    }
    @Test
    public void test() throws InterruptedException {
        PriorityJobScheduler scheduler = new PriorityJobScheduler(5);
        Arrays.asList(
                new PriorityJob("1", Priority.LOW),
                new PriorityJob("2", Priority.HIGH),
                new PriorityJob("3", Priority.MID))
                .forEach(scheduler::scheduleJob);

        TimeUnit.SECONDS.sleep(1);

        List<String> jobNamesAsExecuted = scheduler.getResultFutures().stream().map(StreamUtils.unthrowFunction(Future::get)).collect(Collectors.toList());
        Assert.assertEquals(3, jobNamesAsExecuted.size());
        Assert.assertEquals("2", jobNamesAsExecuted.get(0));
        Assert.assertEquals("3", jobNamesAsExecuted.get(1));
        Assert.assertEquals("1", jobNamesAsExecuted.get(2));


    }

    private enum Priority{
        LOW(1), MID(2), HIGH(3);
        private final Integer val;
        Priority(int val) {
            this.val = val;
        }
    }
    private class PriorityJob implements Callable<String>{
        private final String name;
        private final Priority priority;

        PriorityJob(String name, Priority priority) {
            this.name = name;
            this.priority = priority;
        }

        public String getName() {
            return name;
        }

        @Override
        public String call() throws Exception {
            try {
                System.out.println("JOB: " + name + " PRIORITY: " + priority);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return name;
        }
    }

    private static ExecutorService priorityJobScheduler = Executors.newSingleThreadExecutor();
    private static ExecutorService priorityJobSchedulerExecutor = Executors.newFixedThreadPool(10);
    @AfterClass
    public static void afterClass(){
        priorityJobScheduler.shutdown();
        priorityJobSchedulerExecutor.shutdown();
        try {
            if (!priorityJobScheduler.awaitTermination(1, TimeUnit.SECONDS)){
                priorityJobScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            priorityJobScheduler.shutdownNow();
        }
        try {
            if (!priorityJobSchedulerExecutor.awaitTermination(1, TimeUnit.SECONDS))
                priorityJobSchedulerExecutor.shutdownNow();
        } catch (InterruptedException e) {
            priorityJobSchedulerExecutor.shutdownNow();
        }
    }

}
