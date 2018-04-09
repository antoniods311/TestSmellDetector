package util;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SetUpMethodChecker {

	public boolean isSetUpMethod(Element element) {

		boolean isSetUp = false;
		int i = 0;
		NodeList annotationList = element.getElementsByTagName(ToolConstant.ANNOTATION);
		while(!isSetUp && i < annotationList.getLength()){
			Element annotationElement = (Element) annotationList.item(i);
			NodeList nameList = annotationElement.getElementsByTagName(ToolConstant.NAME);
			int j;
			for(j=0; j<nameList.getLength(); j++){
				Element nameElement = (Element) nameList.item(j);
				String s = nameElement.getTextContent();
				if(s.equalsIgnoreCase(ToolConstant.BEFORE_ANNOTATION)) //|| s.equalsIgnoreCase(ToolConstant.BEFORE_CLASS_ANNOTATION)
					isSetUp = true;
				j++;
			}
			i++;
		}
		
		return isSetUp;
	}
	
}
