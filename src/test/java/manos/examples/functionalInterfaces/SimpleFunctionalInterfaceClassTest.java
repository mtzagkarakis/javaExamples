package manos.examples.functionalInterfaces;

import org.junit.Assert;
import org.junit.Test;

import manos.examples.functionalInterfaces.model.SimpleFunctionalInterfaceClass;


public class SimpleFunctionalInterfaceClassTest {

	@Test
	public void printTest(){
		SimpleFunctionalInterfaceClass ic = new SimpleFunctionalInterfaceClass();
		String inputS = "MPAH";
		String returnS = ic.print(inputS, (messageToPrint) -> {
			System.out.println(messageToPrint);
			return messageToPrint;
		});
		Assert.assertEquals(inputS, returnS);
	}
	@Test
	public void printTestWithReference(){
		SimpleFunctionalInterfaceClass ic = new SimpleFunctionalInterfaceClass();
		String inputS = "MPAH";
		int returnL = ic.stringLength(inputS, String::length);
		Assert.assertEquals(4, returnL);
		//or esle
		returnL = ic.stringLength(inputS, (str)->{return str.length();});
		Assert.assertEquals(4, returnL);
		//or else
		returnL = ic.stringLength(inputS, str->str.length());
		Assert.assertEquals(4, returnL);
	}
	
	@Test
	public void stringCropper(){
		SimpleFunctionalInterfaceClass ic = new SimpleFunctionalInterfaceClass();
		String inputS = "MPAH";
		String returnS = ic.stringCrop(inputS, 2, 3, (str, start, end)->{return str.substring(start, end);});
		Assert.assertEquals("A", returnS);
		
		returnS = ic.stringCrop(inputS, 2, 3, String::substring);
		Assert.assertEquals("A", returnS);
	}
}
