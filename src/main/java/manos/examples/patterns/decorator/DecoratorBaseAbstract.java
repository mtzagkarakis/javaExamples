package manos.examples.patterns.decorator;

public abstract class DecoratorBaseAbstract implements DecoratorInterface {
    DecoratorInterface decoratorInterface;

    public DecoratorBaseAbstract(DecoratorInterface decoratorInterface) {
        this.decoratorInterface = decoratorInterface;
    }

    @Override
    public String decorate() {
        return decoratorInterface.decorate();
    }
}
