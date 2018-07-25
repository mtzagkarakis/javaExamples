package manos.examples.patterns.decorator;

import org.junit.Assert;
import org.junit.Test;

public class DecoratorPatternTest {
    final String baseString = "A base string: ";

    /**
     * A Decorator pattern can be used to attach additional responsibilities to an object either statically or dynamically.
     * A Decorator provides an enhanced interface to the original object.
     * In the implementation of this pattern, we prefer composition over an inheritance – so that we can reduce the overhead
     * of subclassing again and again for each decorating element.
     * The recursion involved with this design can be used to decorate our object as many times as we require.
     *
     * WHEN:
     *      When we wish to add, enhance or even remove the behavior or state of objects
     *      When we just want to modify the functionality of a single object of class and leave others unchanged
     *
     *      ->Although Proxy and Decorator patterns have similar structures, they differ in intention;
     *          while Proxy’s prime purpose is to facilitate ease of use or controlled access, a Decorator attaches additional responsibilities
     *      ->Both Proxy and Adapter patterns hold reference to the original object
     *      ->All the decorators from this pattern can be used recursively, infinite number of times, which is neither possible with other models
     */
    @Test
    public void decoratorPatternTest(){
        DecoratorInterface decoratorInterfaceImpl = ()->baseString;
        DecoratorInterface decoratorAddedFunctionalityOne = new DecoratorAddedFunctionalityOne(decoratorInterfaceImpl);
        DecoratorInterface decoratorAddedFunctionalityTwo = new DecoratorAddedFunctionalityTwo(decoratorInterfaceImpl);

        Assert.assertEquals(baseString+DecoratorAddedFunctionalityOne.DECORATOR_ADDED_STRING, decoratorAddedFunctionalityOne.decorate());
        Assert.assertEquals(baseString+DecoratorAddedFunctionalityTwo.DECORATOR_ADDED_STRING, decoratorAddedFunctionalityTwo.decorate());
    }
}
