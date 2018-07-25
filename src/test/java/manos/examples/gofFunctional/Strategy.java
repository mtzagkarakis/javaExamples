package manos.examples.gofFunctional;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Strategy {
    private String textFormatter(String text, Predicate<String> filter, UnaryOperator<String> format){
        if (filter.test(text))
            return format.apply(text);
        return text;
    }

    @Test
    public void strategy(){
        String testStr = "A case with both Upper AND lowercase characters";
        Assert.assertEquals("a case with both upper and lowercase characters", textFormatter(testStr, text->text.matches(".*[A-Z].*"), String::toLowerCase));
        Assert.assertEquals("A CASE WITH BOTH UPPER AND LOWERCASE CHARACTERS", textFormatter(testStr, text->true, String::toUpperCase));
    }
}
