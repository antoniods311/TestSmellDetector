package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author antoniods311
 *
 */
public class FileFinder {

	private static Logger log = LogManager.getLogger(FileFinder.class.getName());
	
	/**
	 * @param dirPath
	 * @return fileList in directory
	 */
	public static ArrayList<String> findJava(String inputDirPath){
		
		/*
		 * Controllare se il parametro in ingresso finisce con /
		 * in quest cas va rimosso.
		 */
		String dirPath = inputDirPath;
		if(inputDirPath.toCharArray()[inputDirPath.length()-1] == '/'){
			dirPath = inputDirPath.substring(0,inputDirPath.length()-1);
		}
		
		ArrayList<String> fileList = new ArrayList<String>();	
		String command = "find";
		String option = "-name";
		String fileType = "*.java";
		ProcessBuilder processBuilder;
		//processBuilder.inheritIO();
		try {
			processBuilder = new ProcessBuilder(command,dirPath,option,fileType);
			Process process = processBuilder.start();
			process.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			
			while ( (line = reader.readLine()) != null) {
				builder.append(line);
				fileList.add(line);
				builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();
			reader.close();
			
		} catch (IOException e) {
			log.error("errore find");
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("errore find interrupted exception");
			e.printStackTrace();
		}
		
		return fileList;
	}
	
}
