package manos.examples.junit.testRunListener;

import org.junit.runner.JUnitCore;

public class TestClassRunWithListener {
	public static void main(String[] args) {
		JUnitCore runner = new JUnitCore();
		runner.addListener(new TestListener());
		runner.run(testClassA.class, testClassB.class);
		/**
		 * Should print
		 * Test cases to execute : 6
		 * Execution Started : noAnnotatedTestOfA
		 * noAnnotatedTestOfA
		 * Execution Finished : noAnnotatedTestOfA
		 * Execution Started : testOneOfA
		 * testOneOfA
		 * Execution Finished : testOneOfA
		 * Execution Started : testTwoOfA
		 * testTwoOfA
		 * Execution Finished : testTwoOfA
		 * Execution Started : noAnnotatedTestOfB
		 * noAnnotatedTestOfB
		 * Execution Finished : noAnnotatedTestOfB
		 * Execution Started : testOneOfB
		 * testOneOfB
		 * Execution Finished : testOneOfB
		 * Execution Started : testTwoOfB
		 * testTwoOfB
		 * Execution Finished : testTwoOfB
		 * Test cases executed : 6
		 */
	}
}
