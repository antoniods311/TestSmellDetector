package translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
		StringBuffer output = new StringBuffer();
		String command = "/Users/antoniods311/Desktop/srcML/bin/srcml "
				+ " /Users/antoniods311/Desktop/testFiles/HelloWorld.java "
				+ "-o /Users/antoniods311/Desktop/testFiles/HelloWorld.java.xml";
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			
			BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line = "";
	while ((line = reader.readLine())!= null) {
		output.append(line + "\n");
	}
			
		} catch (IOException e) {
			System.out.println("command exec error - IOException");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("command exec error - InterruptedException");
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
