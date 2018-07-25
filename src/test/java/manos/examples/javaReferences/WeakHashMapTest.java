package manos.examples.javaReferences;

import org.junit.Test;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.*;

/**
 *  WeakHashMap has the property that when a reference to the key is not used
 *  then the record gets deleted
 *
 *  In the following example I made null the reference and after a while we see
 *  that the record gets deleted
 */
public class WeakHashMapTest {
    @Test
    public void test() throws InterruptedException {
        WeakHashMap<Integer, String> weakHashMap = new WeakHashMap<>();
        Integer[] integerOne = new Integer[]{1};
        weakHashMap.put(integerOne[0], "one");
        AtomicInteger counter = new AtomicInteger(1);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(weakHashMap.containsKey(integerOne[0])){
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                        if (counter.getAndAdd(1) > 10){
                            integerOne[0] = null;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Thread waiting. Will gc "+ counter.get());
                    System.gc();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println("Main thread is waiting");
        thread.join();
    }
}
