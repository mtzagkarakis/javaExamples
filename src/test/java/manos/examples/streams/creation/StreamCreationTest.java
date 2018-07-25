package manos.examples.streams.creation;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Assert;
import org.junit.Test;



public class StreamCreationTest {
	
	@Test
	public void stream_builder(){
		Stream.Builder<Integer> stream_builder = Stream.<Integer>builder().add(1).add(2);
		stream_builder.accept(3);
		Assert.assertTrue(stream_builder.build().mapToInt(i->i).sum()==6);
	}
	
	@Test
	public void stream_generate(){
		Stream<Integer> stream_generated = Stream.generate(()->2).limit(10);
		Assert.assertTrue(stream_generated.count()==10);
		
		stream_generated = Stream.generate(()->2).limit(10);
		Assert.assertTrue(stream_generated.mapToInt(i->i).sum()==20);
	}
	
	@Test
	public void stream_iterate(){
		Stream<Integer> stream_iterated = Stream.iterate(1, i->i+1).limit(3);
		Assert.assertTrue(stream_iterated.mapToInt(i->i).sum()==6);
	}
	
	@Test
	public void stream_of_primitives(){
		//these plus all stream methods and builder
		Assert.assertTrue(IntStream.range(1, 4).sum()==6);
		Assert.assertTrue(IntStream.rangeClosed(1, 3).sum()==6);
		
		DoubleStream double_stream = (new Random()).doubles(10/*streamSize*/, 0/*from(inclusive)*/, 3/*to (exclusive)*/);
		Assert.assertTrue(double_stream.average().getAsDouble() < 3);
	}

	@Test
	public void create_an_empty_stream(){
		Stream<String> emptyStringStream = Stream.empty();
		Assert.assertTrue(emptyStringStream.count()==0);
	}
	
	@Test
	public void stream_of_array(){
		Stream<Integer> stream_of_array = Stream.of(1,2,3);
		Assert.assertTrue(stream_of_array.mapToInt(i->i).sum()==6);
		//or
		Integer[] array_of_integers = new Integer[]{1,2,3};
		stream_of_array = Stream.of(array_of_integers);
		Assert.assertTrue(stream_of_array.mapToInt(i->i).sum()==6);
		//or
		stream_of_array = Arrays.stream(array_of_integers);
		Assert.assertTrue(stream_of_array.mapToInt(i->i).sum()==6);
	}
	
	@Test
	public void stream_from_iterable(){
		Iterable<Integer> iterable = Arrays.asList(1,2,3);
		Stream<Integer> stream_from_iterabale = StreamSupport.stream(iterable.spliterator(), false/*parallel*/);
		Assert.assertTrue(stream_from_iterabale.mapToInt(i->i).sum()==6);
	}

}
