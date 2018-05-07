package result;

import java.util.ArrayList;
import java.util.HashMap;

public class AssertionRouletteResult {
	
	private String testCasesFile;
	private HashMap<String, ArrayList<String>> noMessageAssertMap;
	
	/**
	 * @param testCase
	 * @param noMessageAssertMap
	 */
	public AssertionRouletteResult(String testCasesFile, HashMap<String, ArrayList<String>> noMessageAssertMap) {
		super();
		this.testCasesFile = testCasesFile;
		this.noMessageAssertMap = noMessageAssertMap;
	}

	/**
	 * @return the testCase
	 */
	public String getTestCasesFile() {
		return testCasesFile;
	}

	/**
	 * @param testCase the testCase to set
	 */
	public void setTestCasesFile(String testCase) {
		this.testCasesFile = testCase;
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
