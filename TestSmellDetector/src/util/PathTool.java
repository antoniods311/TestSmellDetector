package util;

/**
 * 
 * @author antoniods311
 *
 */
public class PathTool {

	/**
	 * 
	 * @param filePath
	 * @param dirPath
	 * @return package substring
	 */
	public static String extractPackage(String filePath, String dirPath){
		
		int index = filePath.indexOf(dirPath);
		String wrongPath = filePath.substring(index+dirPath.length(),filePath.length()); 
		return new String(wrongPath.replace(ToolConstant.PATH_SEPARATOR, ToolConstant.MINUS));
		
	}
	
	/**
	 * @param path
	 * @return path without "/"
	 */
	public static String removeEndSeparator(String path){
	
		String result = path;
		if(path.toCharArray()[path.length()-1] == ToolConstant.PATH_SEPARATOR)
			result = path.substring(0,path.length()-1);

		return result;		
	}
	
	/**
	 * 
	 * @param path
	 * @return return package
	 */
	public static String pathToPackage(String path){	
		return new String(path.replace(ToolConstant.PATH_SEPARATOR, ToolConstant.DOT));
	}
	
}
