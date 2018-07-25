package manos.examples.gofFunctional;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.DoubleUnaryOperator;
import java.util.stream.Stream;

public class Decorator {
    private double generalTax(double amount){
        return amount*0.8d;
    }
    private double regionalTax(double amount){
        return amount * 0.95d;
    }
    private double healthInsurance(double amount){
        return amount-200.0d;
    }

    @Test
    public void salaryCalculator(){
        DoubleUnaryOperator salaryOps = DoubleUnaryOperator.identity()
                .andThen(this::generalTax)
                .andThen(this::regionalTax)
                .andThen(this::healthInsurance);

        Assert.assertTrue(salaryOps.applyAsDouble(2400.0d)==1624.0d);
    }

    @Test
    public void salaryCalculatorAlt(){
        Assert.assertTrue(calculateSalary(2400.0d, this::generalTax, this::regionalTax, this::healthInsurance)==1624.0d);
    }
    private double calculateSalary(double initialSalary, DoubleUnaryOperator ... taxes){
        return Stream.of(taxes).reduce(DoubleUnaryOperator.identity(), (calculatedSalary, tax) -> calculatedSalary.andThen(tax)).applyAsDouble(initialSalary);
    }
}
