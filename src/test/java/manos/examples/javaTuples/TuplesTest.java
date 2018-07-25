package manos.examples.javaTuples;

import java.util.Arrays;
import java.util.List;

import org.javatuples.Triplet;
import org.junit.Assert;
import org.junit.Test;
public class TuplesTest {
	@Test
	public void test(){
		Triplet<String, Integer, Double> triple = new Triplet<>("String", 0, 0.0d);
		Assert.assertEquals(0, triple.getValue1().intValue());
		//or
		triple = Triplet.with("String", 1, 1.0d);
		Assert.assertEquals(1, triple.getValue1().intValue());
		
		//or from collection
		List<String> strings = Arrays.asList("one", "two", "three");
		Triplet<String, String, String> tripleStr = Triplet.fromCollection(strings);
		Assert.assertEquals("two", tripleStr.getValue1());
	}
}
