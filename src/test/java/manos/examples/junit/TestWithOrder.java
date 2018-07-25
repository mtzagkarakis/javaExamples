package manos.examples.junit;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * DEFAULT: Default implementation and the order is not predictable.
 * JVM: This constant leaves the execution of order on JVM.
 * NAME_ASCENDING: This is mostly used constant that sorts the method name in ascending order.
 * MethodSorters.NAME_ASCENDING is the most noteworthy, and especially relevant for users. 
 * It uses Method.toString() method, in case there is a tie breaker (i.e. method with same name) between the method names.
 *
 * here with the @FixMethodOrder(MethodSorters.NAME_ASCENDING) the result must be first test -> second test -> third test. 
 */

//@FixMethodOrder(MethodSorters.DEFAULT)
//@FixMethodOrder(MethodSorters.JVM)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestWithOrder {
	@Test
	public void a_firstTest(){
		System.out.println("first test");
		Assert.assertTrue(true);
	}
	
	@Test
	public void b_secondTest(){
		System.out.println("second test");
		Assert.assertTrue(true);
	}
	
	@Test
	public void c_thirdTest(){
		System.out.println("third test");
		Assert.assertTrue(true);
	}
}
