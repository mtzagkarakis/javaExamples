package manos.examples.functionalInterfaces;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.ToIntBiFunction;

import org.junit.Assert;
import org.junit.Test;

import manos.examples.functionalInterfaces.model.ClassWithFunctionArg;

public class Java8FunctionInterfaceTest {
	@Test
	public void implemented_functionInterfaceTest(){
		Map<String, Integer> map = new HashMap<>();
		map.computeIfAbsent("John", s->s.length());
		Assert.assertEquals(4, map.get("John").intValue());
	}
	@Test
	public void custom_class_functionInterfaceTest(){
		ClassWithFunctionArg cwfa = new ClassWithFunctionArg();
		
		String returnS = cwfa.functionWithFunctionArg(10, (_integer)-> _integer.toString());
		Assert.assertEquals("10", returnS);
		
		Function<Integer, String> intToString = (_integer) -> _integer.toString();
		returnS = cwfa.functionWithFunctionArg(10, intToString);
		Assert.assertEquals("10", returnS);
		
		returnS = cwfa.functionWithFunctionArg(10, Object::toString);
		//using Object::toString instead of Integer::toString because the
		//latter is ambiguous Integer.toString() and Integer.toString(int no)
		Assert.assertEquals("10", returnS);
	}
	@Test
	public void apply_functionInterface_test(){
		Function<String, Integer> countStringLength = (str) -> str.length();
		Function<String, String> cropStringFirstChar = (str) -> str.substring(1);
		Function<String, String> add2UnderscoreAtStart = (str) -> "__" + str;
		
		//apply -> calls the defined function
		Assert.assertEquals(4, countStringLength.apply("MPLA").intValue());
		
		//andThen -> calls the andThen function after calling the defining function
		Assert.assertEquals(3, cropStringFirstChar.andThen(countStringLength).apply("MPAH").intValue());
		
		//compose -> calls the compose argument first and pass the result to callser
		Assert.assertEquals("_MPLA", cropStringFirstChar.compose(add2UnderscoreAtStart).apply("MPLA"));
		
		//identify -> static function that always return its input
		Assert.assertEquals("MPLA", Function.identity().apply("MPLA"));
		
	}
	
	/**
	 * Since a primitive type can’t be a generic type argument, 
	 * there are versions of the Function interface for most used primitive types 
	 * double, int, long, and their combinations in argument and return types
	 */
	@Test
	public void apply_primitive_functionInterface_test(){
		//this take a primitive type as input and return an generic type (in this example Strings)
		IntFunction<String> intToStringFunction = (prim_int)->(new Integer(prim_int)).toString();
		LongFunction<String> longToStringFunction = (prim_long) -> (new Long(prim_long)).toString();
		DoubleFunction<String> doubleToStringFunction = (prim_double) -> (new Double(prim_double)).toString();
		
		Assert.assertEquals("10", intToStringFunction.apply(10));
		Assert.assertEquals("1000", longToStringFunction.apply(1000L));
		Assert.assertEquals("10.0", doubleToStringFunction.apply(10.00d));
	}
	
	@Test
	public void apply_primitive_to_primitive_functionInterface_test(){
		//this take a primitive type as input and return another primitive type
		//there are others like: 
		//DoubleToIntFunction, DoubleToLongFunction, IntToDoubleFunction, IntToLongFunction, LongToIntFunction, LongToDoubleFunction
		DoubleToIntFunction doubleToIntFunction = (prim_double) -> (new Double(prim_double)).intValue();
		
		Assert.assertEquals(10, doubleToIntFunction.applyAsInt(10.0d));
	}
	
	/**
	 * along with the aboves there are the biFunction Interfaces which take 
	 * a two parameters
	 */
	@Test
	public void apply_biFunctionInterface_test(){
		BiFunction<Integer, Double, String> intPlusDoubleToString = (integer, _double) -> (new Double((integer.intValue() + _double.intValue()))).toString();
		Assert.assertEquals("10.0", intPlusDoubleToString.apply(5, 5.0d));
	}
	//and bifunctionPrimitives
	@Test
	public void apply_primitive_biFunctionInteface_test(){
		ToIntBiFunction<Double, Double> toIntBiFuction = (_double1, _double2)-> (_double1.intValue() + _double2.intValue());
		Assert.assertEquals(10, toIntBiFuction.applyAsInt(new Double(4), new Double(6)));
	}
}
