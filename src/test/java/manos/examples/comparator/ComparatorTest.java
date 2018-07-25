package manos.examples.comparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;


public class ComparatorTest {
	private class Entity{
		public int number;
		public String text;
		public Entity(int number, String text){
			this.number = number;
			this.text = text;
		}
		@Override
		public String toString() {
			return "Entity [number=" + number + ", text=" + text + "]";
		}
		
	}
	
	private class EntityComp implements Comparable<EntityComp>{
		public int number;
		public String text;
		public EntityComp(int number, String text){
			this.number = number;
			this.text = text;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EntityComp other = (EntityComp) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (number != other.number)
				return false;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			return true;
		}
		private ComparatorTest getOuterType() {
			return ComparatorTest.this;
		}

		@Override
		public int compareTo(EntityComp e) {
			if (this.number > e.number)
				return 1;
			else if (this.number == e.number)
				return this.text.compareTo(e.text);
			else
				return -1;
		}

		
	}
	
	private List<Entity> createEntityList(){
		return Arrays.asList(new Entity(11, "11"), new Entity(15, "15"), new Entity(10, "10"), new Entity(10, "...10"), new Entity(12, "12"));
	}
	
	private List<EntityComp> createEntityCompList(){
		return Arrays.asList(new EntityComp(11, "11"), new EntityComp(15, "15"), new EntityComp(10, "10"), new EntityComp(10, "...10"), new EntityComp(12, "12"));
	}
	
	/**
	 * The Comparator.comparing static function accepts a sort key Function and returns a Comparator for the type which contains the sort key:
	 */
	@Test
	public void sort_using_comparator(){
		Comparator<Entity> entityComparatorForNumber = Comparator.comparing(entity->entity.number);
		List<Entity> entityList = createEntityList();
		Collections.sort(entityList, entityComparatorForNumber);
		Assert.assertEquals(10, entityList.get(0).number);
		Assert.assertEquals(15, entityList.get(4).number);
	}
	
	/**
	 * There is another option that facilitates overriding the natural ordering of the sort key by providing the Comparator that creates a custom ordering for the sort key:
	 */
	@Test
	public void sort_using_comparator_using_function_to_compare(){
		Comparator<Entity> entityComparatorForNumber = Comparator.comparing(entity->entity.number, (s1, s2)-> {return s1.compareTo(s2);}); //s2.compareTo(s1) makes a desc order
		List<Entity> entityList = createEntityList();
		Collections.sort(entityList, entityComparatorForNumber);
		Assert.assertEquals(10, entityList.get(0).number);
		Assert.assertEquals(15, entityList.get(4).number);
	}
	
	/**
	 * When invoked on an existing Comparator, the instance method Comparator.reversed returns a new Comparator that reverses the sort order of the original.
	 */
	@Test
	public void sort_using_comparator_reversed(){
		Comparator<Entity> entityComparatorForNumber = Comparator.comparing(entity->entity.number);
		entityComparatorForNumber = entityComparatorForNumber.reversed();
		List<Entity> entityList = createEntityList();
		Collections.sort(entityList, entityComparatorForNumber);
		Assert.assertEquals(15, entityList.get(0).number);
		Assert.assertEquals(10, entityList.get(4).number);
	}
	
	
	/**
	 * There is also a function Comparator.comparingInt which does the same thing as Comparator.comparing, but it takes only int selectors. 
	 * There is also one for double and long
	 */
	@Test
	public void sort_using_comparator_comparing_int(){
		Comparator<Entity> entityComparatorForNumber = Comparator.comparingInt(entity->entity.number);
		List<Entity> entityList = createEntityList();
		Collections.sort(entityList, entityComparatorForNumber);
		Assert.assertEquals(10, entityList.get(0).number);
		Assert.assertEquals(15, entityList.get(4).number);
	}
	
	/**
	 * You can define the natural order for elements by implementing a Comparable interface which has compareTo() method 
	 * for comparing current object and object passed as an argument.
	 */
	@Test
	public void sort_using_comparator_natural_order_item_implement_Comparable_interface(){
		List<EntityComp> entityList = createEntityCompList();
		Collections.sort(entityList);
		Assert.assertEquals(10, entityList.get(0).number);
		Assert.assertEquals("...10", entityList.get(0).text);
		Assert.assertEquals("10", entityList.get(1).text);
		Assert.assertEquals(15, entityList.get(4).number);
	}
	
	
	/**
	 * Now, let’s sort the elements using a Comparator interface implementation – where we pass the anonymous inner class on-the-fly to the Collections.sort() API:
	 */
	@Test
	public void sort_using_comparator_using_Comparator_interface_implementation(){
		List<Entity> entityList = createEntityList();
		Collections.sort(entityList, new Comparator<Entity>() {
			@Override
			public int compare(Entity o1, Entity o2) {
				return o1.text.compareTo(o2.text);
			}
			
		});
		Assert.assertEquals("...10", entityList.get(0).text);
		Assert.assertEquals("15", entityList.get(4).text);
	}
	
	/**
	 * We can use Lambdas to implement the Comparator Functional Interface.
	 */
	@Test
	public void sort_using_comparator_with_lambda(){
		List<Entity> entityList = createEntityList();
		Collections.sort(entityList, (e1, e2)-> e1.text.compareTo(e2.text));
		Assert.assertEquals("...10", entityList.get(0).text);
		Assert.assertEquals("15", entityList.get(4).text);
	}
	
	/**
	 * Java 8 comes with two new APIs useful for sorting – comparing() and thenComparing() in the Comparator interface. 
	 * These are quite handy for chaining of multiple conditions of the Comparator.
	 * In this example we compare first by number and then by text but text is reversed
	 * [Entity [number=10, text=10], Entity [number=10, text=...10], Entity [number=11, text=11], Entity [number=12, text=12], Entity [number=15, text=15]]
	 */
	@Test
	public void sort_using_comparator_comparing_and_thenComparing(){
		List<Entity> entityList = createEntityList();
		Function<Entity, Integer> compareByNumber = e->e.number;
		Function<Entity, String> compareByText = e->e.text; 
		Collections.sort(entityList, Comparator.comparing(compareByNumber).reversed().thenComparing(compareByText).reversed());

		Assert.assertEquals("10", entityList.get(0).text);
		Assert.assertEquals("...10", entityList.get(1).text);
		Assert.assertEquals("15", entityList.get(4).text);
	}
}
