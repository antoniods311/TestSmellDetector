package dataflowanalysis;

import java.util.HashSet;
import java.util.Iterator;

import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.ipa.callgraph.CGNode;

import util.prodclass.ToolMethodType;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class CallSiteAnalyzer {

	private CGNode node;
	
	public CallSiteAnalyzer(ToolData data, CGNode node){
		this.node = node;
	}
	
	
	public HashSet<ToolMethodType> analyzeCallSite(HashSet<ToolMethodType> testedMethods, HashSet<String> testedMethodsNames){
		
		Iterator<CallSiteReference> csi = node.iterateCallSites();
		while(csi.hasNext()){
			CallSiteReference csr = csi.next();
			String className = csr.getDeclaredTarget().getDeclaringClass().getName().getClassName().toString();
			String methodName = csr.getDeclaredTarget().getName().toString();
			
			for(String m : testedMethodsNames){
				if(m.equals(methodName)){
					testedMethods.add(new ToolMethodType(className, methodName));
				}
			}
		}
		
		return testedMethods;
	}
	
}
