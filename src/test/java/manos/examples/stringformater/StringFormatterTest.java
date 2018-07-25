package manos.examples.stringformater;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;

public class StringFormatterTest {
    @Test
    public void format(){
        //all arguments here: https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html
        //syntax %[argument_index$][flags][width][.precision]conversion
        /**
         * -> argument_index part is an integer i – indicating that the ith argument from the argument list should be used here
         * -> flags is a set of characters used for modifying the output format
         * -> width is a positive integer which indicates the minimum number of characters to be written to the output
         * -> precision is an integer usually used to restrict the number of characters, whose specific behavior depends on the conversion
         * -> is the mandatory part. It’s a character indicating how the argument should be formatted.
         *    The set of valid conversions for a given argument depends on the argument’s data type
         */

        //argument position
        Assert.assertEquals("String position 1: one and 2: two", String.format("String position 1: %2$s and 2: %1$s", "two", "one"));

        //date time
        Calendar calendar = new GregorianCalendar(2017, 11 /*for December....*/, 31, 23, 59, 55);
        //%T for time, the lower case d is for date of the month, m is for month, y is for two last digits of year, Y is for the four digits of year
        //%H is for hour of the day (24h, lowercase h for 12h), M is for Minute of hour, S is for seconds (s is for epoch)
        Assert.assertEquals("The date is 31 -- 12 -- 17 -- 2017 -- 23 -- 59 -- 55", String.format("The date is %Td -- %1$Tm -- %1$Ty -- %1$TY -- %1$TH -- %1$TM -- %1$TS", calendar));

        //escaping %
        Assert.assertEquals("result: 50%", String.format("result: 50%%"));

        /**
         * ‘b’ or ‘B’ – for Boolean values
         * ‘h’ or ‘H’ – for HashCode
         * ‘s’ or ‘S’ – for String, if null, it prints “null”, else arg.toString()
         * ‘c’ or ‘C’ – for Character
         * ‘d’ – for decimal number
         * ‘o’ – for octal number
         * ‘X’ or ‘x’ – for hexadecimal number
         * ‘e’ or ‘E’ – formatted as a decimal number in computerized scientific notation
         * ‘f’ – formatted as a decimal number
         * ‘g’ or ‘G’ – based on the precision value after rounding, this conversion formats into computerized
         */

        Assert.assertEquals("Boolean true", String.format("Boolean %b", true));
        Assert.assertEquals("Boolean false", String.format("Boolean %b", null));
        Assert.assertEquals("HashCode of empty String is: 0", String.format("HashCode of empty String is: %h", new String().hashCode()));

        //decimals
        Assert.assertEquals("number 10", String.format("number %d", 10));
        Assert.assertEquals("number 7777", String.format("number %o", 0xFFF));

        //floating point
        Assert.assertEquals("floating point 100 is 1.000000e+02", String.format("floating point 100 is %e", 100.00f));
        Assert.assertEquals("floating point 100.51 is 100.510000", String.format("floating point 100.51 is %f", 100.51d));

        //the %.8g is because when g we count and the decimal part
        Assert.assertEquals("floating point 100.123456789 to 5 decimal points is 100.12346", String.format("floating point 100.123456789 to 5 decimal points is %.8g", 100.123456789d));
        Assert.assertEquals("floating point 100.123456789 to 5 decimal points is 100.12346", String.format("floating point 100.123456789 to 5 decimal points is %.5f", 100.123456789d));

        Assert.assertEquals("Precision wors even in boolean is tr", String.format("Precision wors even in boolean is %.2b", true));
    }

    /**
     * Till now we saw the use of format() method of the Formatter class.
     * We can also create a Formatter instance, and use that to invoke the format() method.
     * We can create an instance by passing in an Appendable, OutputStream, File or file name.
     * Based on this, the formatted String is stored in an Appendable, OutputStream, File respectively.
     * Let’s see an example of using it with an Appendable. We can use it with others in the same way.
     */
    @Test
    public void usingStringBuilderWithFormatterInstance(){
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format("This will go into %s", "sb");
        Assert.assertEquals("This will go into sb", sb.toString());
    }

}
