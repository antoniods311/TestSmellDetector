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

		if (methodMatcher.isFailMethod(name)) {
			// devo controllare se c'è almeno un parametro [di tipo string]
			NodeList childList = call.getChildNodes();
			for(int i=0; i<childList.getLength(); i++){
				if(childList.item(i).getNodeType() == Node.ELEMENT_NODE){
					Element currentElement = (Element) childList.item(i);
					if(currentElement.getNodeName().equals(ToolConstant.ARGUMENT_LIST)){
						if(paramAnalyzer.getParameterNumber(currentElement) > 0) {
							hasMsgParam = true;
							System.out.println("£££££ "+name+" ha numero param > 0");
						}
						
					}
				}
			}
		} else {
			if (methodMatcher.isAssertTrueMethod(name) || methodMatcher.isAssertFalseMethod(name)
					|| methodMatcher.isAssertSameMethod(name) || methodMatcher.isAssertNotSameMetho(name)) {
				// devo controllare se ci sono 3 parametri. Il primo deve essere
				// di tipo String

			} else {
				if (methodMatcher.isAssertNullMethod(name) || methodMatcher.isAssertNotNullMethod(name)) {
					// qui devo avere 2 parametri, il primo dei quali deve
					// essere di tipo String

				} else {
					if (methodMatcher.isAssertEqualsMethod(name)) {
						/*
						 * sto nel caso equals. Qui devo gestire il caso con 2,
						 * 3 o 4 parametri nel caso 2 parametri è facile capire
						 * che non ho il msg nel caso 4 parametri ho sicuramente
						 * msg nel caso 3 parametri bisogna ragionare sulla
						 * tolleranza. Se c'è ovviamente sono nella versione del
						 * metodo che prevede 4 parametri e quindi se ne trovo
						 * solo 3 significa che il msg non ci sta
						 */

					}

				}

			}

		}

		return hasMsgParam;
	}

}
