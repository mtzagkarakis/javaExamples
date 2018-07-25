package manos.examples.deque;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Under the hood, the ArrayDeque is backed by an array which doubles its size when it gets filled.
 * Initially, the array is initialized with a size of 16. It’s implemented as a double-ended queue where it maintains two pointers namely a head and a tail.
 *
 * An ArrayDeque (also known as an “Array Double Ended Queue”, pronounced as “ArrayDeck”) is a special kind of a growable array
 * that allows us to add or remove an element from both sides.
 *
 * Operation	            Method	        Method throwing Exception
 * Insertion from Head	    offerFirst(e)	addFirst(e)
 * Removal from Head	    pollFirst()	    removeFirst()
 * Retrieval from Head	    peekFirst()	    getFirst()
 * Insertion from Tail	    offerLast(e)	addLast(e)
 * Removal from Tail	    pollLast()	    removeLast()
 * Retrieval from Tail	    peekLast()	    getLast()
 *
 * Finally, a few more notes worth understanding and remembering about this particular implementation:
 * It’s not thread-safe
 * Null elements are not accepted
 * Works significantly faster than the synchronized Stack
 * Is a faster queue than LinkedList due to the better locality of reference
 * Most operations have amortized constant time complexity
 * An Iterator returned by an ArrayDeque is fail-fast
 * ArrayDeque automatically doubles the size of an array when head and tail pointer meets each other while adding an element
 */
public class ArrayDequeTest {
    Deque<String> deque;
    @Before
    public void before(){
        deque = new ArrayDeque<>();
    }

    @Test
    public void test_insertion_front(){
        //when
        deque.addFirst("0");
        Assert.assertTrue(deque.offerFirst("1"));
        deque.push("2");

        //then
        Assert.assertTrue(deque.size()==3);
        Assert.assertEquals("2", deque.element());
        Assert.assertEquals("0", deque.getLast());
    }

    @Test
    public void test_insertion_front_2(){
        //when change order expect the same result
        Assert.assertTrue(deque.offerFirst("0"));
        deque.push("1");
        deque.addFirst("2");

        //then
        Assert.assertTrue(deque.size()==3);
        Assert.assertEquals("2", deque.peek());
        Assert.assertEquals("0", deque.peekLast());
    }

    @Test
    public void test_insertion_back(){
        //when
        deque.addLast("0");
        Assert.assertTrue(deque.offerLast("1"));
        Assert.assertTrue(deque.add("2"));
        Assert.assertTrue(deque.offer("3"));

        //then
        Assert.assertTrue(deque.size()==4);
        Assert.assertEquals("0", deque.peek());
        Assert.assertEquals("3", deque.peekLast());
    }

    @Test
    public void test_insertion_back_2(){
        //when change order expect the same result
        //when
        Assert.assertTrue(deque.add("0"));
        deque.addLast("1");
        Assert.assertTrue(deque.offer("2"));
        Assert.assertTrue(deque.offerLast("3"));

        //then
        Assert.assertTrue(deque.size()==4);
        Assert.assertEquals("0", deque.peek());
        Assert.assertEquals("3", deque.peekLast());
    }

    @Test
    public void poll_remove_first_from_deque(){
        //when
        deque.add("1");
        deque.add("2");
        deque.add("3");
        deque.add("4");
        deque.add("5");
        deque.add("6");


        //then
        Assert.assertTrue(deque.size()==6);
        Assert.assertEquals("1", deque.pollFirst());
        Assert.assertEquals("2", deque.removeFirst());
        Assert.assertEquals("3", deque.remove());
        Assert.assertEquals("4", deque.poll());
        Assert.assertEquals("5", deque.pop());
        Assert.assertTrue(deque.size()==1);
        Assert.assertEquals("6", deque.element());
        Assert.assertTrue(deque.size()==1);
    }

    @Test
    public void poll_remove_last_from_deque(){
        //when
        deque.add("1");
        deque.add("2");
        deque.add("3");


        //then
        Assert.assertTrue(deque.size()==3);
        Assert.assertEquals("3", deque.pollLast());
        Assert.assertEquals("2", deque.removeLast());
        Assert.assertTrue(deque.size()==1);
        Assert.assertEquals("1", deque.element());
        Assert.assertTrue(deque.size()==1);
    }

    @Test
    public void remove_first_or_last_occurrence(){
        deque.add("1");
        deque.add("0");
        deque.add("2");
        deque.add("0");
        deque.add("2");
        deque.add("1");

        Assert.assertTrue(deque.size()==6);
        Assert.assertTrue(deque.removeFirstOccurrence("0"));
        Assert.assertTrue(deque.removeLastOccurrence("2"));

        Assert.assertTrue(deque.size()==4);
        Assert.assertEquals("1", deque.getFirst());
        Assert.assertEquals("1", deque.getLast());

        Assert.assertTrue(deque.removeFirstOccurrence("1"));
        Assert.assertTrue(deque.removeLastOccurrence("1"));

        Assert.assertTrue(deque.size()==2);
        Assert.assertEquals("2", deque.getFirst());
        Assert.assertEquals("0", deque.getLast());
    }
}
