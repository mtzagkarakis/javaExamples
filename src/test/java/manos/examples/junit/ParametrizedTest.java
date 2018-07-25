package manos.examples.junit;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ParametrizedTest {
	private int value;
    private boolean isEven;
 
    public ParametrizedTest(int value, boolean isEven) {
        this.value = value;
        this.isEven = isEven;
    }
 
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{{1, false}, {2, true}, {4, true}};
        return Arrays.asList(data);
    }
 
    /**
     * This test will be run 3 times (as the array returned in the data function)
     * with parametrized.class each "row" of the array will be passed to the constructor of the test
     * 
     * Note that the declaration of the parameter has to be static and it has to be passed to the constructor 
     * in order to initialize the class member as value for testing. 
     * In addition to that, the return type of parameter class must be a List, and the values are limited to String or primitive data types.
     */
    @Test
    public void givenParametrizedNumber_ifEvenCheckOK_thenCorrect() {
    	System.out.println("test with values: isEven " + isEven + " value " + value);
        Assert.assertEquals(isEven, value % 2 == 0);
    }
}
