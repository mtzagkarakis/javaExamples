package manos.examples.junit.suitsAndCategoriesRunner;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.IncludeCategory(ICategoryOne.class)
@Suite.SuiteClasses({testClassA.class, testClassB.class})
public class categoryTestRunnerA {
	/**
	 * This should run test from SuiteClasses witch are annotated with with @Category annotation and are included in the IncludedCategory interface
	 * it should print testOneOfA and testOneOfB
	 */
}
