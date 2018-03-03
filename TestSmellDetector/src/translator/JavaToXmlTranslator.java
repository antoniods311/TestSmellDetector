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
	public void load(File file) {
		this.setSourceFile(file);
		this.input = ToolConstant.TEST_CASES_DIR + file.getName();
		this.output = ToolConstant.XML_DIR + file.getName() + ".xml";
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File file) {
		this.sourceFile = file;
	}

}
