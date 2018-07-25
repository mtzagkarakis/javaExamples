package manos.examples.jpaExamples.Interceptor.model;

import javax.persistence.*;

@Entity(name = "SampleEntityClass")
@Table(name = "sample_entity_class")
public class SampleEntityClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "attribute")
    private String attribute;


    public SampleEntityClass(){}

    public SampleEntityClass(String attribute) {
        this.attribute = attribute;
    }

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String toString() {
        return "SampleEntityClass{" +
                "id=" + id +
                ", attribute='" + attribute + '\'' +
                '}';
    }
}
