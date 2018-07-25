package manos.examples.mariofuscopalindrome;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Palindrome {
    @FunctionalInterface
    private interface TailCall<T> extends Supplier<TailCall<T>>{
        default boolean isComplete() {return false;}
        default T result(){ throw new UnsupportedOperationException();}
        default T invoke(){
            return Stream.iterate(this, tailcallSupplier -> tailcallSupplier.get())
                    .filter(tailCall -> tailCall.isComplete())
                    .findFirst()
                    .get()
                    .result();
        }
    }

    private class TerminalCall<T> implements TailCall<T>{
        private final T result;
        public TerminalCall(T result) {
            this.result = result;
        }
        @Override
        public boolean isComplete() {
            return true;
        }
        @Override
        public T result() {
            return result;
        }
        @Override
        public TailCall<T> get() {
            throw new UnsupportedOperationException();
        }
    }

    private  <T> TailCall<T> done(T result){
        return new TerminalCall<T>(result);
    }

    public class PalindromePredicate implements Predicate<String>{
        @Override
        public boolean test(String s) {
            return isPalindrome(s, 0, s.length()-1).invoke();
        }
        private TailCall<Boolean> isPalindrome(String s, int start, int end){
            System.out.println("IsPalindrome with start: '" + s.charAt(start) + "' and end '" + s.charAt(end) + "'");
            while(start<end && !Character.isLetter(s.charAt(start)))
                start++;
            while (start<end && !Character.isLetter(s.charAt(end)))
                end--;
            if (start>end)
                return done(true);

            if (Character.toLowerCase(s.charAt(start)) != Character.toLowerCase(s.charAt(end))) {
                System.out.println("Failed because start: '" + s.charAt(start) + "' != end '" + s.charAt(end) + "'");
                return done(false);
            }

            int newStart = start+1;
            int newEnd = end-1;
            return ()-> isPalindrome(s, newStart, newEnd);
        }
    }


    @Test
    public void testPalindrome(){
        PalindromePredicate pred = new PalindromePredicate();
        Assert.assertTrue(pred.test("k ouo k"));
        //Assert.assertFalse(pred.test("koulok"));
    }
}
