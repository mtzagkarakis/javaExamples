package manos.examples.patterns.observer.withpropertychangelistener;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObserverWithPropertyChangeListenerTest {
    private class ObservableObj {
        String value;
        private PropertyChangeSupport support;

        public ObservableObj() {
            this.value = "";
            this.support = new PropertyChangeSupport(this);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener){
            support.addPropertyChangeListener(listener);
        }
        public void removePropertyChangeListener(PropertyChangeListener listener){
            support.removePropertyChangeListener(listener);
        }

        public void setValueWithEvent(String newValue){
            support.firePropertyChange("value", this.value, newValue);
            this.value = newValue;
        }
    }

    private class ObserverObj implements PropertyChangeListener{
        private String observedProperty = "";
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            this.observedProperty = (String)evt.getNewValue();
        }

        public String getObservedProperty() {
            return observedProperty;
        }
    }
    ObservableObj observableObj;
    ObserverObj observerObj1;
    ObserverObj observerObj2;

    @Before
    public void before(){
        observableObj = new ObservableObj();
        observerObj1 = new ObserverObj();
        observerObj2 = new ObserverObj();

        //add observers
        observableObj.addPropertyChangeListener(observerObj1);
        observableObj.addPropertyChangeListener(observerObj2);
    }

    @After
    public void after(){
        observableObj.removePropertyChangeListener(observerObj1);
        observableObj.removePropertyChangeListener(observerObj2);
    }
    @Test
    public void test_observer_pattern(){

        observableObj.setValueWithEvent("manos tzagkarakis");

        //check that observers have the value
        Assert.assertEquals("manos tzagkarakis", observerObj1.getObservedProperty());
        Assert.assertEquals("manos tzagkarakis", observerObj2.getObservedProperty());
    }
}
