package util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestMethodChecker {
	
	/**
	 * This method returns true if the parameter represents a test method
	 * 
	 * @param element
	 * @return isTest
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
	
	
	/**
	 * Calculate test method number in XML 
	 * representation of class file
	 * 
	 * @param xml
	 * @return
	 */
	public int getTestMethodNumber(File xml){
		
		int testMethodNumber = 0;
		DocumentBuilderFactory docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = docbuilderFactory.newDocumentBuilder();
			Document doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			// leggo la lista di nodi function
			NodeList functionList = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < functionList.getLength(); i++) {
				if (functionList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) functionList.item(i);
					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi assert e controllarle
					if (this.isTestMethod(functionElement)) {
						testMethodNumber++;
					}
				}
			}
		} catch (ParserConfigurationException e) {
			System.out.println(ToolConstant.PARSE_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println(ToolConstant.SAX_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(ToolConstant.IO_EXCEPTION_MSG);
			e.printStackTrace();
		}
		
		return testMethodNumber;
	}
	
	
}
