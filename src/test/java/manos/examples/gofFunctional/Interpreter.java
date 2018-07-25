package manos.examples.gofFunctional;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.IntBinaryOperator;

public class Interpreter {

    static final Map<String, IntBinaryOperator> operatorMap =  new HashMap<>();
    static {
        operatorMap.put("+", (a,b)->a+b);
        operatorMap.put("-", (a,b)->a-b);
        operatorMap.put("*", (a,b)->a*b);
    }

    public static int evaluate(String expressionStr){
        Stack<Integer> expressionStack = new Stack<>();
        Arrays.asList(expressionStr.split(" ")).forEach(expression->{
            if (operatorMap.containsKey(expression)){
                int right = expressionStack.pop();
                int left = expressionStack.pop();
                expressionStack.push(operatorMap.get(expression).applyAsInt(left, right));
            } else {
                expressionStack.push(Integer.parseInt(expression));
            }
        });
        return expressionStack.pop();
    }

    @Test
    public void checkEvaluation(){
        String testExpression = "10 7 - 2 3 + *";
        /**
         * (10-7)*(2+3)=3*5=15
         */
        System.out.println(evaluate(testExpression));
        Assert.assertTrue(evaluate(testExpression)==15);
    }
}
