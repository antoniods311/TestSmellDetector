package result;

import util.prodclass.ToolMethodType;

public class IndirectTestingResult {

	private ToolMethodType testerMethod;
	private ToolMethodType indirectTestedMethod;
	
	public IndirectTestingResult() {}
	
	/**
	 * @param testerMethod
	 * @param indirectTestedMethod
	 */
	public IndirectTestingResult(ToolMethodType testerMethod, ToolMethodType indirectTestedMethod) {
		super();
		this.testerMethod = testerMethod;
		this.indirectTestedMethod = indirectTestedMethod;
	}

	/**
	 * @return the testerMethod
	 */
	public ToolMethodType getTesterMethod() {
		return testerMethod;
	}

	/**
	 * @param testerMethod the testerMethod to set
	 */
	public void setTesterMethod(ToolMethodType testerMethod) {
		this.testerMethod = testerMethod;
	}

	/**
	 * @return the indirectTestedMethod
	 */
	public ToolMethodType getIndirectTestedMethod() {
		return indirectTestedMethod;
	}

	/**
	 * @param indirectTestedMethod the indirectTestedMethod to set
	 */
	public void setIndirectTestedMethod(ToolMethodType indirectTestedMethod) {
		this.indirectTestedMethod = indirectTestedMethod;
	}
	
	
	
}
