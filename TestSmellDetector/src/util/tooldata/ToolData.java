package util.tooldata;

import java.io.File;
import java.util.ArrayList;

import com.ibm.wala.ipa.callgraph.CallGraph;

/**
 * @author antoniods311
 *
 */
public class ToolData {
	
	private ArrayList<File> productioClasses = null;
	private ArrayList<File> testClasses = null;
	private CallGraph callGraph = null;
	
	/**
	 * empty constructor
	 */
	public ToolData(){	}
	
	/**
	 * @param productioClasses
	 * @param testClasses
	 * @param callGraph
	 */
	public ToolData(ArrayList<File> productioClasses, ArrayList<File> testClasses, CallGraph callGraph) {
		super();
		this.productioClasses = productioClasses;
		this.testClasses = testClasses;
		this.callGraph = callGraph;
	}

	/**
	 * @return the productioClasses
	 */
	public ArrayList<File> getProductioClasses() {
		return productioClasses;
	}

	/**
	 * @param productioClasses the productioClasses to set
	 */
	public void setProductioClasses(ArrayList<File> productioClasses) {
		this.productioClasses = productioClasses;
	}

	/**
	 * @return the testClasses
	 */
	public ArrayList<File> getTestClasses() {
		return testClasses;
	}

	/**
	 * @param testClasses the testClasses to set
	 */
	public void setTestClasses(ArrayList<File> testClasses) {
		this.testClasses = testClasses;
	}

	/**
	 * @return the callGraph
	 */
	public CallGraph getCallGraph() {
		return callGraph;
	}

	/**
	 * @param callGraph the callGraph to set
	 */
	public void setCallGraph(CallGraph callGraph) {
		this.callGraph = callGraph;
	}
	
	
	
	
	

}
