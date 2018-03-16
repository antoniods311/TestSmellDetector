package util.prodclass;

public class ToolMethodType {
	
	private String classType;
	private String methodName;
	
	/**
	 * @param classType
	 * @param method
	 */
	public ToolMethodType(String classType, String methodName) {
		super();
		this.classType = classType;
		this.methodName = methodName;
	}

	/**
	 * @return the classType
	 */
	public String getClassType() {
		return classType;
	}

	/**
	 * @param classType the classType to set
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}

	/**
	 * @return the method
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethodName(String method) {
		this.methodName = method;
	}

	@Override
	public String toString() {
		//return "ToolMethodType [classType=" + classType + ", method=" + method + "]";
		return classType+"."+methodName;
	}
	
	

}
