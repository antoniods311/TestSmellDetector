package util.prodclass;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;

import util.eagertest.SourceClassAnalyzer;

public class ProductionClassAnalyzer {

	private ArrayList<File> productionClasses;
	private SourceClassAnalyzer srcAnalyzer;
	
	public ProductionClassAnalyzer(ArrayList<File> prodClasses){
		this.productionClasses = prodClasses;
	}
	
	/**
	 * 
	 * @return set of method for a group of classes
	 */
	public HashSet<ToolMethodType> getClassMethods(){
		
		HashSet<ToolMethodType> prodClassesMethods = new HashSet<ToolMethodType>();
		
		for(File xmlProdClass : productionClasses){
			srcAnalyzer = new SourceClassAnalyzer(xmlProdClass);
			for(String method : srcAnalyzer.getClassMethods()){
				prodClassesMethods.add(new ToolMethodType(srcAnalyzer.getClassName(), method));
			}
		}
		
		return prodClassesMethods;
	}
	
	
}
