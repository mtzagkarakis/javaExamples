package manos.examples.junit.suitsAndCategoriesRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


public class testClassA {
	@Test
	@Category(ICategoryOne.class)
	public void testOneOfA(){
		System.out.println("testOneOfA");
		Assert.assertTrue(true);
	}
	
	@Test
	@Category(ICategoryTwo.class)
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
