package manos.examples.patterns.decorator;

public class DecoratorAddedFunctionalityOne extends DecoratorBaseAbstract implements DecoratorInterface {
    public static String DECORATOR_ADDED_STRING = "Added Functionality One";
    public DecoratorAddedFunctionalityOne(DecoratorInterface decoratorInterface) {
        super(decoratorInterface);
    }

    @Override
    public String decorate() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.decorate());
        sb.append(decoratorFunctionalityOne());
        return sb.toString();
    }

    private String decoratorFunctionalityOne(){
        return DECORATOR_ADDED_STRING;
    }
}
