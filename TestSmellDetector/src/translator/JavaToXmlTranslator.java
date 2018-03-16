package translator;

import java.io.File;
import java.io.IOException;

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

	public JavaToXmlTranslator() {
		log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
	}

	public JavaToXmlTranslator(File sourceFile, String input, String output) {
		super();
		this.sourceFile = sourceFile;
		this.input = input;
		this.output = output;
		log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
	}


	/* (non-Javadoc)
	 * @see translator.Translator#translate()
	 */
	@Override
	public File translate() {

		String command = ToolConstant.SRCML_DIR + ToolConstant.SRCML_COMMAND;
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(command, input, ToolConstant.SRCML_OUTPUT_OPTION, output);
			procBuilder = procBuilder.directory(new File(ToolConstant.SRCML_DIR));
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
			this.input = ToolConstant.PRODUCTION_CLASS_DIR + file.getName();
			this.output = ToolConstant.PRODUCTION_CLASSES_XML_DIR + file.getName() + ".xml";
		}else{
			this.input = ToolConstant.TEST_CASES_JAVA_DIR + file.getName();
			this.output = ToolConstant.TEST_CASE_XML_DIR + file.getName() + ".xml";
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
