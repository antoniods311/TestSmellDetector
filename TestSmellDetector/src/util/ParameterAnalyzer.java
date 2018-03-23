package util;

import java.util.ArrayList;
import java.util.HashSet;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.prodclass.ToolMethodType;
import util.tooldata.ToolData;

public class ParameterAnalyzer {
	
	private ToolData data;

	/**
	 * Constructor for ParameterAnalyzer
	 */
	public ParameterAnalyzer() {
		this.data = null;
	}
	
	/**
	 * Constructor for ParameterAnalyzer
	 */
	public ParameterAnalyzer(ToolData data) {
		this.data = data;
	}

	/**
	 * This method calculates parameters number for the Element
	 * 
	 * @param argumentList
	 * @return
	 */
	public int getParameterNumber(Element argumentList) {

		int argListSize = 0;

		NodeList childNodeList = argumentList.getChildNodes();
		for (int i = 0; i < childNodeList.getLength(); i++) {
			if (childNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) childNodeList.item(i);
				if (element.getNodeName().equals(ToolConstant.ARGUMENT))
					argListSize++;
			}
		}
		return argListSize;
	}
	
	/*
	 * DA IMPLEMENTARE: restituisce la lista di parametri di un ARGUMENT LIST element
	 */
	public ArrayList<String> getParameters(Element currentElement) {
		
		
		
		
		
		
		return null;
	}

	/**
	 * This method calculates the list of parameters which represents a production class 
	 * method call, starting from an Element which represents a list of arguments
	 * 
	 * @param argumentList
	 * @return parameters a list of parameters
	 */
	public HashSet<String> getPCCallsParameters(Element argumentList) {

		HashSet<String> parameters = new HashSet<String>();

		NodeList childList = argumentList.getChildNodes();
		for(int i=0; i<childList.getLength(); i++){
			Node argument = childList.item(i);
			if(argument.getNodeType() == Node.ELEMENT_NODE && argument.getNodeName().equalsIgnoreCase(ToolConstant.ARGUMENT)){
				Element argumentElement = (Element) argument;
				NodeList nameList = argumentElement.getElementsByTagName(ToolConstant.NAME);
				for(int j=0; j<nameList.getLength(); j++){
					Node name = nameList.item(j);//prendo i "name" dentro un argument
					if(name.getNodeType() == Node.ELEMENT_NODE && isPCMethod(name.getNodeValue())){
						parameters.add(name.getNodeValue());
					}
				}
			}
			
		}
		
		return parameters;
	}

	/**
	 * This method checks if the node calls a
	 * production class method
	 * 
	 * @param nodeName
	 * @return
	 */
	private boolean isPCMethod(String nodeValue) {
		
		boolean isPCmeth = false;
		HashSet<ToolMethodType> pcMethods = data.getProductionMethods();
		for(ToolMethodType tmt : pcMethods)
			if(tmt.getMethodName().equals(nodeValue))
				isPCmeth = true;
		
		return isPCmeth;
	}

	/**
	 * This method returns the parameter type for the Element
	 * 
	 * @param argument
	 * @return paramType
	 */
	public String getParameterType(Element argument) {

		/*
		 * Dentro argument c'è expr dentro expr ci puà essere: literal o name si
		 * gestisce solo il caso literal
		 */
		String paramType = "";
		NodeList argChildList = argument.getChildNodes();
		for (int i = 0; i < argChildList.getLength(); i++) {
			if (argChildList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element exprElement = (Element) argChildList.item(i);
				NodeList literalOrNameList = exprElement.getChildNodes();
				for (int j = 0; j < literalOrNameList.getLength(); j++) {
					if (literalOrNameList.item(j).getNodeType() == Node.ELEMENT_NODE) {
						Element litOrName = (Element) literalOrNameList.item(j);
						if (litOrName.getNodeName().equals(ToolConstant.LITERAL)) {
							// leggo l'attributo type
							paramType = litOrName.getAttribute(ToolConstant.TYPE);
						}
					}
				}
			}
		}

		return paramType;
	}

}
