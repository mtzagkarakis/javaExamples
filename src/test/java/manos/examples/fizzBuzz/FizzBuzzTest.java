package manos.examples.fizzBuzz;

import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

public class FizzBuzzTest {
	FizzBuzzSolver fizzBuzzSolver = new FizzBuzzSolver();
	private static final int MAX_TEST_NUMBER = Integer.MAX_VALUE;
	@Test
	public void test_for_fizz(){
		boolean result =
		IntStream.range(1, MAX_TEST_NUMBER)
			.filter(i -> i % 3 == 0)
			.filter(i -> i % 5 != 0)
			.boxed()
			.parallel()
			.map(this.fizzBuzzSolver::numberToFizzBuzzString)
			.allMatch(str-> str.equals("fizz"));
		Assert.assertTrue(result);
	}
	@Test
	public void test_for_buzz(){
		boolean result =
		IntStream.range(1, MAX_TEST_NUMBER)
			.filter(i -> i % 3 != 0)
			.filter(i -> i % 5 == 0)
			.boxed()
			.parallel()
			.map(this.fizzBuzzSolver::numberToFizzBuzzString)
			.allMatch(str-> str.equals("buzz"));
		Assert.assertTrue(result);
	}
	@Test
	public void test_for_fizzBuzz(){
		boolean result =
		IntStream.range(1, MAX_TEST_NUMBER)
			.filter(i -> i % 3 == 0)
			.filter(i -> i % 5 == 0)
			.boxed()
			.parallel()
			.map(this.fizzBuzzSolver::numberToFizzBuzzString)
			.allMatch(str-> str.equals("fizzBuzz"));
		Assert.assertTrue(result);
	}
	@Test
	public void test_for_regular(){
		boolean result =
		IntStream.range(1, MAX_TEST_NUMBER)
			.filter(i -> i % 3 != 0)
			.filter(i -> i % 5 != 0)
			.boxed()
			.parallel()
			.map(this.fizzBuzzSolver::numberToFizzBuzzString)
			.allMatch(str-> !str.equals("fizzBuzz") && !str.equals("fizz") && !str.equals("buzz"));
		Assert.assertTrue(result);
	}
	
	@Test 
	public void test_solution_size(){
		Assert.assertEquals(100, fizzBuzzSolver.solve(100).size());
	}
}
