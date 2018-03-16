package util.tooldata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import com.ibm.wala.ipa.callgraph.CallGraph;

import util.prodclass.ToolMethodType;

/**
 * @author antoniods311
 *
 */
public class ToolData {
	
	private ArrayList<File> testClasses = null;
	private ArrayList<File> productionClasses = null;
	private HashSet<ToolMethodType> productionMethods;
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
		this.productionClasses = productioClasses;
		this.testClasses = testClasses;
		this.callGraph = callGraph;
	}

	/**
	 * @return the productioClasses
	 */
	public ArrayList<File> getProductionClasses() {
		return productionClasses;
	}

	/**
	 * @param productioClasses the productioClasses to set
	 */
	public void setProductionClasses(ArrayList<File> productioClasses) {
		this.productionClasses = productioClasses;
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

	/**
	 * @return the productionMethods
	 */
	public HashSet<ToolMethodType> getProductionMethods() {
		return productionMethods;
	}

	/**
	 * @param productionMethods the productionMethods to set
	 */
	public void setProductionMethods(HashSet<ToolMethodType> productionMethods) {
		this.productionMethods = productionMethods;
	}
	
	
	
	
	

}
