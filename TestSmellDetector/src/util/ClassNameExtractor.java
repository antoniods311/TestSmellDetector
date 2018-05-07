package util;

import java.util.StringTokenizer;

/**
 * @author antoniods311
 *
 */
public class ClassNameExtractor {

	
	/**
	 * This method returns test class name file
	 * @param path
	 * @return testClassName
	 */
	public static String extractClassNameFromPath(String path){
		
		String className = null;
		StringTokenizer tokenizer = new StringTokenizer(path,"/");
		String lastToken = null;
		while(tokenizer.hasMoreTokens())
			lastToken = tokenizer.nextToken();
		
		if(lastToken!=null){
			StringTokenizer secondTokenizer = new StringTokenizer(lastToken,".");
			if(secondTokenizer.hasMoreTokens())
				className = secondTokenizer.nextToken();
		}
		
		return className;
	}
}
