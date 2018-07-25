package manos.examples.lambda.sort;

public class Human {
	private int age;
	private String name;
	public Human(){}
	public Human(String name, int age){
		this.name = name;
		this.age = age;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static int compareByNameThenAge(Human h1, Human h2){
		if (h1.age == h2.age){
			return h1.age - h2.age;
		} else {
			return h1.name.compareTo(h2.name);
		}
	}
}
