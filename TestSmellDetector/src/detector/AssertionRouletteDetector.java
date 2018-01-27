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

import util.AssertParameterChecker;
import util.MethodMatcher;
import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;

/**
 * 
 * @author antoniods311
 *
 */
public class AssertionRouletteDetector implements Detector {

	private File xml;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private MethodMatcher methodMatcher;
	private AssertParameterChecker assertChecker;
	private HashMap<String, ArrayList<String>> result;
	private static Logger log;

	public AssertionRouletteDetector(File xml) {
		this.xml = xml;
		testChecker = new TestMethodChecker();
		methodMatcher = new MethodMatcher();
		assertChecker = new AssertParameterChecker();
		log = LogManager.getLogger(AssertionRouletteDetector.class.getName());
	}

	@Override
	public double analyze() {

		log.info("*** START ASSERTION ROULETTE ANALYSIS ***");
		result = new HashMap<String, ArrayList<String>>();
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
					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi assert e controllarle
					if (testChecker.isTestMethod(functionElement)) {
						// aggiungere check sugli assert
						readNoMessageAsserts(functionElement);
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

		for(String m : result.keySet()){
			ArrayList<String> array = result.get(m);
			for(String s : array){
				log.info("test method "+m+" calls "+s+" without message parameter");
			}
		}
		
		log.info("*** END ASSERTION ROULETTE ANALYSIS ***\n");
		return 0;
	}

	/*
	 * metodo che individua gli assert per i quali non è specificato il
	 * parametro message
	 */
	private void readNoMessageAsserts(Element functionElement) {

		String methodName = TestParseTool.readMethodNameByFunction(functionElement);
		result.put(methodName, new ArrayList<String>());

		// devo scorrere i name della function
		NodeList nameList = functionElement.getElementsByTagName(ToolConstant.NAME);
		for (int j = 0; j < nameList.getLength(); j++) {
			Element nameElement = (Element) nameList.item(j);
			String nameElementContent = nameElement.getTextContent();
			if (methodMatcher.isAssertMethod(nameElementContent)) {
				// se entro ho trovato un metodo assert
				// e quindi devo vedere se ho il parametro message o meno
				if (nameElement.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
					Element call = (Element) nameElement.getParentNode();
					if (!assertChecker.hasMessageParameter(call, nameElementContent)) 
						result.get(methodName).add(nameElement.getTextContent());
				}
			}
		}
	}

	@Override
	public void run() {
		analyze();
	}

}
