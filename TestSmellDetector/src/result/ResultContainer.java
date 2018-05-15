package result;
import java.util.HashMap;
import java.util.HashSet;

public class ResultContainer {
	
	private String testCasesFile;
	private HashMap<String,HashSet<String>> testedMethods;
	
	/**
	 * @param testCase
	 * @param testedMethods
	 */
	public ResultContainer(String testCase, HashMap<String, HashSet<String>> testedMethods) {
		super();
		this.testCasesFile = testCase;
		this.testedMethods = testedMethods;
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
