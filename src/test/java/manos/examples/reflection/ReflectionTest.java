package manos.examples.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;



public class ReflectionTest {
	public class ReflectionOps{
		public ReflectionOps(){}
		public String publicMethod(String string, int number){
			return (string + number);
		}
		protected String protectedMethod(String string, int number){
			return (string + number);
		}
		@SuppressWarnings("unused")
		private String privateMethod(String string, int number){
			return (string + number);
		}
	}
	
	ReflectionOps reflectionOpsInstance = new ReflectionOps(); 
	/**
	 * we can get thew public methods with the getMethod method
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@Test
	public void invoke_publicMethod() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method publicMethod = ReflectionOps.class.getMethod("publicMethod", String.class, int.class);
		String result = (String)publicMethod.invoke(reflectionOpsInstance, "Number: ", 0);
		Assert.assertEquals("Number: 0", result);
	}
	
	/**
	 * we can invoke a private or protected method but an illegal access exception will be thrown
	 * if we do not change the setAccesible method.
	 * We also use the class.getDeclaredMethod in order to get private or protected
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@Test(expected=IllegalAccessException.class)
	public void invoke_privateMethod_with_exception() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method privateMethod = ReflectionOps.class.getDeclaredMethod("privateMethod", String.class, int.class);
		String result = (String)privateMethod.invoke(reflectionOpsInstance, "Number: ", 0);
		Assert.assertEquals("Number: 0", result);
	}
	
	/**
	 * here we set the set accessible on the private method so no exception is thrown
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@Test
	public void invoke_privateMethod() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method privateMethod = ReflectionOps.class.getDeclaredMethod("privateMethod", String.class, int.class);
		privateMethod.setAccessible(true);
		String result = (String)privateMethod.invoke(reflectionOpsInstance, "Number: ", 0);
		Assert.assertEquals("Number: 0", result);
	}
}
