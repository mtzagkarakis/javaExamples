package manos.examples.streams.collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.junit.Assert;
import org.junit.Test;


public class CustomImmutableListCollectorTest {

	public static <T, A extends List<T>> Collector<T, A, List<T>> toImmutableList(Supplier<A> supplier){
		return Collector.of(
				supplier,
				(list, i)-> list.add(i), 
				(impartialList0, impartialList1)->{
					impartialList0.addAll(impartialList1);
					return impartialList0;
				},
				list -> Collections.unmodifiableList(list));
	}
	public static <T> Collector<T, List<T>, List<T>> toImmutableArrayList(){
		return Collector.of(
				()->new ArrayList<>(),
				(list, i)-> list.add(i), 
				(impartialList0, impartialList1)->{
					impartialList0.addAll(impartialList1);
					return impartialList0;
				},
				list -> Collections.unmodifiableList(list));
	}
	
	private List<String> createAMutableList(){
		return Arrays.asList("one", "two", "three");
	}
	@Test
	public void test_for_mutable_list(){
		List<String> mutable = createAMutableList();
		mutable.replaceAll(str-> "anotherString");
		
		Assert.assertEquals(3, mutable.size());
		Assert.assertEquals("anotherString", mutable.get(0));
		Assert.assertEquals("anotherString", mutable.get(1));
		Assert.assertEquals("anotherString", mutable.get(2));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void test_for_immutable_list(){
		List<String> immutable = createAMutableList().stream().collect(toImmutableList(()->new ArrayList<String>()));
		immutable.replaceAll(str-> "anotherString");
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void test_for_immutable_list_2(){
		List<String> immutable = createAMutableList().stream().collect(toImmutableArrayList());
		immutable.replaceAll(str-> "anotherString");
	}

}
