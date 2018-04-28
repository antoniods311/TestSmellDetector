package dataflowanalysis;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import util.prodclass.ToolMethodType;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class CallSiteAnalyzer {

	private CGNode node;
	private static int conto = 0;
	
	public CallSiteAnalyzer(ToolData data, CGNode node){
		this.node = node;
	}
	
	
	public HashSet<ToolMethodType> analyzeCallSite(HashSet<ToolMethodType> testedMethods, HashSet<String> testedMethodsNames, File xml, String methodName2){
		conto++;
		
		IMethod method = node.getMethod();
    	MethodReference ref = method.getReference();
    	TypeReference tr = ref.getDeclaringClass();
    	ClassLoaderReference clr = tr.getClassLoader();
		
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
		
		System.out.println("Chiamata n "+conto+" --- file: "+xml.getName()+" --- "+methodName2);
		for(ToolMethodType n : testedMethods){
			System.out.println("*** "+n.getMethodName());
		}
		
		return testedMethods;
	}
	
}
