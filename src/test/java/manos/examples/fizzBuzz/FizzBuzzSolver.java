package manos.examples.fizzBuzz;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FizzBuzzSolver {
	
	private boolean isFizz(int number){
		return number % 3 == 0;
	}
	private boolean isBuzz(int number){
		return number % 5 == 0;
	}
	public String numberToFizzBuzzString(Integer number){
		boolean isFizz = this.isFizz(number);
		boolean isBuzz = this.isBuzz(number);
		if (isFizz && isBuzz)
			return "fizzBuzz";
		else if (isFizz)
			return "fizz";
		else if (isBuzz)
			return "buzz";
		else
			return number.toString();
	}
	public List<String> solve(int max){
		return
				Stream.iterate(1, counter -> counter+1)
				.limit(max)
				.map(this::numberToFizzBuzzString)
				.collect(Collectors.toList());
	}
}
