package manos.examples.gofFunctional;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class Observer {
    private class Observable<A, T>{
        private final ConcurrentMap<A, Consumer<T>> listeners = new ConcurrentHashMap<>();
        public void register(A key, Consumer<T> listener){
            listeners.put(key, listener);
        }
        public void unregister(A key){
            listeners.remove(key);
        }
        public void sendEvent(T event){
            listeners.values().forEach(listener -> listener.accept(event));
        }
    }

    @Test
    public void observable(){
        Observable<String, Integer> observable = new Observable<>();
        List<Integer> resultList = new ArrayList<>();

        observable.register("one", integ -> resultList.add(integ) );
        observable.register("two", integ -> resultList.replaceAll(number -> number*2));

        observable.sendEvent(1);

        Assert.assertEquals(1, resultList.size());
        Assert.assertEquals(2, resultList.get(0).intValue());

        observable.sendEvent(3);

        Assert.assertEquals(2, resultList.size());
        Assert.assertEquals(4, resultList.get(0).intValue());
        Assert.assertEquals(6, resultList.get(1).intValue());

        resultList.clear();
        observable.unregister("one");
        observable.unregister("two");
    }

}
