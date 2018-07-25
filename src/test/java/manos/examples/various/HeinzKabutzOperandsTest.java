package manos.examples.various;

import org.junit.Assert;
import org.junit.Test;

public class HeinzKabutzOperandsTest {
	@Test
	public void test(){
		int i=2;
		int j = (i=3)*i;
		Assert.assertEquals(9, j);
	}
	
	@Test
	public void test2(){
		int a =9;
		a += (a=3);
		//a = a(9) + (a=3)
		Assert.assertEquals(12, a);
		
		int b = 9;
		b = b + (b=3);
		Assert.assertEquals(12, b);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void test3(){
		int j=1;
		try {
			int i = forgetIt() + (j=2);
		} catch (Exception e){
			Assert.assertEquals(1, j);
		}
		
		j=1;
		try {
			int i = (j=2) + forgetIt();
		} catch (Exception e){
			Assert.assertEquals(2, j);
		}
	}
	
	private int forgetIt() throws Exception{
		throw new Exception("I am out of here");
	}
}
