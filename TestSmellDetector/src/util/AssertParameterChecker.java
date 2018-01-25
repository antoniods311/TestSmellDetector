package util;

import javax.print.attribute.standard.Sides;

import org.w3c.dom.Element;

public class AssertParameterChecker {

	private MethodMatcher methodMatcher;
	
	public AssertParameterChecker(){
		methodMatcher = new MethodMatcher();
	}
	
	public boolean hasMessageParameter(Element call, String name) {

		boolean hasMsgParam = false;
		
		if(methodMatcher.isFailMethod(name)){
			//devo controllare se c'è almeno un parametro di tipo string
			
			
		}else{
			
			
			
		}

		return hasMsgParam;
	}

	
	

}
