package util;

public class ToolConstant {

	// Directories and files
	public static String CONFIG_FILE_PATH = "";
//	public static final String WORKING_DIR = System.getProperty("user.dir");
//	public static final String TEST_CASES_JAVA_DIR = WORKING_DIR + "/inputTestCases-java/";
//	public static final String TEST_CASES_JAR_DIR = WORKING_DIR + "/inputTestCases-jar/";
//	public static final String PRODUCTION_CLASS_DIR = WORKING_DIR + "/inputProductionClasses/";
//	public static final String TEST_CASE_XML_DIR = WORKING_DIR + "/outputTestCasesXML/";
//	public static final String PRODUCTION_CLASSES_XML_DIR = WORKING_DIR + "/outputProductionClassesXML/";
//	public static final String SRCML_DIR = WORKING_DIR + "/srcML/bin/";
//	public static final String EXCLUSION_FILE = WORKING_DIR + "/Java60RegressionExclusions.txt";
	
	// Configuration properties
	public static final String TEST_CASES_JAR_DIR = "jar_file";
	public static final String TEST_CASES_JAVA_DIR = "java_tc_dir";
	public static final String PRODUCTION_CLASS_JAVA_DIR = "java_pc_dir";
	public static final String TEST_CASE_XML_DIR = "xml_tc_dir";
	public static final String PRODUCTION_CLASSES_XML_DIR = "xml_pc_dir";
	public static final String EXCLUSION_FILE = "exclusion_file";
	public static final String WALA_PROPERTIES_FILE = "wala_properties_file";
	public static final String LOG4J_CONFIG = "log4j_config";
	public static final String SRCML_DIR = "srcML_path";
	public static final String BINARY_DIR = "binary_dir";
	public static final String SCOPE_LOCATION = "scope_file_location";
	
	// Scope read file
	public static final String SCOPE_FILE_NAME = "scope.txt";
	public static final String PRIMORDIAL_STD_LIB = "Primordial,Java,stdlib,none";
	public static final String PRIMORDIAL_JAR_FILE = "Primordial,Java,jarFile,primordial.jar.model";
	public static final String APPLICATION_BIN_DIR = "Application,Java,binaryDir,";
	public static final String WRITING_SCOPE_FILE_ERROR = "Writing scope file error!";
	
	
	// Thresholds absolute values
	public static final String ASSERTION_ROULETTE_ABS = "assertion_roulette_abs";
	public static final String EAGER_TEST_ABS = "eager_test_abs";
	public static final String INDIRECT_TESTING_ABS = "indirect_testing_abs";
	public static final String GENERAL_FIXTURE_ABS = "general_fixture_abs";
	public static final String MYSTERY_GUEST_ABS = "mystery_guest_abs";
	public static final String SENSITIVE_EQUALITY_ABS = "sensitive_equality_abs";
	public static final String CODE_DUPLICATION_ABS = "code_duplication_abs";
	public static final String LAZY_TEST_ABS = "lazy_test_abs";
	
	// Thresholds percentage values
	public static final String ASSERTION_ROULETTE_PERC = "assertion_roulette_perc";
	public static final String EAGER_TEST_PERC = "eager_test_perc";
	public static final String INDIRECT_TESTING_PERC = "indirect_testing_perc";
	public static final String GENERAL_FIXTURE_PERC  = "general_fixture_perc";
	public static final String MYSTERY_GUEST_PERC  = "mystery_guest_perc";
	public static final String SENSITIVE_EQUALITY_PERC  = "sensitive_equality_perc";
	public static final String CODE_DUPLICATION_PERC  = "code_duplication_perc";
	public static final String LAZY_TEST_PERC = "lazy_test_perc";
	
	// Translator
	public static final int PRODUCTION_CLASS = 0;
	public static final int TEST_CLASS = 1;

	// Tool
	public static final String SRCML_COMMAND = "srcml";
	public static final String SRCML_OUTPUT_OPTION = "-o";

	// Tool Error
	public static final String SRCML_ERROR = "srcml tool execution error!";
	public static final String WALA_ILLEGAL_ARG_ERROR = "Illegal argument exception in graph building";
	public static final String CALL_GRAPH_BUILDER_ERROR = "Call graph builder cancel exception!";
	public static final String BUILD_CALL_GRAPH_ERROR = "Problem with jar file or in graph building";
	
	// XML Parser Errors
	public static final String PARSE_EXCEPTION_MSG = "ParserConfigurationException, document builder error!";
	public static final String SAX_EXCEPTION_MSG = "SAX Exception, parse method error!";
	public static final String IO_EXCEPTION_MSG = "IO Exception, parse method error!";
	
	// Annotations
	public static final String TEST_ANNOTATION = "Test";
	public static final String BEFORE_ANNOTATION = "Before";
	public static final String BEFORE_CLASS_ANNOTATION = "BeforeClass";

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
	
	//Assert API info
	public static final String JUNIT_PACKAGE = "org/junit/Assert";

	// File API types
	public static final String[] FILE_API_TYPES = { "File", "BufferedReader", "FileReader", "FileWriter", "IOException",
			"FileWriter", "PrintWriter", "Scanner", "Connection" };

	// File API methods
	public static final String[] FILE_API_METHODS = { "readLine", /*"nextLine",*/ /*"close",*/ "read", "getConnection", 
			"createStatement", "executeQuery", "executeUpdate" };

	// toString method
	public static final String TO_STRING = "toString";
	
	// Object class
	public static final String OBJECT_CLASS = "Object";
	
	// Operators
	public static final String NEW_OPERATOR = "new";

	// XML ELEMENTS NAME
	public static final String ANNOTATION = "annotation";
	public static final String NAME = "name";
	public static final String FUNCTION = "function";
	public static final String CALL = "call";
	public static final String TYPE = "type";
	public static final String ARGUMENT_LIST = "argument_list";
	public static final String ARGUMENT = "argument";
	public static final String LITERAL = "literal";
	public static final String UNIT = "unit";
	public static final String OPERATOR = "operator";
	public static final String BLOCK = "block";
	public static final String INIT = "init";
	public static final String PACKAGE = "package";
	
	//XML ATTRIBUTES NAME
	public static final String FILENAME_ATTRIBUTE = "filename";
	
	//WALA Elements
	public static final String APPLLICATION_CLASS_LOADER = "Application";
	
	//Types
	public static final String STRING_TYPE_LOWER = "string";
	public static final String STRING_TYPE_UPPER = "String";
	
	//Extension
	public static final String JAVA_EXTENSION = "java";

	//Chars
	public static final char PATH_SEPARATOR = '/';
	public static final char DOT = '.';
	public static final char MINUS = '-';
	
}
