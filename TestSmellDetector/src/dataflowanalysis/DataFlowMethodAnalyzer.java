package dataflowanalysis;

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.IR;

public class DataFlowMethodAnalyzer {

	private CGNode node;
	
	public DataFlowMethodAnalyzer(CGNode node){
		this.node = node;
	}
	
	/**
	 * 
	 * @return IR object
	 */
	public IR getIR(){
		return node.getIR();
	}
	
}
