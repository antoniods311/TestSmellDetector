package translator;

import java.io.File;

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
