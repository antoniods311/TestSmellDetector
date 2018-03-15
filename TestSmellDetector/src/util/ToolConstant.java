package util;

import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;

public class ToolConstant {

	// Directories and files
	public static final String WORKING_DIR = System.getProperty("user.dir");
	public static final String TEST_CASES_JAVA_DIR = WORKING_DIR + "/inputTestCases-java/";
	public static final String TEST_CASES_JAR_DIR = WORKING_DIR + "/inputTestCases-jar/";
	public static final String XML_DIR = WORKING_DIR + "/outputXML/";
	public static final String SRCML_DIR = WORKING_DIR + "/srcML/bin/";
	public static final String EXCLUSION_FILE = WORKING_DIR + "Java60RegressionExclusions.txt";

	// Tool
	public static final String SRCML_COMMAND = "srcml";
	public static final String SRCML_OUTPUT_OPTION = "-o";

	// Tool Error
	public static final String SRCML_ERROR = "srcml tool execution error!";
	public static final String WALA_ILLEGAL_ARG_ERROR = "Illegal argument exception in graph building";
	public static final String CALL_GRAPH_BUILDER_ERROR = "Call graph builder cancel exception!";

	// XML Parser Errors
	public static final String PARSE_EXCEPTION_MSG = "ParserConfigurationException, document builder error!";
	public static final String SAX_EXCEPTION_MSG = "SAX Exception, parse method error!";
	public static final String IO_EXCEPTION_MSG = "IO Exception, parse method error!";

	// Annotations
	public static final String TEST_ANNOTATION = "Test";

	// Assert Methods
	public static final String[] ASSERT_METHODS = { "fail", "assertTrue", "assertFalse", "assertEquals", "assertNull",
			"assertNotNull", "assertSame", "assertNotSame" };
	public static final String FAIL = "fail";
	public static final String ASSERT_TRUE = "assertTrue";
	public static final String ASSERT_FALSE = "assertFalse";
	public static final String ASSERT_EQUALS = "assertEquals";
	public static final String ASSERT_NULL = "assertNull";
	public static final String ASSERT_NOT_NULL = "assertNotNull";
	public static final String ASSERT_SAME = "assertSame";
	public static final String ASSERT_NOT_SAME = "assertNotSame";

	// File API types
	public static final String[] FILE_API_TYPES = { "File", "BufferedReader", "FileReader", "FileWriter", "IOException",
			"FileWriter", "PrintWriter", "Scanner" };

	// File API methods
	public static final String[] FILE_API_METHODS = { "readLine", "nextLine", "close", "read" };

	// toString method
	public static final String TO_STRING = "toString";

	// XML ELEMENTS NAME
	public static final String ANNOTATION = "annotation";
	public static final String NAME = "name";
	public static final String FUNCTION = "function";
	public static final String CALL = "call";
	public static final String TYPE = "type";
	public static final String ARGUMENT_LIST = "argument_list";
	public static final String ARGUMENT = "argument";
	public static final String LITERAL = "literal";
	
	//Types
	public static final String STRING_TYPE_LOWER = "string";
	public static final String STRING_TYPE_UPPER = "String";

}
