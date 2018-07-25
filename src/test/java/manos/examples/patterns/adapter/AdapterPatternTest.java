package manos.examples.patterns.adapter;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Supplier;

public class AdapterPatternTest {
    /**
     * An Adapter pattern acts as a connector between two incompatible interfaces that otherwise cannot be connected directly.
     * An Adapter wraps an existing class with a new interface so that it becomes compatible with client’s interface.
     * The main motive behind using this pattern is to convert an existing interface into another interface that client expects.
     * It’s usually implemented once the application is designed.
     *
     * WHEN:
     *      ->When an outside component provides captivating functionality that we’d like to reuse, but it’s incompatible with our current application.
     *          A suitable Adapter can be developed to make them compatible with each other
     *      ->When our application is not compatible with the interface that our client is expecting
     *      ->When we want to reuse legacy code in our application without making any modification in the original code
     *
     * While proxy provides the same interface, Adapter provides a different interface that’s compatible with its client
     * Adapter pattern is used after the application components are designed so that we can use them without modifying the source code.
     * This is in contrast to Bridge pattern, which is used before the components are designed.
     */
    @Test
    public void adapterPatternTest(){
        AdapterInterface adapterInterface = new AdapterImpl();
        AdapterAlterInterface adapterAlterInterface = new AdapterAlterImpl(adapterInterface);

        Assert.assertEquals(AdapterImpl.NAME, adapterInterface.getMyName());
        Assert.assertEquals(AdapterImpl.NAME.toLowerCase(), adapterAlterInterface.getMyName());
    }

    @Test
    public void adapterPatternFunctionalTest(){
        AdapterInterface adapterInterface = () -> "Manos";
        AdapterAlterInterface adapterAlterInterface = () -> adapterInterface.getMyName().toLowerCase();

        Assert.assertEquals("manos", adapterAlterInterface.getMyName());
    }

    @Test
    public void adapterPatternFunctionalTestWithoutCustomInterfaces(){
        Supplier<String> adapterInterface = () -> "Manos";
        Supplier<String> adapterAlterInterface = () -> adapterInterface.get().toLowerCase();

        Assert.assertEquals("manos", adapterAlterInterface.get());
    }
}
