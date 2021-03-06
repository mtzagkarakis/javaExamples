package manos.examples.jpaExamples.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "owners")
public class OwnerLazy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "owner_cars", joinColumns = @JoinColumn(name = "owner_id", referencedColumnName="id"),
	inverseJoinColumns = @JoinColumn(name="car_id", referencedColumnName="id"))
	List<Car> cars;

	public OwnerLazy(){}
	
	public OwnerLazy(String name, List<Car> cars) {
		super();
		this.name = name;
		this.cars = cars;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
}
