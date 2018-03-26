package result;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class LazyTestResult {
	
	private File testCase;
	private HashMap<String,HashSet<String>> testedMethods;
	
	/**
	 * @param testCase
	 * @param testedMethods
	 */
	public LazyTestResult(File testCase, HashMap<String, HashSet<String>> testedMethods) {
		super();
		this.testCase = testCase;
		this.testedMethods = testedMethods;
	}

	/**
	 * @return the testCase
	 */
	public File getTestCase() {
		return testCase;
	}

	/**
	 * @param testCase the testCase to set
	 */
	public void setTestCase(File testCase) {
		this.testCase = testCase;
	}

	/**
	 * @return the testedMethods
	 */
	public HashMap<String, HashSet<String>> getTestedMethods() {
		return testedMethods;
	}

	/**
	 * @param testedMethods the testedMethods to set
	 */
	public void setTestedMethods(HashMap<String, HashSet<String>> testedMethods) {
		this.testedMethods = testedMethods;
	}
	
	

}
