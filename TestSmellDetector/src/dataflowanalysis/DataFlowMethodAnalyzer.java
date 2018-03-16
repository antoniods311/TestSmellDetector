package dataflowanalysis;

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.IR;

public class DataFlowMethodAnalyzer {

	private CGNode node;
	private IR ir;
	
	/**
	 * @param node
	 */
	public DataFlowMethodAnalyzer(CGNode node){
		this.node = node;
		this.ir = node.getIR();
	}

	public void analyze() {
		
		
		
	}

	/**
	 * @return IR object
	 */
	public IR getIR(){
		return ir;
	}
	
	/**
	 * @return Call Graph Node object
	 */
	public CGNode getCGNode(){
		return node;
	}
	
}
