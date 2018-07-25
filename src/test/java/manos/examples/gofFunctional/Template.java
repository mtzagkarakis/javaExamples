package manos.examples.gofFunctional;


import org.junit.Test;

import java.util.function.Consumer;

public class Template {
    private class ClosableResource implements AutoCloseable{
        public boolean isClosed = false;
        public void functionOne(){System.out.println("FunctionOne");}
        public void functionTwo(){System.out.println("FunctionTwo");}
        @Override
        public void close() {
            System.out.println("Closed");
            isClosed = true;
        }
    }

    private void withResource (Consumer<ClosableResource> consumer){
        try(ClosableResource closableResource = new ClosableResource()){
            consumer.accept(closableResource);
        }

    }

    @Test
    public void template(){
        withResource(resource -> resource.functionOne());
        withResource(resource -> resource.functionTwo());
    }
}
