package manos.examples.lambda.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class LambdaSortTest {
	@Test
	public void preLambdaSorting(){
		List<Human> humans = new ArrayList<>();
		humans.add(new Human("Manos", 28));
		humans.add(new Human("Kostas", 32));
		humans.add(new Human("Kostas", 30));
		
		Collections.sort(humans, new Comparator<Human>(){
			@Override
			public int compare(Human h1, Human h2){
				return h1.getName().compareTo(h2.getName());
			}
		});
		Assert.assertEquals("Kostas", humans.get(0).getName());
	}
	
	@Test
	public void sortWithLambda(){
		List<Human> humans = new ArrayList<>();
		humans.add(new Human("Manos", 28));
		humans.add(new Human("Kostas", 32));
		humans.add(new Human("Kostas", 30));
		
		humans.sort((Human h1, Human h2)-> h1.getName().compareTo(h2.getName()));
		Assert.assertEquals("Kostas", humans.get(0).getName());
		//added a static compare to name then age
		humans.sort(Human::compareByNameThenAge);
		Assert.assertEquals("Kostas", humans.get(0).getName());
		
		//or
		Collections.sort(humans, Comparator.comparing(Human::getName));
		Assert.assertEquals("Kostas", humans.get(0).getName());
		
		//or
		Comparator<Human> comparator = (h1, h2) -> h1.getName().compareTo(h2.getName());
		humans.sort(comparator);
		Assert.assertEquals("Kostas", humans.get(0).getName());
		
		//Reversed
		humans.sort(comparator.reversed());
		Assert.assertEquals("Manos", humans.get(0).getName());
		
		//with then
		humans.sort(comparator.thenComparing(Human::getAge));
		Assert.assertEquals(30, humans.get(0).getAge());
	}
}
