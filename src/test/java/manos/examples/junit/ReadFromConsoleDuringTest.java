package manos.examples.junit;

import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

public class ReadFromConsoleDuringTest {

	@Test
	public void read_from_console(){
		Scanner sc = new Scanner(System.in);
		System.out.println("type a not empty string");
		Assert.assertTrue(sc.next().length() > 0);
		sc.close();
	}
}
