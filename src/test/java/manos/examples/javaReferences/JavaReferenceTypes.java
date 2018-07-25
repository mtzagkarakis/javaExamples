package manos.examples.javaReferences;

import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JavaReferenceTypes {
    /**
     * A weakly referenced object is cleared by the Garbage Collector when it’s weakly reachable.
     * Weak reachability means that an object has neither strong nor soft references pointing to it.
     * The object can be reached only by traversing a weak reference.
     * First off, the Garbage Collector clears a weak reference, so the referent is no longer accessible.
     * Then the reference is placed in a reference queue (if any associated exists) where we can obtain it from.
     * At the same time, formerly weakly-reachable objects are going to be finalized.
     *
     * As stated by Java documentation, weak references are most often used to implement canonicalizing mappings.
     * A mapping is called canonicalized if it holds only one instance of a particular value.
     * Rather than creating a new object, it looks up the existing one in the mapping and uses it.
     * Of course, the most known use of these references is the WeakHashMap class.
     * It’s the implementation of the Map interface where every key is stored as a weak reference to the given key.
     * When the Garbage Collector removes a key, the entity associated with this key is deleted as well.
     *
     * Another area where they can be used is the Lapsed Listener problem.
     * A publisher (or a subject) holds strong references to all subscribers (or listeners) to notify them about events that happened.
     * The problem arises when a listener can’t successfully unsubscribe from a publisher.
     * Therefore, a listener can’t be garbage collected since a strong reference to it’s still available to a publisher. Consequently, memory leaks may happen.
     * The solution to the problem can be a subject holding a weak reference to an observer allowing the former to be garbage collected
     * without the need to be unsubscribed (note that this isn’t a complete solution, and it introduces some other issues which aren’t covered here).
     *
     * As an example I will create a
     *
     *
     */
    @Test
    public void weakReferenceTest() throws InterruptedException {

        int counter = 0;
        String referencedString = new String("Manos Tzagkarakis");
        WeakReference<String> weakReference = new WeakReference<>(referencedString);
        while (weakReference.get() != null){
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                if (counter++ >= 4){
                    System.out.println("I make reference to string null");
                    referencedString = null;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.gc();
            System.out.println("Thread waiting after triggering gc. Counter " + counter + " weak reference: " + weakReference.get());

        }
        Assert.assertTrue(counter == 5);
    }

    /**
     * A soft reference object (or a softly reachable object) can be cleared by the Garbage Collector in response to a memory demand.
     * A softly reachable object has no strong references pointing to it.
     * When a Garbage Collector gets called, it starts iterating over all elements in the heap. GC stores reference type objects in a special queue.
     * After all objects in the heap get checked, GC determines which instances should be removed by removing objects from that queue mentioned above.
     * These rules vary from one JVM implementation to another, but the documentation states that all soft references to softly-reachable objects are guaranteed
     * to be cleared before a JVM throws an OutOfMemoryError.
     * Though, no guarantees are placed upon the time when a soft reference gets cleared or the order in which a set of such references to different objects get cleared.
     * As a rule, JVM implementations choose between cleaning of either recently-created or recently-used references.
     * Softly reachable objects will remain alive for some time after the last time they are referenced.
     *
     * The default value is a one second of lifetime per free megabyte in the heap.
     *
     * This value can be adjusted using the -XX:SoftRefLRUPolicyMSPerMB flag.
     * For example, to change the value to 2.5 seconds (2500 milliseconds), we can use:
     *      -XX:SoftRefLRUPolicyMSPerMB=2500
     * In comparison to weak references, soft references can have longer lifetimes since they continue to exist until extra memory is required.
     * Therefore, they’re a better choice if we need to hold objects in memory as long as possible.
     *
     *
     * Soft references can be used for implementing memory-sensitive caches where memory management is a very important factor.
     * As long as the referent of a soft reference is strongly reachable, that is – is actually in use, the reference won’t be cleared.
     * A cache can, for example, prevent its most recently used entries from being discarded by keeping strong referents to those entries,
     * leaving the remaining entries to be discarded at the discretion of the Garbage Collector.
     */

    @Test
    @Ignore
    public void softReferenceTest(){
        //this loop should go on forever
        List<SoftReference<String>> softReferenceList = new ArrayList();
        Long counter = 0L;
        boolean shouldContinue = true;
        while (shouldContinue){
            try {
                softReferenceList.add(new SoftReference<String>(counter.toString()));
                counter++;

                if (counter % 100_000 == 0){
                    softReferenceList = softReferenceList.parallelStream()
                            .filter(softRef -> softRef.get() != null)
                            .collect(Collectors.toList());

                }
                System.out.println("Counter: " + counter + " size " + softReferenceList.size());
            } catch (Throwable e){
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Phantom references have two major differences from soft and weak references.
     * We can’t get a referent of a phantom reference.
     * The referent is never accessible directly through the API and this is why we need a reference queue to work with this type of references.
     * The Garbage Collector adds a phantom reference to a reference queue after the finalize method of its referent is executed.
     * It implies that the instance is still in the memory.
     *
     * There’re two common use-cases they are used for.
     * The first technique is to determine when an object was removed from the memory which helps to schedule memory-sensitive tasks.
     * For example, we can wait for a large object to be removed before loading another one.
     * The second practice is to avoid using the finalize method and improve the finalization process.
     */
    private class LargeResource{
        public void clearResource() {
            System.out.println("Resources are cleaned");
        }
    }
    private class CustomPhantomReference extends PhantomReference<LargeResource>{
        boolean resourcesReleased;
        public CustomPhantomReference(LargeResource referent, ReferenceQueue<? super LargeResource> q) {
            super(referent, q);
            resourcesReleased = false;
        }
        public void releaseResources(){
            resourcesReleased = true;
        }

        public boolean isResourcesReleased() {
            LargeResource largeResource;
            if ((largeResource = this.get()) != null){
                largeResource.clearResource();
            }
            System.out.println("Finalized");
            return resourcesReleased;
        }
    }
    @Test
    public void phantomReferenceTest(){
        int memSize = 10;
        //for creating the reference objects - here the CustomPhantomReference which extends Reference
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        //to keep track of the reference object that we create
        List<CustomPhantomReference> references = new ArrayList<>(memSize);
        //to keep track of the objects that we create
        final List<LargeResource> largeResources = new ArrayList<>(memSize);


        //we create the objects and their references
        IntStream.range(0, memSize).forEach(i->{
            LargeResource largeResource = new LargeResource();
            largeResources.add(largeResource);
            references.add(new CustomPhantomReference(largeResource, referenceQueue));
        });


        Assert.assertEquals(0, references.stream().filter(Reference::isEnqueued).count());
        largeResources.clear();
        System.gc(); //gc enqueues the objects to reference Queue
        Assert.assertEquals(memSize, references.stream().filter(Reference::isEnqueued).count());

        //we use the queue to effectively finalize the objects;
        Reference<?> reference;
        while ((reference = referenceQueue.poll()) != null){
            ((CustomPhantomReference) reference).releaseResources();
            reference.clear();
        }

        Assert.assertEquals(memSize, references.stream().filter(CustomPhantomReference::isResourcesReleased).count());

    }




}
