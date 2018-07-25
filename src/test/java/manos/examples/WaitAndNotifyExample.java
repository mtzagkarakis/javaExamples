package manos.examples;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

public class WaitAndNotifyExample {
    static final long sleepFor = 1000L;
    Thread sleepThread1;
    Thread sleepThread2;
    Thread notifyThread;
    Thread notifyAllThread;

    @Before
    public void before(){
        sleepThread1 = new Thread(this::waiting, "sleepThread1");
        sleepThread2 = new Thread(this::waiting1, "sleepThread2");
        notifyThread = new Thread(this::notifying, "notifyThread");
        notifyAllThread = new Thread(this::notifyingAll, "notifyAllThread");
    }
    synchronized void waiting(){
        try {
            System.out.println("Waiting");
            wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
    synchronized void waiting1(){
        try {
            System.out.println("Waiting1");
            wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
    synchronized void notifying(){
        try {
            Thread.currentThread().sleep(sleepFor);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        notify();
    }
    synchronized void notifyingAll(){
        try {
            Thread.currentThread().sleep(sleepFor);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        notifyAll();
    }
    @Test
    public void waitNotifyExample() throws InterruptedException {
        sleepThread1.start(); //sleep thread1 will wait
        Thread.sleep(100);//wait to a bit to start
        Instant start = Instant.now();
        notifyThread.start();//notify thread will remove the lock from sleepthread1 after sleepFor milliseconds

        sleepThread1.join();//waiting for sleep thread 1 to finish
        System.out.println(sleepThread1.getName() + " finished");

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();
        System.out.println(duration);
        Assert.assertTrue(duration >= sleepFor);
        notifyThread.join();
    }

    @Test
    public void waitNotifyAllExample() throws InterruptedException{
        sleepThread1.start();//sleepThread 1 acquired lock
        sleepThread2.start();//sleepThread 2 acquired lock

        Thread.sleep(100);//wait to a bit to start
        Instant start = Instant.now();
        notifyAllThread.start();//notify thread will remove the lock from sleepthread1 after sleepFor milliseconds

        sleepThread1.join();//waiting for sleep thread 1 to finish
        System.out.println(sleepThread1.getName() + " finished");
        sleepThread2.join();//waiting for sleep thread 2 to finish
        System.out.println(sleepThread2.getName() + " finished");

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();
        System.out.println(duration);
        Assert.assertTrue(duration >= sleepFor);

        notifyAllThread.join();
    }

}
