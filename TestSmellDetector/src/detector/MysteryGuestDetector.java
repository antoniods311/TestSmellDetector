package detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.FileApiChecker;
import util.MethodMatcher;
import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;

/**
 * 
 * @author antoniods311
 *
 */
public class MysteryGuestDetector implements Detector {

	private File xml;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private HashMap<String, ArrayList<String>> typeResult;
	private HashMap<String, ArrayList<String>> functionResult;
	private FileApiChecker fileApiChecker;
	private static Logger log;

	public MysteryGuestDetector(File xml) {
		this.xml = xml;
		fileApiChecker = new FileApiChecker();
		log = LogManager.getLogger(MysteryGuestDetector.class.getName());
	}

	/*
	 * Per individuare un MysteryGuest: 1. Cercare i "tipi" specifici coinvolti
	 * nell'uso dei file 2. Cercare i metodi tipicamente utilizzati nella
	 * gestione dei file.
	 */
	@Override
	public double analyze() {

		log.info("*** START MYSTERY GUEST ANALYSIS ***");

		typeResult = new HashMap<String, ArrayList<String>>();
		functionResult = new HashMap<String, ArrayList<String>>();
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			// leggo la lista di nodi function
			NodeList functionList = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < functionList.getLength(); i++) {
				if (functionList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) functionList.item(i);
					/*
					 * In questo caso non serve controllare se sono in un metodo
					 * di test o meno perché l'uso dei file va individuato anche
					 * nei metodi accessori e non solo in quelli di test.
					 */
					calculateFileApiTypes(functionElement);
					calculateFileApiFunctions(functionElement);
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

		for (String s : typeResult.keySet()) {
			ArrayList<String> list = typeResult.get(s);
			for (String t : list) {
				log.info("file API types for method " + s + ": " + t);
			}
		}
		
		for(String s: functionResult.keySet()){
			ArrayList<String> list = functionResult.get(s);
			for(String t: list){
				log.info("file API called method for method " + s + ": " + t);
			}
		}
		
		log.info("*** END MYSTERY GUEST ANALYSIS ***\n");

		return 0;
	}

	/*
	 * metodo che legge le chiamate a metodi di API per la gestione dei file
	 */
	private void calculateFileApiFunctions(Element functionElement) {

		String methodName = TestParseTool.readMethodNameByFunction(functionElement);
		functionResult.put(methodName, new ArrayList<String>());
		NodeList callList = functionElement.getElementsByTagName(ToolConstant.CALL);
		for (int i = 0; i < callList.getLength(); i++) {
			Element call = (Element) callList.item(i);
			NodeList nameMethodList = call.getElementsByTagName(ToolConstant.NAME);
			for (int j = 0; j < nameMethodList.getLength(); j++) {
				Element callNode = (Element) nameMethodList.item(j);
				if (fileApiChecker.isFileApiFunction(callNode)) {
					// per il metodo corrente aggiungo le chiamate alla API di
					// gestione dei file
					String calledMethod = callNode.getTextContent();
					functionResult.get(methodName).add(calledMethod);
				}

			}
		}
	}

	/*
	 * metodo che legge i tipi definite nelle API di gestione file
	 */
	private void calculateFileApiTypes(Element functionElement) {

		String methodName = TestParseTool.readMethodNameByFunction(functionElement);
		typeResult.put(methodName, new ArrayList<String>());
		NodeList typeNodeList = functionElement.getElementsByTagName(ToolConstant.TYPE);

		for (int i = 0; i < typeNodeList.getLength(); i++) {
			Element typeNode = (Element) typeNodeList.item(i);
			if (fileApiChecker.isFileApiType(typeNode)) {
				// per il metodo corrente vengono aggiunti i tipi.
				String type = typeNode.getTextContent();
				typeResult.get(methodName).add(type);
			}
		}

	}

	@Override
	public void run() {
		analyze();
	}
}
