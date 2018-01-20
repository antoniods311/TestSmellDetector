package util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestParseTool {

	/*
	 * metodo che restituisce il nome di un metodo in un TC a partire
	 * dall'elemento function
	 */
	public static String readMethodNameByFunction(Element functionElement) {

		String methodName = "";

		// leggo il nome del metodo
		NodeList childNodes = functionElement.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++)
			if (childNodes.item(j).getNodeName() == ToolConstant.NAME) {
				methodName = childNodes.item(j).getTextContent();
			}

		return methodName;
	}

}
