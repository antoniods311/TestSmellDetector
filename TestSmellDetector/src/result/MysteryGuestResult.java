package result;

import java.util.ArrayList;
import java.util.HashMap;

public class MysteryGuestResult {

	private String testClassName;
	private HashMap<String, ArrayList<String>> typeResult;
	private HashMap<String, ArrayList<String>> callResult;
	private int testMethodNumber;

	/**
	 * Object constructor
	 * 
	 * @param testCase
	 * @param typeResult
	 * @param callResult
	 */
	public MysteryGuestResult(String testCase, HashMap<String, ArrayList<String>> typeResult,
			HashMap<String, ArrayList<String>> callResult, int testMethodNumber) {
		super();
		this.testClassName = testCase;
		this.typeResult = typeResult;
		this.callResult = callResult;
		this.testMethodNumber = testMethodNumber;
	}
	
	/**
	 * @return the testCase
	 */
	public String getTestClassName() {
		return testClassName;
	}

	/**
	 * @param testCase the testCase to set
	 */
	public void setTestClassName(String testCase) {
		this.testClassName = testCase;
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
	
	/**
	 * @return the testMethodNumber
	 */
	public int getTestMethodNumber() {
		return testMethodNumber;
	}

	/**
	 * @param testMethodNumber the testMethodNumber to set
	 */
	public void setTestMethodNumber(int testMethodNumber) {
		this.testMethodNumber = testMethodNumber;
	}
	
	
}
