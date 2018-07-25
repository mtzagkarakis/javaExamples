package manos.examples.junit.testRunListener;

import org.junit.Assert;
import org.junit.Test;


public class testClassA {
	@Test
	public void testOneOfA(){
		System.out.println("testOneOfA");
		Assert.assertTrue(true);
	}
	
	@Test
	public void testTwoOfA(){
		System.out.println("testTwoOfA");
		Assert.assertTrue(true);
	}
	
	@Test
	public void noAnnotatedTestOfA(){
		System.out.println("noAnnotatedTestOfA");
		Assert.assertTrue(true);
	}
}
