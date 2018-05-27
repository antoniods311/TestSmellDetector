package util.prodclass;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import util.PathTool;
import util.ToolConstant;
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
	public HashSet<ToolMethodType> getClassMethods(String dir){
		
		HashSet<ToolMethodType> prodClassesMethods = new HashSet<ToolMethodType>();
		
		for(File xmlProdClass : productionClasses){
			
			srcAnalyzer = new SourceClassAnalyzer(xmlProdClass);
			
			String pack = xmlProdClass.getAbsolutePath();
			String packPlusClass = PathTool.extractPackage(pack, dir);
			String correctPack = "";
			if(packPlusClass!=null){
				ArrayList<String> directories = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(packPlusClass, Character.toString(ToolConstant.MINUS));
				int lastTokenIndex = -1;
				while(tokenizer.hasMoreTokens()){
					lastTokenIndex++;
					directories.add(tokenizer.nextToken());
				}
				
				boolean first = true;
				for(int i=0; i<directories.size(); i++){
					if(i != lastTokenIndex){
						if(first){
							correctPack = directories.get(i);
							first = false;
						}else{
							correctPack = correctPack + ToolConstant.DOT + directories.get(i);
						}
					}
				}
			}				
			for(String method : srcAnalyzer.getClassMethods()){			
				prodClassesMethods.add(new ToolMethodType(srcAnalyzer.getClassName(), method, correctPack));
			}
		}
		return prodClassesMethods;
	}
	
	
}
