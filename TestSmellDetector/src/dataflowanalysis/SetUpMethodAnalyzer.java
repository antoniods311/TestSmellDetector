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
	
	public SetUpMethodAnalyzer(File xml){
		this.xml = xml;
	}
	
	/**
	 * 
	 * @return a set of created object names
	 */
	public HashSet<String> getCreatedSet(){
		
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
					if(setUpChecker.isSetUpMethod(functionElement)){ //ho trovato un setUp
						//analizzare il setUp andando a vedere quali sono i new object
						
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
	
}
