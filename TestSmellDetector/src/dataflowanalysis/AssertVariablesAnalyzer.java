package dataflowanalysis;

import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;

import util.tooldata.ToolData;

public class AssertVariablesAnalyzer {
	
	private IR ir;
	private ToolData data;
	
	/**
	 * Constructor
	 * 
	 * @param ir the test node intermediate representation
	 * @param data info for analysis
	 */
	public AssertVariablesAnalyzer(ToolData data,IR ir){
		this.ir = ir;
		this.data = data;
	}
	
	/**
	 * This method analyze a variable specific use.
	 * 
	 * @param use
	 */
	public void analyzeUse(SSAInstruction use){
		
		
		
	}
	
	

}
