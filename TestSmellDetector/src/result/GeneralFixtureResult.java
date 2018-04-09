package result;

import java.util.HashMap;

public class GeneralFixtureResult {

	private String testCase;
	private HashMap<String,HashMap<String,Boolean>> results;
	
	/**
	 * @param testCase
	 * @param results
	 */
	public GeneralFixtureResult(String testCase, HashMap<String, HashMap<String, Boolean>> results) {
		super();
		this.testCase = testCase;
		this.results = results;
	}

	/**
	 * @return the testCase
	 */
	public String getTestCase() {
		return testCase;
	}

	/**
	 * @param testCase the testCase to set
	 */
	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

	/**
	 * @return the results
	 */
	public HashMap<String, HashMap<String, Boolean>> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(HashMap<String, HashMap<String, Boolean>> results) {
		this.results = results;
	}
	
	
	
	
}
