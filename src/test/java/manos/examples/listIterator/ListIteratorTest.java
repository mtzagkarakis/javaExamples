package manos.examples.listIterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ListIteratorTest {
    List<String> aList;
    @Before
    public void before(){
        aList = new ArrayList<>();
        aList.add("one");
        aList.add("two");
        aList.add("three");
        aList.add("four");
        aList.add("five");
    }

    @Test
    public void listIteratorForwardExample(){
        ListIterator<String> listIterator = aList.listIterator();//listIterator( .. number ..) starts from specified position
        while (listIterator.hasNext()){
            if (listIterator.next().equalsIgnoreCase("two")) {
                listIterator.set("2");
                break;
            } else {
                listIterator.remove();
            }
        }
        Assert.assertEquals(4, aList.size());
        Assert.assertEquals("2", aList.get(0));
        Assert.assertEquals("five", aList.get(3));
    }

    @Test
    public void listIteratorBackExample(){
        ListIterator<String> listIterator = aList.listIterator(aList.size());
        while (listIterator.hasPrevious()){
            if (listIterator.previous().equalsIgnoreCase("three")) {
                listIterator.set("3");
                break;
            } else {
                listIterator.remove();
            }
        }

        Assert.assertEquals(3, aList.size());
        Assert.assertEquals("one", aList.get(0));
        Assert.assertEquals("two", aList.get(1));
        Assert.assertEquals("3", aList.get(2));
    }
}
