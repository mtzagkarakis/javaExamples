package manos.examples.jmx;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.concurrent.atomic.AtomicLong;

public class InfoContainer extends NotificationBroadcasterSupport implements InfoContainerMBean/* or implement: javax.management.NotificationEmitter */{
    private final AtomicLong sequenceNumber = new AtomicLong(0L);
    private String info;

    public InfoContainer(String info) {
        super(createBeanNotificationInfo());
        this.info = info;
    }

    private static MBeanNotificationInfo[] createBeanNotificationInfo(){
        String[] types = new String[]{
                AttributeChangeNotification.ATTRIBUTE_CHANGE
        };
        String name = AttributeChangeNotification.class.getName();
        String description = "InfoContainer of User Bean has Changed";

        return new MBeanNotificationInfo[]{new MBeanNotificationInfo(types,name,description)};
    }

    @Override
    public void printAppInfo() {
        System.out.println("THIS IS APP INFO: " + info);
    }

    @Override
    public void setInfo(String info) {
        notifyAboutInfoChange(info);
        this.info = info;
    }
    private synchronized void notifyAboutInfoChange(String newInfo){
        Notification notification = new AttributeChangeNotification(this,
                    sequenceNumber.getAndAdd(1),
                    System.currentTimeMillis(),
                    "InfoContainer.info has changed",
                    "Info",
                    "String",
                    this.info,
                    newInfo
                );
        sendNotification(notification); // from NotificationBroadcasterSupport.class
        System.out.println("Send notification executed");
    }
    @Override
    public String getInfo() {
        return info;
    }
}
