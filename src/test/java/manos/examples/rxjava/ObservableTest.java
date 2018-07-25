package manos.examples.rxjava;

import org.junit.Assert;
import org.junit.Test;
import rx.Observable;
import rx.observables.BlockingObservable;
import rx.observables.ConnectableObservable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Observable represents any object that can get data from a data source and whose state may be of interest in a way that other objects may register an interest
 * An observer is any object that wishes to be notified when the state of another object changes
 *
 * Non-Blocking –>  asynchronous execution is supported and is allowed to unsubscribe at any point in the event stream.
 *                      On this article, we’ll focus mostly on this kind of type
 * Blocking –>      all onNext observer calls will be synchronous, and it is not possible to unsubscribe in the middle of an event stream.
 *                      We can always convert an Observable into a Blocking Observable, using the method toBlocking:
 */
public class ObservableTest {
    @Test
    public void create_observable_with_just(){
        List<String> results = new ArrayList<>();
        Observable<String> observable = Observable.just("Hello_world");
        observable.subscribe(results::add);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Hello_world", results.stream().findFirst().orElse(""));
    }

    @Test
    public void create_observable_with_from(){
        List<String> results = new ArrayList<>();
        Observable<String> observable = Observable.from((String[])Arrays.asList("Hello", " ","World" ," ", "of", " ", "observables!").toArray());
        observable.subscribe(results::add);

        Assert.assertEquals(7, results.size());
        Assert.assertEquals("Hello World of observables!", results.stream().collect(Collectors.joining()));

        results.clear();
        observable.subscribe(
                i -> results.add(i),//on next
                Throwable::printStackTrace, //on error
                () -> results.removeIf(str->str.equals(" "))//on completed
        );
        Assert.assertEquals(4, results.size());
        Assert.assertEquals("HelloWorldofobservables!", results.stream().collect(Collectors.joining()));
    }

    @Test
    public void observable_map_operation(){
        List<String> results = new ArrayList<>();
        Observable.from((String[])Arrays.asList("Hello", " ","World" ," ", "of", " ", "observables!").toArray())
                .map(String::toUpperCase)
                .subscribe(results::add, Throwable::printStackTrace, ()->results.removeIf(str->str.equals(" ")));
        Assert.assertEquals(4, results.size());
        Assert.assertEquals("HELLOWORLDOFOBSERVABLES!", results.stream().collect(Collectors.joining()));
    }

    @Test
    public void observable_flat_map_operation(){
        List<String> results = new ArrayList<>();
        Observable.from((String[])Arrays.asList("Hello", " ","World" ," ", "of", " ", "observables!").toArray())
                .flatMap(str->Observable.just(str)) // unneeded mostly for illustration
                .map(String::toUpperCase)
                .subscribe(results::add, Throwable::printStackTrace, ()->results.removeIf(str->str.equals(" ")));
        Assert.assertEquals(4, results.size());
        Assert.assertEquals("HELLOWORLDOFOBSERVABLES!", results.stream().collect(Collectors.joining()));
    }

    @Test
    public void observable_scan_operation(){
        List<Integer> results = new ArrayList<>();
        Observable<Integer> observable = Observable.from((Integer[]) Arrays.asList(1,2,3,4,5).toArray());
        observable.scan(0, (s0, s1)-> {
            //returns 1
            //returns 3
            //returns 6
            //returns 10
            //returns 15
            return s0+s1;
        }).reduce(0, (s0, s1)->s0+s1) //adds 1+3+6+10+15=35
                .subscribe(results::add,
                Throwable::printStackTrace,
                () -> Assert.assertTrue(results.stream().findFirst().orElse(0)==35));
    }

    @Test
    public void observable_group_by_operation(){
        Map<String, Integer> results = new HashMap<>();
        results.put("ODD", 0);
        results.put("EVEN", 0);

        Observable.from((Integer[]) Arrays.asList(1,2,3,4,5,6).toArray())
            .groupBy(integer -> integer%2==0?"EVEN":"ODD")
                .subscribe(groupedObservable->
                        groupedObservable.subscribe(
                                integer-> {
                                    if (groupedObservable.getKey().equals("ODD")){
                                        results.computeIfPresent("ODD", (s, val) -> val+integer);
                                    } else {
                                        results.computeIfPresent("EVEN", (s, val) -> val+integer);
                                    }
                                }  ));

        Assert.assertTrue(results.get("ODD")==9);
        Assert.assertTrue(results.get("EVEN")==12);
    }

    @Test
    public void observable_filter_operation(){
        List<Integer> results = new ArrayList<>();
        Observable.from((Integer[]) Arrays.asList(1,2,3,4,5,6).toArray())
                .filter(integer -> integer%2==0)
                .reduce(0, (i0, i1)->i0+i1)
                .subscribe(results::add);

        Assert.assertEquals(1, results.size());
        Assert.assertTrue(results.stream().findFirst().orElse(0)==12);
    }

    @Test
    public void observable_conditionals_empty_first_last(){//they are like optionals
        List<String> results = new ArrayList<>();
        Observable.<String>empty().defaultIfEmpty("empty").subscribe(results::add);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("empty", results.stream().findFirst().orElse(""));
        results.clear();

        Observable.from((String[])Arrays.asList("Hello", " ","World" ," ", "of", " ", "observables!").toArray())
                .first()
                .defaultIfEmpty("empty")
                .subscribe(results::add);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Hello", results.stream().findFirst().orElse(""));
        results.clear();

        Observable.from((String[])Arrays.asList("Hello", " ","World" ," ", "of", " ", "observables!").toArray())
                .last()
                .defaultIfEmpty("empty")
                .subscribe(results::add);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("observables!", results.stream().findFirst().orElse(""));
        results.clear();
    }

    @Test
    public void observable_while_operator(){
        //contains
        Assert.assertTrue(Observable.from((Integer[]) Arrays.asList(1,2,3,4,5).toArray()).contains(3).toBlocking().first());

        //take while
        BlockingObservable<Integer> observable = Observable.from((Integer[]) Arrays.asList(1,2,3,4,5).toArray())
            .takeWhile(i->i!=3)
            .toBlocking();
        Assert.assertTrue(observable.first()==1);
        Assert.assertTrue(observable.last()==2);

        observable = Observable.from((Integer[]) Arrays.asList(1,2,3,4,5).toArray())
                .skipWhile(i->i!=3)
                .toBlocking();
        Assert.assertTrue(observable.first()==3);
        Assert.assertTrue(observable.last()==5);

        observable = Observable.from((Integer[]) Arrays.asList(1,2,3,4,5).toArray())
                .takeUntil(i->i==3)
                .toBlocking();

        Assert.assertTrue(observable.first()==1);
        Assert.assertTrue(observable.last()==3);
    }

    @Test
    public void connectable_observables() throws InterruptedException {
        /**
         * A ConnectableObservable resembles an ordinary Observable,
         * except that it doesn’t begin emitting items when it is subscribed to, but only when the connect operator is applied to it.
         *
         * internal -> create an Observable that emits a sequence of integers spaced by a given time interval
         * publish  -> creates the connectable observable
         */
        List<Long> results = new ArrayList<>();
        ConnectableObservable<Long> connectableObservable = Observable.interval(50, TimeUnit.MILLISECONDS).publish();

        connectableObservable.subscribe(results::add);
        Assert.assertTrue(results.isEmpty());

        connectableObservable.connect();//will start serving objects after connect but the subscribe action must be implemented before connect
        TimeUnit.SECONDS.sleep(1);
        //at most 20, 50ms intervals will have to fit in a Second so...

        Assert.assertTrue(results.size()>18);
    }

    @Test
    public void observable_singles(){
        /**
         * Single is like an Observable who, instead of emitting a series of values, emits one value or an error notification.
         *
         * With this source of data, we can only use two methods to subscribe:
         *      OnSuccess returns a Single that also calls a method we specify
         *      OnError also returns a Single that immediately notifies subscribers of an error
         *
         */
        List<String> results = new ArrayList<>();
        Observable.just("Hello")
                .toSingle()
                .doOnSuccess(results::add)
                .doOnError(Throwable::printStackTrace)
                .doOnSubscribe(()-> System.out.println("Subscribed"))
                .subscribe();
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Hello", results.get(0));
    }
}
