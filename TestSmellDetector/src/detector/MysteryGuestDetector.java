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

import util.MethodMatcher;
import util.TestMethodChecker;
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
	private HashMap<String,ArrayList<String>> typeResult;
	private HashMap<String,ArrayList<String>> functionResult;
	private TestMethodChecker testChecker;
	private MethodMatcher methodMatcher;
	private static Logger log;

	public MysteryGuestDetector(File xml) {
		this.xml = xml;
		testChecker = new TestMethodChecker();
		methodMatcher = new MethodMatcher();
		log = LogManager.getLogger(MysteryGuestDetector.class.getName());
	}

	/*
	 * Per individuare un MysteryGuest: 1. Cercare i "tipi" specifici coinvolti
	 * nell'uso dei file 2. Cercare i metodi tipicamente utilizzati nella
	 * gestione dei file.
	 */
	@Override
	public double analyze() {

		log.info(" <<< start Mystery Guest analysis >>>");

		typeResult = new HashMap<String,ArrayList<String>>();
		functionResult = new HashMap<String,ArrayList<String>>();
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			// leggo la lista di nodi function
			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) list.item(i);
					/*
					 * In questo caso non serve controllare se sono in un metodo
					 * di test o meno perché l'uso dei file va individuato anche
					 * nei metodi accessori e non solo in quelli di test.
					 */
					calculateFileApiTypes(functionElement);
					calculateFileApiFunctions(functionElement);
				}
			}

			for (String s : typeResult.keySet()) {
				log.info("assert number for  " + s + ": " + typeResult.get(s));
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

		log.info("<<< end Mystery Guest analysis >>>");

		return 0;
	}
	
	
	/*
	 * metodo che legge le chiamate a metodi di API per la gestione dei file
	 */
	private void calculateFileApiFunctions(Element functionElement) {
		
		
	}

	/*
	 * metodo che legge i tipi definite nelle API di gestione file 
	 */
	private void calculateFileApiTypes(Element functionElement) {
		
		
	}

	@Override
	public void run() {
		analyze();
	}
}
