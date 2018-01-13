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
	
	private File sourceFile, xmlFile;
	private String input, output;
	
	@Override
	public File translate() {
		// chiamata a tool di traduzione.
		String command = ToolConstant.SRCML_DIR+"srcml";
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(command,input, "-o", output);
		    procBuilder = procBuilder.directory(new File(ToolConstant.SRCML_DIR));
			procBuilder.start();
			xmlFile = new File(output);
			
		} catch (IOException e) {
			System.out.println("Command exec error");
			e.printStackTrace();
		}
		return xmlFile;
	}

	@Override
	public void load(File file) {
		this.setSourceFile(file);
		this.input = ToolConstant.TEST_CASES_DIR+file.getName();
		this.output = ToolConstant.XML_DIR+file.getName()+".xml";
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File file) {
		this.sourceFile = file;
	}

}
