package manos.examples.patterns.proxy;

public class ExpensiveProcedureImpl implements ExpensiveProcedure {
    public static final int DURATION = 3_000;
    public ExpensiveProcedureImpl(){
        configure();
    }
    @Override
    public void execute() {
        System.out.println("Execute");
    }


    private void configure() {
        try {
            Thread.sleep(DURATION);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
