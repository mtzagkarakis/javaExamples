package manos.examples.streams.strings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;


public class StringStreamOperations {
	private String joiningStreamArrayWithCommas(String[] arrayOfStrings){
		return Arrays.asList(arrayOfStrings).stream().collect(Collectors.joining(",", "[", "]")); //delimiter, prefix, postfix
	}
	
	private List<String> splitStringOfCommas(String in_str){
		return Stream.of(in_str.split(",")).map(str->str.trim()).collect(Collectors.toList());
	}
	private List<Character> splitStringToCharacters(String in_str){
		return in_str.chars().mapToObj(ch -> (char)ch).collect(Collectors.toList());
	}
	
	@Test
	public void joinStringArrayWithCommasTest(){
		String[] str_arr = {"one", "two", "three"};
		String joined = joiningStreamArrayWithCommas(str_arr);
		Assert.assertEquals("[one,two,three]", joined);
	}
	
	@Test
	public void splitStringOfCommasTest(){
		String in = "one, two, three";
		List<String> splitted = splitStringOfCommas(in);
		Assert.assertEquals(3, splitted.size());
		Assert.assertEquals("one", splitted.get(0));
		Assert.assertEquals("two", splitted.get(1));
		Assert.assertEquals("three", splitted.get(2));
	}
	
	@Test
	public void splitStringToCharactersTest(){
		String in = "abcd";
		List<Character> splitted = splitStringToCharacters(in);
		Assert.assertEquals(4, splitted.size());
		Assert.assertEquals("a", splitted.get(0).toString());
		Assert.assertEquals("b", splitted.get(1).toString());
		Assert.assertEquals("c", splitted.get(2).toString());
		Assert.assertEquals("d", splitted.get(3).toString());
	}
}
