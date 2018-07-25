package manos.examples.streams.collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CollectorsTest {
	List<String> givenList;
	@Before
	public void before(){
		givenList = Arrays.asList("one", "two", "three", "four", "one", "two");
	}
	
	@Test
	public void collectors_to_list(){
		List<String> collectedList = givenList.stream().collect(Collectors.toList());
		Assert.assertEquals(6, collectedList.size());
	}
	
	@Test
	public void collectors_to_set(){
		Set<String> collectedSet = givenList.stream().collect(Collectors.toSet());
		Assert.assertEquals(4, collectedSet.size());
	}
	
	@Test
	public void collecotrs_to_specific_collection_impl(){
		Supplier<ArrayList<String>> newArrayListSupplier = () -> new ArrayList<>();
		List<String> collectedArrayList = givenList.stream().collect(Collectors.toCollection(newArrayListSupplier));//or ArrayList::new, or ()-> new ArrayList<>()
		Assert.assertEquals(6, collectedArrayList.size());
		Assert.assertTrue(collectedArrayList instanceof ArrayList);
		
		Set<String> collectedHashSet = givenList.stream().collect(Collectors.toCollection(HashSet::new));
		Assert.assertEquals(4, collectedHashSet.size());
	}
	
	@Test(expected = IllegalStateException.class)//duplicate key...
	public void collectors_to_map_exception(){
		givenList.stream().collect(Collectors.toMap((value)->value, (value)->value.length()));
	}
	
	@Test
	public void collectors_to_map(){
		Map<String, Integer> map = givenList.stream().limit(4).collect(Collectors.toMap((value)->value, (value)->value.length()));
		Assert.assertEquals(4, map.size());
		Assert.assertEquals(3, map.get("two").intValue());
		Assert.assertEquals(5, map.get("three").intValue());
	}
	
	@Test
	public void collectors_to_map_handle_same_key_collisions(){
		/**
		 * The third argument here is a BinaryOperator, where you can specify how you want collisions to be handled. 
		 * In this case, we will just pick any of these two colliding values because we know that same strings will always have same lengths too.
		 */
		Map<String, Integer> map = givenList.stream().collect(Collectors.toMap((value)->value, String::length, (v0, v1)-> v1));
		Assert.assertEquals(4, map.size());
		Assert.assertEquals(3, map.get("two").intValue());
		Assert.assertEquals(5, map.get("three").intValue());
	}
	
	@Test
	public void collectors_collecting_and_then(){
		/**
		 * CollectingAndThen is a special collector that allows performing another action on a result straight after collecting ends.
		 * and then function:  Function<R,RR> finisher
		 * R - result type of the downstream collector
		 * RR - result type of the resulting collector
		 */
		int size = givenList.stream().map(v->v.length()).collect(Collectors.collectingAndThen(Collectors.toList(), (len_list)-> len_list.size()));
		Assert.assertEquals(6, size);
	}
	
	@Test
	public void collectors_joining(){
		String result = givenList.stream().collect(Collectors.joining());
		Assert.assertEquals("onetwothreefouronetwo", result);
		
		result = givenList.stream().collect(Collectors.joining(", "));
		Assert.assertEquals("one, two, three, four, one, two", result);
		
		result = givenList.stream().collect(Collectors.joining(",", "[", "]"));
		Assert.assertEquals("[one,two,three,four,one,two]", result);
	}
	
	@Test
	public void collectors_counting(){
		Assert.assertEquals(6, givenList.stream().count());
		Assert.assertEquals(6, givenList.stream().collect(Collectors.counting()).intValue());
	}
	
	@Test
	public void collectors_summarizingDouble(){
		DoubleSummaryStatistics stat = givenList.stream().collect(Collectors.summarizingDouble(str->str.length()));
		
		Assert.assertTrue(stat.getAverage() == 3.5d);
		Assert.assertTrue(stat.getCount() == 6);
		Assert.assertTrue(stat.getMax() == 5);
		Assert.assertTrue(stat.getMin() == 3);
		Assert.assertTrue(stat.getSum() == 21);
		
		IntSummaryStatistics stat_int = givenList.stream().collect(Collectors.summarizingInt(str->str.length()));
		Assert.assertTrue(stat_int.getAverage() == 3.5);
		
		//or you can do individual functions
		Assert.assertTrue(givenList.stream().collect(Collectors.summingDouble(String::length)) == 21);
		
		Assert.assertTrue(givenList.stream().collect(Collectors.averagingDouble(String::length)) == 3.5d);
	}
	
	@Test
	public void collectors_maxBy(){
		Optional<String> result = givenList.stream().collect(Collectors.maxBy(Comparator.naturalOrder()));
		Assert.assertEquals("two", result.get());
		
		result = givenList.stream().collect(Collectors.minBy(Comparator.naturalOrder()));
		Assert.assertEquals("four", result.get());
		
		result = givenList.stream().collect(Collectors.maxBy(Comparator.reverseOrder()));
		Assert.assertEquals("four", result.get());
		
		result = givenList.stream().collect(Collectors.maxBy(Comparator.comparing((str)-> str.length())));
		Assert.assertEquals("three", result.get());
		
		result = givenList.stream().collect(Collectors.minBy(Comparator.comparing((str)-> str.length())));
		Assert.assertEquals("one", result.get());
		
		result = givenList.stream().skip(1).collect(Collectors.minBy(Comparator.comparing((str)-> str.length())));
		Assert.assertEquals("two", result.get());
	}
	
	
	@Test
	public void collectors_grouping_by(){
		Map<Integer, List<String>> map = givenList.stream().collect(Collectors.groupingBy(String::length, Collectors.toList()));
		Assert.assertEquals(4, map.get(3).size());
		Assert.assertEquals("four", map.get(4).get(0));
		Assert.assertEquals("three", map.get(5).get(0));
		
	}
	
	@Test
	/**
	 * Partition result in a map with keys= true or false
	 */
	public void collectors_partitionBy(){
		Map<Boolean, List<String>> map = givenList.stream().collect(Collectors.partitioningBy(s-> s.length() > 3));
		Assert.assertEquals(2, map.size());
		Assert.assertTrue(map.get(true).size() == 2);
		Assert.assertTrue(map.get(false).size() == 4);
	}
}
