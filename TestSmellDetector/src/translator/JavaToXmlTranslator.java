package translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
		String command = "/Users/antoniods311/Desktop/srcML/bin/srcml";
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(command, "../../testFiles/HelloWorld.java");
			procBuilder = procBuilder.directory(new File("/Users/antoniods311/Desktop/srcML/bin"));
			Process process = procBuilder.start();
			
			 InputStream is = process.getInputStream();
		     InputStreamReader isr = new InputStreamReader(is);
		     BufferedReader br = new BufferedReader(isr);
		     String line;
			
			while ((line = br.readLine()) != null) {
			       System.out.println(line);
			}
			
		} catch (Exception e) {
			System.out.println("Command exec error");
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
