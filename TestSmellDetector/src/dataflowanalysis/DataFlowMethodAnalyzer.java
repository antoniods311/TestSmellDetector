package dataflowanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.graph.traverse.BFSPathFinder;

import util.ToolConstant;
import util.prodclass.ToolMethodType;
import util.tooldata.ToolData;

public class DataFlowMethodAnalyzer {

	private CGNode node;
	private IR ir;
	private HashSet<String> results;

	/**
	 * @param node
	 */
	public DataFlowMethodAnalyzer(CGNode node) {
		this.node = node;
		this.ir = node.getIR();
		this.results = new HashSet<String>();
	}

	/**
	 * This method calculates production class methods reached from
	 * test method node 
	 * 
	 * @param data
	 * @param testMethod
	 */
	public void calculatePCMethodsCall(ToolData data, String testMethod) {
		
		for (ToolMethodType toolMethodType : data.getProductionMethods()) {
			Iterator<CGNode> iter = data.getCallGraph().iterator();
			while(iter.hasNext()){
				CGNode dest = iter.next();
				IMethod iMethod = dest.getMethod();
		    	MethodReference methRef = iMethod.getReference();
		    	TypeReference typeRef = methRef.getDeclaringClass();
		    	ClassLoaderReference classLoaderRef = typeRef.getClassLoader();
		    	if (classLoaderRef.getName().toString().equalsIgnoreCase(ToolConstant.APPLLICATION_CLASS_LOADER)
						&& iMethod.getName().toString().equalsIgnoreCase(toolMethodType.getMethodName())) {
		    		BFSPathFinder<CGNode> pathFinder = new BFSPathFinder<CGNode>(data.getCallGraph(),node,dest);
		    		List<CGNode> path = pathFinder.find();
		    		if(path!=null && path.size()>0){
		    			results.add(toolMethodType.getMethodName());
		    		}

				}
			}
		}	
	}

	/**
	 * @return the results
	 */
	public HashSet<String> getResults() {
		return results;
	}
	
	/**
	 * @return IR object
	 */
	public IR getIR() {
		return ir;
	}

	/**
	 * @return Call Graph Node object
	 */
	public CGNode getCGNode() {
		return node;
	}

}
