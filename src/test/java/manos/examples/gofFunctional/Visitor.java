package manos.examples.gofFunctional;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Visitor {

    private class LambdaVisitor<T> implements Function<Object, T>{
        private Map<Class<?>, Function<Object, T>> functionMap = new HashMap<>();
        @Override
        public T apply(Object o) {
            return functionMap.get(o.getClass()).apply(o);
        }
        public <B> Acceptor<T, B> on (Class<B> clazz){
            return new Acceptor<>(this, clazz);
        }
    }
    private class Acceptor<A, B>{
        private final LambdaVisitor visitor;
        private final Class<B> clazz;

        public Acceptor(LambdaVisitor<A> visitor, Class<B> clazz) {
            this.visitor = visitor;
            this.clazz = clazz;
        }
        public LambdaVisitor<A> then (Function<B, A> function){
            visitor.functionMap.put(clazz, function);
            return visitor;
        }
    }

    private class Name {
        public Name(String text) { this.text = text; }
        public String text;
    };
    private class Surname {
        public Surname(String text) { this.text = text; }
        public String text;
    };
    @Test
    public void visitor(){
        Function<Object, String> nameVisitor =
                new LambdaVisitor<String>()
                        .on(Name.class).then(nameClass -> nameClass.text)
                        .on(String.class).then(String::toString)
                        .on(Surname.class).then(surnameClass -> surnameClass.text);

        String result =
                Arrays.asList(new Name("Manos"), new String(" "), new Surname("Tzagkarakis"))
                        .stream()
                        .peek(k -> System.out.println("BEFORE: " + k + " class: " + k.getClass()))
                        .map(nameVisitor)
                        .peek(k -> System.out.println("BEFORE: " + k + " after: " + k.getClass()))
                        .reduce("", (str0, str1) -> str0+str1);

        Assert.assertEquals("Manos Tzagkarakis", result);
    }
}
