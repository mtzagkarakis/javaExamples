package manos.examples.patterns.Bridge;

public class BaseChild extends AbstractBaseClass {
    public static final String PROPERTY = " is Awesome";
    public BaseChild(AbstractClassProperty abstractClassProperty) {
        super(abstractClassProperty);
    }

    @Override
    public String calculate() {
        return abstractClassProperty.getProperty() + PROPERTY;
    }
}
