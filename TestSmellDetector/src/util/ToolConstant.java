package util;

public class ToolConstant {
	
	//Directories
	public static final String WORKING_DIR = System.getProperty("user.dir");
	public static final String TEST_CASES_DIR = WORKING_DIR+"/inputTestCases/";
	public static final String XML_DIR = WORKING_DIR+"/outputXML/";
	public static final String SRCML_DIR = WORKING_DIR+"/srcML/bin/";
	
	//Errors
	public static final String SRCML_ERROR = "srcml tool execution error!";
	
	//Tool
	public static final String SRCML_COMMAND = "srcml";
	public static final String SRCML_OUTPUT_OPTION = "-o";
	
}
