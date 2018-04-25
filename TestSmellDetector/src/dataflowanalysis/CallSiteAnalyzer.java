package dataflowanalysis;

import java.util.HashSet;

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.IR;

import util.prodclass.ToolMethodType;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class CallSiteAnalyzer {

	private ToolData data;
	private CGNode node;
	private IR ir;
	
	public CallSiteAnalyzer(ToolData data, CGNode node){
		this.data = data;
		this.node = node;
		this.ir = node.getIR();
	}
	
	public HashSet<ToolMethodType> analyzeCallSite(HashSet<ToolMethodType> testedMethods){
		
		
		
		
		
		return testedMethods;
	}
	
}
