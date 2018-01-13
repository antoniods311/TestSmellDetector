package translator;

import java.io.File;
import java.io.IOException;
import util.ToolConstant;

/**
 * 
 * @author antoniods311
 *
 */
public class JavaToXmlTranslator implements Translator {
	
	private File sourceFile;
	private File xmlFile = null;
	
	@Override
	public File translate() {
		// chiamata a tool di traduzione.
		String command = ToolConstant.SRCML_DIR+"srcml";
		String testCaseName = sourceFile.getName();
		System.out.println(testCaseName);
		String inputTc = ToolConstant.TEST_CASES_DIR+testCaseName;
		String outputPath = ToolConstant.XML_DIR+testCaseName+".xml";
		
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(command,inputTc, "-o", outputPath);
		    procBuilder = procBuilder.directory(new File(ToolConstant.SRCML_DIR));
			procBuilder.start();
			xmlFile = new File(outputPath);
			
		} catch (IOException e) {
			System.out.println("Command exec error");
			e.printStackTrace();
		}
		
		return xmlFile;
	}

	@Override
	public int load(File file) {
		this.setSourceFile(file);
		return 0;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File file) {
		this.sourceFile = file;
	}

}
