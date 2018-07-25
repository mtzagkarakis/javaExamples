package manos.examples.patterns.proxy;

public class ExpensiveProcedureProxy implements ExpensiveProcedure {
    private static ExpensiveProcedure expensiveProcedure;

    @Override
    public void execute() {
        if (expensiveProcedure == null){
            expensiveProcedure = new ExpensiveProcedureImpl();
        }
        expensiveProcedure.execute();
    }

}
