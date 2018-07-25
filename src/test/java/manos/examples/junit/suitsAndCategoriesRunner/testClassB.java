package manos.examples.junit.suitsAndCategoriesRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class testClassB {
	@Test
	@Category(ICategoryOne.class)
	public void testOneOfB(){
		System.out.println("testOneOfB");
		Assert.assertTrue(true);
	}
	
	@Test
	@Category(ICategoryTwo.class)
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
