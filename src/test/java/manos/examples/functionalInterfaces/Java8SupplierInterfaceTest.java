package manos.examples.functionalInterfaces;

import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;



/**
 * The Supplier functional interface is yet another Function specialization 
 * that does not take any arguments. It is typically used for lazy generation of values
 * @author Manos
 *
 */
public class Java8SupplierInterfaceTest {
	@Test
	public void apply_supplierInteface_test(){
		Supplier<String> returnMplahSupplier = () -> "MPLAH";
		Assert.assertEquals("MPLAH", returnMplahSupplier.get());
	}
	@Test
	public void apply_supplierInteface_with_StreamGenerate_for_fibonacci_test(){
		final int[] fibs = {0, 1};
		Supplier<Integer> fib_supplier = () -> {
			int result = fibs[1];
			int fib3 = fibs[0] + fibs[1];
			fibs[0] = fibs[1];
			fibs[1] = fib3;
			return result;
		};
		
		Stream<Integer> stream = Stream.generate(fib_supplier).limit(11);
		int[] fibonacci10 = stream.mapToInt(_int->_int.intValue()).toArray();
		Assert.assertEquals(1, fibonacci10[1]);
		Assert.assertEquals(2, fibonacci10[2]);
		Assert.assertEquals(3, fibonacci10[3]);
		Assert.assertEquals(5, fibonacci10[4]);
		Assert.assertEquals(8, fibonacci10[5]);
		Assert.assertEquals(13, fibonacci10[6]);
		Assert.assertEquals(21, fibonacci10[7]);
		Assert.assertEquals(34, fibonacci10[8]);
		Assert.assertEquals(55, fibonacci10[9]);
		Assert.assertEquals(89, fibonacci10[10]);
	}
	
	@Test
	public void apply_INT_supplierInteface_with_StreamGenerate_for_fibonacci_test(){
		final int[] fibs = {0, 1};
		IntSupplier int_fib_supplier = () -> {
			int result = fibs[1];
			int fib3 = fibs[0] + fibs[1];
			fibs[0] = fibs[1];
			fibs[1] = fib3;
			return result;
		};
		int[] fibonacci10 = new int[11];
		for (int i=0; i<11; i++){
			fibonacci10[i] = int_fib_supplier.getAsInt();
		}
		
		Assert.assertEquals(1, fibonacci10[1]);
		Assert.assertEquals(2, fibonacci10[2]);
		Assert.assertEquals(3, fibonacci10[3]);
		Assert.assertEquals(5, fibonacci10[4]);
		Assert.assertEquals(8, fibonacci10[5]);
		Assert.assertEquals(13, fibonacci10[6]);
		Assert.assertEquals(21, fibonacci10[7]);
		Assert.assertEquals(34, fibonacci10[8]);
		Assert.assertEquals(55, fibonacci10[9]);
		Assert.assertEquals(89, fibonacci10[10]);
	}
}
