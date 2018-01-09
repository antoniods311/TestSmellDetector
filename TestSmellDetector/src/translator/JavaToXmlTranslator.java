package translator;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author antoniods311
 *
 */
public class JavaToXmlTranslator implements Translator {
	
	private File file;
	
	@Override
	public String translate() {
		// chiamata a tool di traduzione.
		String command = "/home/antoniods311/Desktop/srcML/bin/srcml "
				+ "../../testFiles/HelloWorld.java "
				+ "-o HelloWorld.xml";
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.out.println("command exec error");
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public int load(File file) {
		this.setFile(file);
		return 0;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
