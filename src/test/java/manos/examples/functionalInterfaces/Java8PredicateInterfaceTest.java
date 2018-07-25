package manos.examples.functionalInterfaces;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;



/**
 * In mathematical logic, a predicate is a function that receives a value and returns a boolean value.
 * @author Manos
 *
 */
public class Java8PredicateInterfaceTest {
	Predicate<Integer> predicateIsOdd = (_integer) -> _integer%2 == 0?false:true;
	Predicate<Integer> predicateIsEven = (_integer) -> _integer%2 == 0?true:false;
	Predicate<Integer> predicateIsZero = (_integer) -> _integer==0?true:false;
	IntPredicate predicateIsIntZero = (_int) -> _int==0?true:false;
	DoublePredicate predicateIsDoubleZero = (_double) -> _double==0.0d?true:false;
	@Test
	public void test_PredicateInteraface_test(){
		Assert.assertTrue(predicateIsOdd.test(11));
		Assert.assertTrue(predicateIsEven.test(10));
		Assert.assertTrue(predicateIsZero.test(0));
		Assert.assertTrue(predicateIsIntZero.test(0));
		Assert.assertTrue(predicateIsDoubleZero.test(0.0d));
		
		Assert.assertFalse(predicateIsOdd.test(4));
		Assert.assertFalse(predicateIsEven.test(33));
		Assert.assertFalse(predicateIsZero.test(1000));
	}
	
	@Test
	public void and_or_PredicateInterface_test(){
		Assert.assertTrue(predicateIsEven.and(predicateIsZero).test(0));
		Assert.assertTrue(predicateIsEven.or(predicateIsZero).test(0));
		Assert.assertTrue(predicateIsEven.or(predicateIsOdd).test(Integer.MAX_VALUE));
		
		Assert.assertFalse(predicateIsOdd.and(predicateIsEven).test(Integer.MAX_VALUE));
		Assert.assertFalse(predicateIsOdd.or(predicateIsZero).test(10));
	}
	
	/**
	 * Predicate.isEqual returns a predicate which is true when the objects are equal
	 */
	@Test
	public void static_ISEQUAL_PredicateInterface_test(){
		Predicate<Integer> isEqualPredicate = Predicate.isEqual(new Integer(10));
		Assert.assertTrue(isEqualPredicate.test(10));
		Assert.assertFalse(isEqualPredicate.test(0));
	}
	
	@Test
	public void negate_PredicateInterface_test(){
		Predicate<Integer> newPredicateIsOdd = predicateIsEven.negate();
		Assert.assertTrue(newPredicateIsOdd.test(11));
		Assert.assertFalse(newPredicateIsOdd.test(2));
	}
}
