package dataflowanalysis;

import com.ibm.wala.ssa.DefUse;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;

import util.prodclass.ToolMethodType;
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
	 * @param def the instruction that defines the variable
	 * @param the variable defined by SSAInstruction object
	 */
	public String analyzeUse(SSAInstruction def, int var){
		
		boolean pmCallFound = false;
		String pcDefinitionMethod = null;
		ToolMethodType[] prodMethods = (ToolMethodType[]) data.getProductionMethods().toArray();
		int index = 0;
		int size = prodMethods.length;
		/*
		 * A. controllo se l'istruzione def contiene o meno
		 * la chiamata ad un metodo della PC (es: sum()).
		 * altrimenti pmCallFound resta false e quindi
		 * entro nell'if del punto B.
		 */
		while(index<size && !pmCallFound){
			String pcMethod = prodMethods[index].getMethodName();
			if(def.toString().contains(pcMethod)){
				pmCallFound = true;
				pcDefinitionMethod = pcMethod;
			}
			index++;
		}
		/*
		 * B. Se non ho trovato il metodo della pc nella
		 * definizione significa che probabilmente si
		 * sta usando un'altra variabile per la definizione
		 * di var. Quindi è necessario risalire le 
		 * istruzioni di definizione.
		 * (Vedere conversion(J) in questo caso)
		 */
		if(!pmCallFound) {
			pcDefinitionMethod = checkConversion(def,var);
		}
		
		return pcDefinitionMethod;
		
	}

	
	private String checkConversion(SSAInstruction instruction, int var) {
		
		/*
		 * t = sum(1+2);
		 * s = t;
		 * assertEquals("message",s,3);
		 * 
		 * 10 = conversion(J) 8
		 * 
		 * 10 = s 	and		8 = t
		 * 
		 */
		String method = null;
		if(instruction.toString().contains("conversion")){
			int usedVar = instruction.getUse(0); //leggo la variabile usata nella definizione
			DefUse defUse = new DefUse(ir);
			SSAInstruction definition = defUse.getDef(usedVar);
			method = analyzeUse(definition, usedVar);
		}
		return method;
	}
	
	

}
