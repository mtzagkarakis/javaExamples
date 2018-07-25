package manos.examples.gofFunctional;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Command {
    StringBuilder builder = new StringBuilder();
    String result = "";
    public void add(String message) {
        builder.append(message);
    }
    public void clear(){
        builder = new StringBuilder();
        result = "";
    }

    public void saveResult() {
        result = builder.toString();
    }


    @Test
    public void testCommand(){
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(()-> add("Hello Command Pattern"));
        tasks.add(()-> add(" "));
        tasks.add(()-> add("I am manos"));
        tasks.add(()->saveResult());

        tasks.forEach(Runnable::run);
        Assert.assertTrue(result.equals("Hello Command Pattern I am manos"));

        tasks.add(()->clear());
        tasks.add(()->add("New"));
        tasks.forEach(Runnable::run);
        Assert.assertTrue(result.equals(""));

        tasks.add(()->saveResult());
        tasks.forEach(Runnable::run);
        Assert.assertTrue(result.equals("New"));

    }
}
