package manos.examples.junit.testRunListener;

import org.junit.Assert;
import org.junit.Test;

public class testClassB {
	@Test
	public void testOneOfB(){
		System.out.println("testOneOfB");
		Assert.assertTrue(true);
	}
	
	@Test
	public void testTwoOfB(){
		System.out.println("testTwoOfB");
		Assert.assertTrue(true);
	}
	
	@Test
	public void noAnnotatedTestOfB(){
		System.out.println("noAnnotatedTestOfB");
		Assert.assertTrue(true);
	}
}
