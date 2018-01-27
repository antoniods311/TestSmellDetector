package util;

import javax.xml.soap.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParameterAnalyzer {

	public ParameterAnalyzer() {

	}

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
