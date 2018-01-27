package util;

import javax.xml.soap.Node;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParameterAnalyzer {

	public ParameterAnalyzer(){
		
	}
	
	public int getParameterNumber(Element argumentList){
		
		int argListSize = 0;
		
		NodeList childNodeList = argumentList.getChildNodes();
		for(int i=0; i<childNodeList.getLength(); i++){
			if(childNodeList.item(i).getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) childNodeList.item(i);
				if(element.getNodeName().equals(ToolConstant.ARGUMENT)) argListSize++;
			}
		}
		return argListSize;
	}
	
	public String getParameterType(Element argument){
		
		String paramType = "";
		
		
		return paramType;
		
	}
	
}
