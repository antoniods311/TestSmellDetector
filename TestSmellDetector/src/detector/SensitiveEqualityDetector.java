package detector;

import java.io.File;
import java.io.IOException;
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
public class SensitiveEqualityDetector implements Detector {

	private File xml;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private HashMap<String, Integer> result;
	private TestMethodChecker testChecker;
	private MethodMatcher methodMatcher;
	private static Logger log;

	public SensitiveEqualityDetector(File xml) {
		this.xml = xml;
		testChecker = new TestMethodChecker();
		methodMatcher = new MethodMatcher();
		log = LogManager.getLogger(EagerTestDetector.class.getName());
	}

	@Override
	public double analyze() {

		log.info("<<< start sensitive Equality analysis >>>");
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		result = new HashMap<String, Integer>();

		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			// leggo la lista di nodi function
			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) list.item(i);

					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi assert
					if (testChecker.isTestMethod(functionElement)) {
						checkToString(functionElement);
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

		for(String s: result.keySet()){
			log.info("toString call number for "+s+": "+result.get(s));
		}
		
		log.info("<<< end Sensitive Equality analysis >>>");

		return 0;

	}

	/*
	 * Conta il numero di toString che ci sono nel singolo metodo aggiorna
	 * l'hashmap dei risultati.
	 */
	private void checkToString(Element functionElement) {

		int numOfToString = 0;
		String methodName = "";

		// leggo il nome del metodo
		NodeList childNodes = functionElement.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			if (childNodes.item(j).getNodeName() == ToolConstant.NAME) {
				methodName = childNodes.item(j).getTextContent();
			}
		}

		// calcolo il numero di result
		//devo scorrere direttamente i name
		NodeList nameList = functionElement.getElementsByTagName(ToolConstant.NAME);
		for(int i=0; i<nameList.getLength(); i++){
			Element nameElement = (Element) nameList.item(i);
			if(methodMatcher.isToStringMethod(nameElement.getTextContent()))
				numOfToString++;
		}
		
		result.put(methodName, numOfToString);
	}

	@Override
	public void run() {
		analyze();
	}

}
