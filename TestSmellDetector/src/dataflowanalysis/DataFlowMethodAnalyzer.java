package dataflowanalysis;

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.IR;

public class DataFlowMethodAnalyzer {

	private CGNode node;
	private IR ir;
	
	public DataFlowMethodAnalyzer(CGNode node){
		this.node = node;
		this.ir = node.getIR();
	}
	
	/**
	 * @return IR object
	 */
	public IR getIR(){
		return ir;
	}
	
	
}
