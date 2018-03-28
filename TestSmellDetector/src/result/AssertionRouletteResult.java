package result;

import java.util.ArrayList;
import java.util.HashMap;

public class AssertionRouletteResult {

	private String testCase;
	private HashMap<String, ArrayList<String>> noMessageAssertMap;
	
	/**
	 * @param testCase
	 * @param noMessageAssertMap
	 */
	public AssertionRouletteResult(String testCase, HashMap<String, ArrayList<String>> noMessageAssertMap) {
		super();
		this.testCase = testCase;
		this.noMessageAssertMap = noMessageAssertMap;
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
	 * @return the noMessageAssertMap
	 */
	public HashMap<String, ArrayList<String>> getNoMessageAssertMap() {
		return noMessageAssertMap;
	}

	/**
	 * @param noMessageAssertMap the noMessageAssertMap to set
	 */
	public void setNoMessageAssertMap(HashMap<String, ArrayList<String>> noMessageAssertMap) {
		this.noMessageAssertMap = noMessageAssertMap;
	}
	
}
