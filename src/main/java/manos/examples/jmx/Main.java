package manos.examples.jmx;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    /**
     * Dont forget to add jvm args
     *
     * -Dcom.sun.management.jmxremote
     * -Dcom.sun.management.jmxremote.port=8008
     * -Dcom.sun.management.jmxremote.authenticate=false
     * -Dcom.sun.management.jmxremote.ssl=false
     *
     * @param args
     * @throws InterruptedException
     * @throws MalformedObjectNameException
     */
    public static void main(String... args) throws InterruptedException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        System.out.println("Open JConsole!!! Will send a notification about a change in InfoContainer.class every 10 seconds");
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("test:type=InfoContainer");
        InfoContainer info = new InfoContainer("initial info");

        mBeanServer.registerMBean(info, name);

        AtomicLong counter = new AtomicLong(1);
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                info.setInfo("info change should send a notification " + counter.getAndAdd(1));
                System.out.println("Notification should send");
            }
        };
        new Timer().scheduleAtFixedRate(tt, 0L, 10_000L);


        Thread.currentThread().sleep(Long.MAX_VALUE);
    }

}
