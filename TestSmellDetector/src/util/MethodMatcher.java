package util;

public class MethodMatcher {

	public MethodMatcher() {

	}

	/*
	 * metodo che controlla se il nome del nodo passato è un assert
	 */
	public boolean isAssertMethod(String value) {

		boolean isAssert = false;
		int index = 0;
		while (index < ToolConstant.ASSERT_METHODS.length && !isAssert) {
			if (value.equalsIgnoreCase(ToolConstant.ASSERT_METHODS[index]))
				isAssert = true;
			index++;
		}
		return isAssert;
	}

	/*
	 * metodo che controlla se il nodo contiene la chiamata al metodo toString()
	 */
	public boolean isToStringMethod(String textContent) {
		return textContent.equalsIgnoreCase(ToolConstant.TO_STRING);
	}

	public boolean isFailMethod(String methodName) {
		return methodName.equals(ToolConstant.FAIL);
	}

	public boolean isAssertTrueMethod(String methodName) {
		return methodName.equals(ToolConstant.ASSERT_TRUE);
	}

	public boolean isAssertFalseMethod(String methodName) {
		return methodName.equals(ToolConstant.ASSERT_FALSE);
	}

	public boolean isAssertEqualsMethod(String methodName) {
		return methodName.equals(ToolConstant.ASSERT_EQUALS);
	}

	public boolean isAssertNullMethod(String methodName) {
		return methodName.equals(ToolConstant.ASSERT_NULL);
	}

	public boolean isAssertNotNullMethod(String methodName) {
		return methodName.equals(ToolConstant.ASSERT_NOT_NULL);
	}

	public boolean isAssertSameMethod(String methodName) {
		return methodName.equals(ToolConstant.ASSERT_SAME);
	}

	public boolean isAssertNotSameMetho(String methodName) {
		return methodName.equals(ToolConstant.ASSERT_NOT_SAME);
	}

}
