package translator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.TestSmellsAnalyzer;
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
	private static String SRCML_DIR;
	private static String TEST_CASES_JAVA_DIR;
	private static String PRODUCTION_CLASS_DIR;
	private static String PRODUCTION_CLASSES_XML_DIR;
	private static String TEST_CASE_XML_DIR;
	
	public JavaToXmlTranslator() {
		/*
		 * Lettura delle info di configurazione
		 */
		Properties prop = new Properties();
		InputStream input = null;
		try{
			input = new FileInputStream(ToolConstant.CONFIG_FILE_PATH);
			prop.load(input);
			
			SRCML_DIR = prop.getProperty("srcML_path");
			PRODUCTION_CLASS_DIR = prop.getProperty("java_pc_dir");
			TEST_CASES_JAVA_DIR = prop.getProperty("java_tc_dir");
			PRODUCTION_CLASSES_XML_DIR = prop.getProperty("xml_pc_dir");
			TEST_CASE_XML_DIR = prop.getProperty("xml_tc_dir");
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

		String command = SRCML_DIR + ToolConstant.SRCML_COMMAND;
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(command, input, ToolConstant.SRCML_OUTPUT_OPTION, output);
			procBuilder = procBuilder.directory(new File(SRCML_DIR));
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
			this.input = PRODUCTION_CLASS_DIR + file.getName();
			this.output = PRODUCTION_CLASSES_XML_DIR + file.getName() + ".xml";
		}else{
			this.input = TEST_CASES_JAVA_DIR + file.getName();
			this.output = TEST_CASE_XML_DIR + file.getName() + ".xml";
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
