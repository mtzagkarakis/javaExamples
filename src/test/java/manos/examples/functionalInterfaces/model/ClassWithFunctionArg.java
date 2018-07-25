package manos.examples.functionalInterfaces.model;

import java.util.function.Function;

public class ClassWithFunctionArg {
	public String functionWithFunctionArg(Integer _integer, Function<Integer,String> function){
		return function.apply(_integer);
	}
}
