package manos.examples.patterns.Bridge;

public abstract class AbstractBaseClass {
    AbstractClassProperty abstractClassProperty;

    public AbstractBaseClass(AbstractClassProperty abstractClassProperty) {
        this.abstractClassProperty = abstractClassProperty;
    }

    public abstract String calculate();
}
