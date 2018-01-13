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
	
	private File sourceFile;
	private File xmlFile = null;
	
	@Override
	public File translate() {
		// chiamata a tool di traduzione.
		String workingDir = System.getProperty("user.dir");
		String command = workingDir+"/srcML/bin/srcml";
		String testCaseName = "HelloWorld.java";
		String inputTc = workingDir+"/inputTestCases/"+testCaseName;
		String outputPath = workingDir+"/outputXML/"+testCaseName+".xml";
		
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(command,inputTc, "-o", outputPath);
		    procBuilder = procBuilder.directory(new File(workingDir+"/srcML/bin"));
			procBuilder.start();
			xmlFile = new File(outputPath);
			
//		    Process process = procBuilder.start();
//			InputStream is = process.getInputStream();
//		    InputStreamReader isr = new InputStreamReader(is);
//		    BufferedReader br = new BufferedReader(isr);
//		    String line;
//			while ((line = br.readLine()) != null) {
//			       System.out.println(line);
//			}
			
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
