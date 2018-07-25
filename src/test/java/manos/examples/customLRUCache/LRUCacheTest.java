package manos.examples.customLRUCache;

import mt.streams.StreamUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LRUCacheTest {
    private static final ExecutorService service = Executors.newFixedThreadPool(4);
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
    @Test
    public void testLruCache(){
        final int maxEntries = 5;
        Map<Integer, String> cacheMap = LRUCache.<Integer, String>getInstance(maxEntries);

        AtomicInteger counter = new AtomicInteger(0);


        List<Future<String>> futures = IntStream.rangeClosed(0, 100)
                .mapToObj(i->service.submit(()->cacheMap.put(counter.getAndAdd(1), "A string")))
                .collect(Collectors.toList());
        futures.forEach(StreamUtils.unthrowConsumer(Future::get));

        Assert.assertEquals(maxEntries, cacheMap.size());
        Assert.assertTrue(cacheMap.containsKey(100));
        Assert.assertTrue(cacheMap.containsKey(99));
        Assert.assertTrue(cacheMap.containsKey(98));
        Assert.assertTrue(cacheMap.containsKey(97));
        Assert.assertTrue(cacheMap.containsKey(96));

        cacheMap.entrySet().forEach(System.out::println);
    }
}
