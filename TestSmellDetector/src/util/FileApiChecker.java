package util;

import org.w3c.dom.Element;

public class FileApiChecker {

	/*
	 * metodo che controlla se un tipo è appartentente
	 * ad una API di gestione dei file
	 */
	public boolean isFileApiType(Element typeNode){
		
		boolean isFileType = false;
		int index = 0;
		int size = ToolConstant.FILE_API_TYPES.length;
		while(index < size && !isFileType){
			if(typeNode.getTextContent().equals(ToolConstant.FILE_API_TYPES[index]))
				isFileType = true;
			index++;
		}
		
		return isFileType;
	}
	
	/*
	 * metodo che controlla se un metodo chiamato nel TC
	 * appartiene o meno ad una API di gestione dei file.
	 */
	public boolean isFileApiFunction(Element callNode){
		
		boolean isFileCall = false;
		
		
		return isFileCall;
	}
	
	
}
