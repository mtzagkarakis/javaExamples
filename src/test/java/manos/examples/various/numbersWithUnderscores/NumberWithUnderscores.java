package manos.examples.various.numbersWithUnderscores;

import org.junit.Assert;
import org.junit.Test;


public class NumberWithUnderscores {
	@Test
	public void test_numbers_with_underscores(){
		int onemillion = 1000000;
		int onemillionUndescor = 1_000_000;
		Assert.assertEquals(onemillion, onemillionUndescor);
	}
}
