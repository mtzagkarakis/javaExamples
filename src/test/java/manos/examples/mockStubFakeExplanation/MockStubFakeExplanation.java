package manos.examples.mockStubFakeExplanation;

public class MockStubFakeExplanation {
 /**
  * Fakes are objects that have working implementations, but not same as production one. 
  * Usually they take some shortcut and have simplified version of production code.
  * An example of this shortcut, can be an in-memory implementation of Data Access Object or Repository. 
  * This fake implementation will not engage database, but will use a simple collection to store data. 
  * This allows us to do integration test of services without starting up a database and performing time consuming requests.
  */
	
	
	/**
	 * Stub is an object that holds predefined data and uses it to answer calls during tests. 
	 * It is used when we cannot or don’t want to involve objects that would answer with real data or have undesirable side effects.
	 * An example can be an object that needs to grab some data from the database to respond to a method call. 
	 * Instead of the real object, we introduced a stub and defined what data should be returned.
	 */
	
	/**
	 * Methods that return some result and do not change the state of the system, are called Query. 
	 * Method avarangeGrades, that returns average of student grades is a good example.
	 * There is also another category of methods called Command. 
	 * This is when a method performs some actions, that changes the system state, but we don’t expect any return value from it.
	 */
	
	/**
	 * Mocks are objects that register calls they receive.
	 * In test assertion we can verify on Mocks that all expected actions were performed.
	 */
	
	/**
	 * For testing Query type methods we should prefer use of Stubs as we can verify method’s return value. 
	 * But what about Command type of methods, like method sending an e-mail? How to test them when they do not return any values? The answer is Mock
	 */
}
