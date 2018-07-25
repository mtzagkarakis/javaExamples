package manos.examples.functionalInterfaces.model;

public class SimpleFunctionalInterfaceClass {
	@FunctionalInterface
	public interface Printer{
		String print(String textToPrint);
	}
	public String print(String textToPrint, Printer printer){
		return printer.print(textToPrint);
	}
	
	@FunctionalInterface
	public interface StringLength{
		int calculateStringLength(String stringToCrop);
	}
	public int stringLength(String stringToMeasure, StringLength stringLength){
		return stringLength.calculateStringLength(stringToMeasure);
	}
	
	@FunctionalInterface
	public interface StringCropper{
		String crop(String stringToCrop, int start, int end);
	}
	public String stringCrop(String stringToCrop, int start, int end, StringCropper stringCropper){
		return stringCropper.crop(stringToCrop, start, end);
	}
}
