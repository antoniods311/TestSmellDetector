package result;

import java.util.ArrayList;
import java.util.HashMap;

public class MysteryGuestResult {

	private String testCase;
	private HashMap<String, ArrayList<String>> typeResult;
	private HashMap<String, ArrayList<String>> callResult;
	
	/**
	 * Object constructor
	 * 
	 * @param testCase
	 * @param typeResult
	 * @param callResult
	 */
	public MysteryGuestResult(String testCase, HashMap<String, ArrayList<String>> typeResult,
			HashMap<String, ArrayList<String>> callResult) {
		super();
		this.testCase = testCase;
		this.typeResult = typeResult;
		this.callResult = callResult;
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
	 * @return the typeResult
	 */
	public HashMap<String, ArrayList<String>> getTypeResult() {
		return typeResult;
	}

	/**
	 * @param typeResult the typeResult to set
	 */
	public void setTypeResult(HashMap<String, ArrayList<String>> typeResult) {
		this.typeResult = typeResult;
	}

	/**
	 * @return the callResult
	 */
	public HashMap<String, ArrayList<String>> getCallResult() {
		return callResult;
	}

	/**
	 * @param callResult the callResult to set
	 */
	public void setCallResult(HashMap<String, ArrayList<String>> callResult) {
		this.callResult = callResult;
	}
	
	
	
	
}
