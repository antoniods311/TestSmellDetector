package util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AssertParameterChecker {

	private MethodMatcher methodMatcher;
	private ParameterAnalyzer paramAnalyzer;

	public AssertParameterChecker() {
		methodMatcher = new MethodMatcher();
		paramAnalyzer = new ParameterAnalyzer();
	}

	public boolean hasMessageParameter(Element call, String name) {

		boolean hasMsgParam = false;
		NodeList childList = call.getChildNodes();

		if (methodMatcher.isFailMethod(name)) {
			// devo controllare se c'è almeno un parametro [di tipo string]
			for (int i = 0; i < childList.getLength(); i++) {
				if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element currentElement = (Element) childList.item(i);
					if (currentElement.getNodeName().equals(ToolConstant.ARGUMENT_LIST)) {
						if (paramAnalyzer.getParameterNumber(currentElement) > 0)
							hasMsgParam = true;
					}
				}
			}
		} else {
			if (methodMatcher.isAssertSameMethod(name) || methodMatcher.isAssertNotSameMetho(name)) {
				// devo controllare se ci sono 3 parametri. Il primo deve essere
				// di tipo String
				for (int i = 0; i < childList.getLength(); i++) {
					if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element currentElement = (Element) childList.item(i);
						if (currentElement.getNodeName().equals(ToolConstant.ARGUMENT_LIST)) {
							if (paramAnalyzer.getParameterNumber(currentElement) > 2)
								hasMsgParam = true;
						}
					}
				}

			} else {
				if (methodMatcher.isAssertNullMethod(name) || methodMatcher.isAssertNotNullMethod(name)
						|| methodMatcher.isAssertTrueMethod(name) || methodMatcher.isAssertFalseMethod(name)) {
					// qui devo avere 2 parametri, il primo dei quali deve
					// essere di tipo String
					for (int i = 0; i < childList.getLength(); i++) {
						if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element currentElement = (Element) childList.item(i);
							if (currentElement.getNodeName().equals(ToolConstant.ARGUMENT_LIST)) {
								if (paramAnalyzer.getParameterNumber(currentElement) > 1)
									hasMsgParam = true;
							}
						}
					}

				} else {
					if (methodMatcher.isAssertEqualsMethod(name)) {
						/*
						 * sto nel caso equals. Qui devo gestire il caso con 2,
						 * 3 o 4 parametri. Nel caso 2 parametri è facile capire
						 * che non ho il msg; nel caso 4 parametri ho
						 * sicuramente msg; nel caso 3 parametri bisogna
						 * ragionare sulla tolleranza. Se c'è ovviamente sono
						 * nella versione del metodo che prevede 4 parametri e
						 * quindi se ne trovo solo 3 significa che il msg non ci
						 * sta
						 */
						for (int i = 0; i < childList.getLength(); i++) {
							if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
								Element argListElement = (Element) childList.item(i);
								if (argListElement.getNodeName().equals(ToolConstant.ARGUMENT_LIST)) {
									int parameterCount = paramAnalyzer.getParameterNumber(argListElement);

									switch (parameterCount) {
									case 2:
										hasMsgParam = false;
										break;
									case 3:
										hasMsgParam = checkThreeParam(argListElement);
										break;
									case 4:
										hasMsgParam = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return hasMsgParam;
	}

	private boolean checkThreeParam(Element argumentList) {

		boolean hasMsg = false;

		NodeList args = argumentList.getElementsByTagName(ToolConstant.ARGUMENT);
		if(args.getLength() > 0){
			//check first parameter
			if(args.item(0).getNodeType() == Node.ELEMENT_NODE){
				Element argument = (Element) args.item(0);
				if(paramAnalyzer.getParameterType(argument).equals(ToolConstant.STRING_TYPE_LOWER))
					hasMsg = true;
			}
		}
				
		return hasMsg;

	}

}
