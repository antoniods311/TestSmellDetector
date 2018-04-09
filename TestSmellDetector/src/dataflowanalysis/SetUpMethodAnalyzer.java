package dataflowanalysis;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.SetUpMethodChecker;
import util.ToolConstant;

public class SetUpMethodAnalyzer {

	private File xml;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;

	public SetUpMethodAnalyzer(File xml) {
		this.xml = xml;
	}

	/**
	 * 
	 * @return a set of created object names
	 */
	public HashSet<String> getCreatedSet() {

		HashSet<String> createdSet = new HashSet<String>();
		SetUpMethodChecker setUpChecker = new SetUpMethodChecker();

		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			NodeList functionList = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < functionList.getLength(); i++) {
				if (functionList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) functionList.item(i);
					//ho trovato un setup se entro in questo if
					if (setUpChecker.isSetUpMethod(functionElement)) { 
						// analizzare il setUp individuando i new object
						NodeList childList = functionElement.getChildNodes();
						for (int j = 0; j < childList.getLength(); j++) {
							if (childList.item(j).getNodeType() == Node.ELEMENT_NODE) {
								Element blockElement = (Element) childList.item(j);
								if (blockElement != null && blockElement.getNodeName().equalsIgnoreCase(ToolConstant.BLOCK)) {
									NodeList blockChildList = blockElement.getElementsByTagName(ToolConstant.OPERATOR);
									for (int k = 0; k < blockChildList.getLength(); k++) {
										if(blockChildList.item(k).getTextContent().equalsIgnoreCase(ToolConstant.NEW_OPERATOR)){
											//trovato un new --> devo trovare la variabile
											String varName = findVariableName(blockChildList.item(k));
											createdSet.add(varName);
										}
									}
								}

							}
						}

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
		return createdSet;
	}

	/**
	 * 
	 * @param item
	 * @return variable which contains the "new" object
	 */
	private String findVariableName(Node item) {
		
		String varName = null;
		
		
		
		return varName;
	}

}
