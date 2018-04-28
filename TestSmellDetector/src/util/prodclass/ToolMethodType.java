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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classType == null) ? 0 : classType.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ToolMethodType other = (ToolMethodType) obj;
		if (classType == null) {
			if (other.classType != null)
				return false;
		} else if (!classType.equals(other.classType))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		return true;
	}
	
	
	
	

}
