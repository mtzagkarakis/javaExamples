package manos.examples.functionalInterfaces;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;

import org.junit.Assert;
import org.junit.Test;



/**
 * As opposed to the Supplier, the Consumer accepts a generified argument and returns nothing. 
 * It is a function that is representing side effects.
 * @author Manos
 *
 */
public class Java8ConsumerIntefaceTest {
	@Test
	public void accept_ConsumerInterface_test(){
		List<Integer> list = Arrays.asList(1,2,3,4,5);//immutable array
		final int[] arrayOfOne = new int[1];
		arrayOfOne[0] = 0;
		Consumer<Integer> consumer = (_integer) -> arrayOfOne[0] += _integer.intValue();
		list.forEach(consumer);
		
		Assert.assertEquals(15, arrayOfOne[0]);
	}
	
	@Test
	public void accept_ConsumerInterface_print_test(){
		Consumer<Integer> consumer = (_integer) -> System.out.println(_integer);
		Integer integer = new Integer(10);
		consumer.accept(integer);
		Assert.assertEquals(10, integer.intValue());
	}
	@Test
	public void accept_intComsumerInteface_test(){
		int[] integerArray = {1,2,3,4,5};
		final int[] arrayOfOne = new int[1];
		arrayOfOne[0] = 0;
		IntConsumer consumer = (_integer) -> arrayOfOne[0] += _integer;
		for (int i=0; i<5; i++){
			consumer.accept(integerArray[i]);
		}
		
		Assert.assertEquals(15, arrayOfOne[0]);
	}
	
	@Test 
	public void accept_biConsumerInteface_test(){
		final int[] arrayOfOne = new int[1];
		arrayOfOne[0] = 0;
		BiConsumer<Integer, Double> biConsumer = (_integer, _double)-> arrayOfOne[0] += (_integer.intValue() + _double.intValue());
		Integer[] intArray = {1,2,3,4,5};
		Double[] doubleArray = {1.0d, 2.0d, 3.0d, 4.0d, 5.0d};
		
		for (int i=0; i<5; i++){
			biConsumer.accept(intArray[i], doubleArray[i]);
		}
		
		Assert.assertEquals(30, arrayOfOne[0]);	
	}
	
	@Test
	public void accept_biObjIntConsumerInteface_test(){
		final int[] arrayOfOne = new int[1];
		arrayOfOne[0] = 0;
		
		ObjIntConsumer<Double> intDoubleBiConsumer = (_double, _int) -> arrayOfOne[0] += (_int + _double.intValue());
		
		int[] intArray = {1,2,3,4,5};
		Double[] doubleArray = {1.0d, 2.0d, 3.0d, 4.0d, 5.0d};
		
		for (int i=0; i<5; i++){
			intDoubleBiConsumer.accept(doubleArray[i], intArray[i]);
		}
		
		Assert.assertEquals(30, arrayOfOne[0]);
		
	}
}
