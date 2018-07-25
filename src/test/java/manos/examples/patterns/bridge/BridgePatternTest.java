package manos.examples.patterns.bridge;

import manos.examples.patterns.Bridge.AbstractClassProperty;
import manos.examples.patterns.Bridge.BaseChild;
import org.junit.Assert;
import org.junit.Test;

public class BridgePatternTest {
    /**
     * The official definition for Bridge design pattern introduced by Gang of Four (GoF)
     * is to decouple an abstraction from its implementation so that the two can vary independently.
     * This means to create a bridge interface that uses OOP principles to separate out responsibilities into different abstract classes.
     *
     * WHEN:
     *      ->When we want a parent abstract class to define the set of basic rules, and the concrete classes to add additional rules
     *      ->When we have an abstract class that has a reference to the objects, and it has abstract methods that will be defined in each of the concrete classes
     *
     * A Bridge pattern can only be implemented before the application is designed.
     * Allows an abstraction and implementation to change independently whereas an Adapter pattern makes it possible for incompatible classes to work together
     */
    @Test
    public void bridgePatternTest(){
        BaseChild baseChild = new BaseChild((AbstractClassProperty)()->"Manos Tzagkarakis");
        Assert.assertEquals("Manos Tzagkarakis"+BaseChild.PROPERTY, baseChild.calculate());
    }
}
