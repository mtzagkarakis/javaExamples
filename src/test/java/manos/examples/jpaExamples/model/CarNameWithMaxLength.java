package manos.examples.jpaExamples.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cars_ml")
public class CarNameWithMaxLength {
	public static final int nameMaxLength=20;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length=nameMaxLength)
	private String name;

	public long getId() {
		return id;
	}
	
	public CarNameWithMaxLength(){}

	public CarNameWithMaxLength(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
}
