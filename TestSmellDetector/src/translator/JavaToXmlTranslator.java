package translator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.TestSmellsAnalyzer;
import util.PathTool;
import util.ToolConstant;

/**
 * 
 * @author antoniods311
 *
 */
public class JavaToXmlTranslator implements Translator {

	private File sourceFile, xmlFile;
	private String input, output;
	private static Logger log;
	private static String srcML_dir;
	private static String test_cases_java_dir;
	private static String production_classes_java_dir;
	private static String production_classes_xml_dir;
	private static String test_case_xml_dir;
	
	public JavaToXmlTranslator() {
		/*
		 * Lettura delle info di configurazione
		 */
		Properties prop = new Properties();
		InputStream input = null;
		try{
			input = new FileInputStream(ToolConstant.CONFIG_FILE_PATH);
			prop.load(input);
			
			srcML_dir = prop.getProperty(ToolConstant.SRCML_DIR);
			production_classes_java_dir = prop.getProperty(ToolConstant.PRODUCTION_CLASS_JAVA_DIR);
			test_cases_java_dir = prop.getProperty(ToolConstant.TEST_CASES_JAVA_DIR);
			production_classes_xml_dir = prop.getProperty(ToolConstant.PRODUCTION_CLASSES_XML_DIR);
			test_case_xml_dir = prop.getProperty(ToolConstant.TEST_CASE_XML_DIR);
		}
		catch(IOException io){
			log.info("Error reading configuration file!");
			io.printStackTrace();
		}
		finally{
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
	}

//	public JavaToXmlTranslator(File sourceFile, String input, String output) {
//		super();
//		this.sourceFile = sourceFile;
//		this.input = input;
//		this.output = output;
//		log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
//	}

	/* (non-Javadoc)
	 * @see translator.Translator#translate()
	 */
	@Override
	public File translate() {

		String command = srcML_dir + ToolConstant.SRCML_COMMAND;
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(command, input, ToolConstant.SRCML_OUTPUT_OPTION, output);
			procBuilder = procBuilder.directory(new File(srcML_dir));
			Process process = procBuilder.start();
			process.waitFor(); //wait process termination
			xmlFile = new File(output);

		} catch (IOException e) {
			log.error(ToolConstant.SRCML_ERROR);
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error(ToolConstant.SRCML_ERROR);
			e.printStackTrace();
		}
		return xmlFile;
	}

	@Override
	public void load(File file, int type) {
		this.setSourceFile(file);
		if(type == ToolConstant.PRODUCTION_CLASS){
			this.input = file.getAbsolutePath();
			this.output = production_classes_xml_dir + PathTool.extractPackage(file.getAbsolutePath(), production_classes_java_dir) + ".xml";
		}else{
			this.input = file.getAbsolutePath();
			this.output = test_case_xml_dir + PathTool.extractPackage(file.getAbsolutePath(), test_cases_java_dir) + ".xml";
		}
		
	}

	/**
	 * @return soruce file
	 */
	public File getSourceFile() {
		return sourceFile;
	}

	/**
	 * @param file
	 */
	public void setSourceFile(File file) {
		this.sourceFile = file;
	}

	/**
	 * @return the input file
	 */
	public String getInput() {
		return input;
	}

	/**
	 * @param the input file to set
	 */
	public void setInput(String input) {
		this.input = input;
	}

	/**
	 * @return the output path
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the xmlFile
	 */
	public File getXmlFile() {
		return xmlFile;
	}
	
	

}
