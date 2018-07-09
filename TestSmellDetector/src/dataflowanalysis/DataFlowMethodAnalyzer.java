package dataflowanalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
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

	/**
	 * @param node
	 */
	public DataFlowMethodAnalyzer(CGNode node) {
		this.node = node;
		this.ir = node.getIR();
	}
	
	/**
	 * This method finds which production class methods 
	 * are reached from test method node 
	 * 
	 * @param data
	 * @param testMethod
	 */
	public HashSet<String> calculatePCMethodsCall(ToolData data, String testMethod) {
		
		HashSet<String> results = new HashSet<String>();
		
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
		return results;
	}
	
	/**
	 * This method finds which production class 
	 * methods are tested by test method 
	 * 
	 * @param data
	 * @param testMethod
	 * @return methods tested by the test method parameter
	 */
	public HashSet<String> getPCMethodsTestedByTestMethod(ToolData data, String testMethod){
		/*
		 * 1. recuperare il nodo del testMethod (node passato al costruttore)
		 * 2. individuare in questo tutte le istruzioni che sono degli assert
		 * 3. per ogni assert recuperare le variabili
		 * 4. chiamare un metodo che prende le variabili dell'assert e l'IR del
		 * metodo e va a fare l'analisi. 
		 */
		
		//System.out.println("###"+testMethod);
		
		HashSet<String> testedMethods = new HashSet<String>();
		IR ir = node.getIR();
		Iterator<SSAInstruction> istrIter = ir.iterateAllInstructions();
		while(istrIter.hasNext()){
			SSAInstruction instruction = istrIter.next();
			String instructionString = instruction.toString();
			if(instructionString!=null && (instructionString.contains(ToolConstant.JUNIT_PACKAGE) && isAssert(instructionString))){
				int limit = instruction.getNumberOfUses();
				for(int g=0; g<limit; g++){
					DefUse defUse = new DefUse(ir);
					int usedVariable = instruction.getUse(g);
					
	    			SSAInstruction uvDefinition = defUse.getDef(usedVariable); //recupero la definizione della variabile usata dall'assert
//	    			if(uvDefinition!=null)
//	    				System.out.println(testMethod+"-->"+uvDefinition.toString());
	    			/*
	    			 * 4.per ogni uso ora va richiamato in metodo di analisi delle variabili,
	    			 * controllando prima che la SSAInstruction non sia "null" (caso delle costanti)
	    			 * Dovrei aspettarmi una String restituita dal metodo di analisi della variabile
	    			 */
	    			
//	    			if(testMethod.equals("testIterateFilesAndDirs")){
//    					System.out.println("usedVariable: "+usedVariable);
//    				}
	    			
	    			String testedMethod = null;
	    			if(uvDefinition!=null){
	    				AssertVariablesAnalyzer ava = new AssertVariablesAnalyzer(data, ir);
	    				testedMethod = ava.analyzeUse(uvDefinition, usedVariable,0);
	    				testedMethods.add(testedMethod);
	    				
//	    				if(testMethod.equals("testIterateFilesAndDirs")){
//	    					System.out.println("metodotestato: "+testedMethod);
//	    				}
	    				
	    			}		
				}
			}		
		}
		//System.out.println("###"+testedMethods.size()+"\n");
		return testedMethods;
	}
	
	/**
	 * This method checks if @param instructionString 
	 * contains an assert call
	 * 
	 * @param instructionString
	 * @return
	 */
	private boolean isAssert(String instructionString) {
		
		boolean isAssertMethod = false;
		int index = 0;
		int size = ToolConstant.ASSERT_METHODS.length;
		while(!isAssertMethod && index<size){
			if(instructionString.contains(ToolConstant.ASSERT_METHODS[index])){
				isAssertMethod = true;
			}
			index++;
		}
		return isAssertMethod;
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
