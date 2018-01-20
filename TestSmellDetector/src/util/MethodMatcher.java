package util;

public class MethodMatcher {
	
	public MethodMatcher(){
		
	}

	/*
	 * metodo che controlla se il nome del nodo passato è un assert
	 */
	public boolean isAssertMethod(String value) {
		
		boolean isAssert = false;
		int index = 0;
		while(index < ToolConstant.ASSERT_METHODS.length && !isAssert){
			if(value.equalsIgnoreCase(ToolConstant.ASSERT_METHODS[index]))
				isAssert = true;
			index++;
		}		
		return isAssert;
	}

	/*
	 * metodo che controlla se il nodo contiene la chiamata al metodo toString()
	 */
	public boolean isToStringMethod(String textContent) {

		boolean hasToStringMethod = false;
		
		if(textContent.equalsIgnoreCase(ToolConstant.TO_STRING)) 
			hasToStringMethod = true;
		
		return hasToStringMethod;
	}
	
}