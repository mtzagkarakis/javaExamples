package manos.examples.jmx;

public interface InfoContainerMBean {
    //naming conventions matterin JMX
    //custom operation
    public void printAppInfo();
    //write only attribute
    public void setInfo(String info);
    //read only attribute
    public String getInfo();

    //because we defined both getInfo and setInfo `info` becomes a read-write attribute
}
