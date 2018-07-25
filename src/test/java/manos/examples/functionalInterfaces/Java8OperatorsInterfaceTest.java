package manos.examples.functionalInterfaces;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

import org.junit.Assert;
import org.junit.Test;



/**
 * Represents an operation on a single operand that produces a result of the same type as its operand. 
 * This is a specialization of Function for the case where the operand and result are of the same type.
 * This is a functional interface whose functional method is Function.apply(Object).
 * 
 * Unary extends Function Interface
 * Binary extends BiFunction Interface
 * @author Manos
 *
 */
public class Java8OperatorsInterfaceTest {
	@Test
	public void apply_UnaryOperatorInterface_test(){
		UnaryOperator<String> toUpperCaseOperator = _str -> _str.toUpperCase();
		List<String> strList = Arrays.asList("Manos", "Tzagkarakis");//the list is immutable not its values
		strList.replaceAll(toUpperCaseOperator);
		Assert.assertEquals("MANOS", strList.get(0));
		Assert.assertEquals("TZAGKARAKIS", strList.get(1));
		
		//or with method reference
		strList.replaceAll(String::toLowerCase);
		Assert.assertEquals("manos", strList.get(0));
		Assert.assertEquals("tzagkarakis", strList.get(1));
	}
	
	@Test
	public void apply_intUnaryOperatorInterface_test(){
		IntUnaryOperator plusOneOperator = _int -> _int + 1;
		Assert.assertEquals(11, plusOneOperator.applyAsInt(10));
		
		//this will work
		plusOneOperator = _int -> ++_int;
		Assert.assertEquals(11, plusOneOperator.applyAsInt(10));
		
		//this wont work
		plusOneOperator = _int -> _int++;
		Assert.assertNotEquals(11, plusOneOperator.applyAsInt(10));
	}
	
	/**
	 * Identify returns a UnaryOperator which always return the same value applied
	 */
	@Test
	public void appy_identify_UnaryOperatorInterface_test(){
		UnaryOperator<Integer> sameIntegerOp = UnaryOperator.identity();
		Assert.assertEquals(10, sameIntegerOp.apply(10).intValue());
	}
	/**
	 * Represents an operation upon two operands of the same type, producing a result of the same type as the operands. 
	 * This is a specialization of BiFunction for the case where the operands and the result are all of the same type.
	 * This is a functional interface whose functional method is BiFunction.apply(Object, Object).
	 */
	@Test 
	public void apply_binaryOperator_test(){
		BinaryOperator<Integer> binaryOperatorSum = (_integer1, _integer2) -> _integer1 + _integer2;
		Assert.assertEquals(15, binaryOperatorSum.apply(10, 5).intValue());
	}
	
	@Test
	public void apply_maxBy_minBy_static_binaryOperator_test(){
		Comparator<Integer> comp = new Comparator<Integer>() {
			@Override
			public int compare(Integer _integer1, Integer _integer2){
				if (_integer1.equals(_integer2))
					return 0;
				if (_integer1 > _integer2)
					return 1;
				else 
					return -1;
			}
		};
		
		BinaryOperator<Integer> maxByOperator = BinaryOperator.maxBy(comp) ;
		BinaryOperator<Integer> minByOperator = BinaryOperator.minBy(comp) ;
		Assert.assertEquals(5, maxByOperator.apply(3, 5).intValue());
		Assert.assertEquals(5, maxByOperator.apply(5, 3).intValue());
		Assert.assertEquals(5, maxByOperator.apply(5, 5).intValue());
		
		Assert.assertEquals(3, minByOperator.apply(3, 5).intValue());
		Assert.assertEquals(3, minByOperator.apply(5, 3).intValue());
		Assert.assertEquals(5, minByOperator.apply(5, 5).intValue());
	}
}
