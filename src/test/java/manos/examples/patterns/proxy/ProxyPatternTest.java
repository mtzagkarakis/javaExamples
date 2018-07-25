package manos.examples.patterns.proxy;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

public class ProxyPatternTest {
    @Test
    /**
     * With this pattern, we create an intermediary that acts as an interface to another resource, e.g., a file, a connection.
     * This secondary access provides a surrogate for the real component and protects it from the underlying complexity.
     *
     * WHEN:
     *      When we want a simplified version of a complex or heavy object.
     *          In this case, we may represent it with a skeleton object which loads the original object on demand,
     *          also called as lazy initialization. This is known as the Virtual Proxy
     *
     *      When the original object is present in different address space, and we want to represent it locally.
     *          We can create a proxy which does all the necessary boilerplate stuff like creating and maintaining the connection,
     *          encoding, decoding, etc., while the client accesses it as it was present in their local address space.
     *          This is called the Remote Proxy
     *
     *      When we want to add a layer of security to the original underlying object to provide controlled access based on access rights of the client.
     *          This is called Protection Proxy
     *
     * The proxy provides the same interface as the object it’s holding the reference to, and it doesn’t modify the data in any manner;
     * it’s in contrast to Adapter and Decorator patterns which alter and decorate the functionalities of pre-existing instances respectively
     * The Proxy usually has the information about the real subject at the compile time itself whereas Decorator and Adapter get injected at runtime,
     * knowing only the actual object’s interface
     */
    public void testProxyPattern(){
        ExpensiveProcedure expensiveProcedure = new ExpensiveProcedureProxy();
        Instant now = Instant.now();
        expensiveProcedure.execute();
        expensiveProcedure.execute();
        Assert.assertEquals(ExpensiveProcedureImpl.DURATION/1_000, Duration.between( now, Instant.now()).getSeconds());
    }
}
