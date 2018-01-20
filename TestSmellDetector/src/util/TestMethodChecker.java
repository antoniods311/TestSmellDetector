package util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestMethodChecker {
	
	/*
	 * metodo che controlla se un metodo del TC è un test o meno.
	 * Si verifica che ci sia l'annotazione @Test
	 */
	public boolean isTestMethod(Element element) {

		boolean isTest = false;
		int i = 0;
		NodeList annotationList = element.getElementsByTagName(ToolConstant.ANNOTATION);
		while(!isTest && i < annotationList.getLength()){
			Element annotationElement = (Element) annotationList.item(i);
			NodeList nameList = annotationElement.getElementsByTagName(ToolConstant.NAME);
			int j;
			for(j=0; j<nameList.getLength(); j++){
				Element nameElement = (Element) nameList.item(j);;
				if(nameElement.getTextContent().equalsIgnoreCase(ToolConstant.TEST_ANNOTATION))
					isTest = true;
				j++;
			}
			i++;
		}
		
		return isTest;
	}
}
