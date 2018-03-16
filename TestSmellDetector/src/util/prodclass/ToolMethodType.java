package util.prodclass;

public class ToolMethodType {
	
	private String classType;
	private String method;
	
	/**
	 * @param classType
	 * @param method
	 */
	public ToolMethodType(String classType, String method) {
		super();
		this.classType = classType;
		this.method = method;
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
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	

}
