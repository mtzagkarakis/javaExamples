package manos.examples.patterns.decorator;

public class DecoratorAddedFunctionalityTwo extends DecoratorBaseAbstract implements DecoratorInterface{
    public static String DECORATOR_ADDED_STRING = "Added Functionality Two";
    public DecoratorAddedFunctionalityTwo(DecoratorInterface decoratorInterface) {
        super(decoratorInterface);
    }

    @Override
    public String decorate() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.decorate());
        sb.append(decoratorFunctionalityTwo());
        return sb.toString();
    }

    private String decoratorFunctionalityTwo(){
        return DECORATOR_ADDED_STRING;
    }
}
