package detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import util.MethodMatcher;
import util.ParameterAnalyzer;
import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;
import util.eagertest.SourceClassAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.wala.ipa.callgraph.CallGraph;

/**
 * 
 * @author antoniods311
 *
 */
public class EagerTestDetector extends Thread {

	private File xmlTest, xmlClass;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private HashMap<String, Integer> numOfAssertResult;
	private ArrayList<String> eagerTestResult;
	private TestMethodChecker testChecker;
	private MethodMatcher methodMatcher;
	private SourceClassAnalyzer scAnalyzer;
	private TreeSet<String> classMethods;
	private static Logger log;
	private ParameterAnalyzer paramAnalyzer;

	public EagerTestDetector(File xml, File xmlClass, CallGraph callGraph) {
		this.xmlTest = xml;
		this.xmlClass = xmlClass;
		testChecker = new TestMethodChecker();
		methodMatcher = new MethodMatcher();
		paramAnalyzer = new ParameterAnalyzer();
		log = LogManager.getLogger(EagerTestDetector.class.getName());
	}

	/*
	 * bisogna navigare ricorsivamente il doc xml in modo tale che per ogni
	 * function (annotata con @Test) che incontra vado a verificare il contenuto
	 * dell'elemento block. Nel block ci possono essere 2 situazioni: - trovo un
	 * assert e allora devo aggiungerlo alla lista di assert trovati; - trovo un
	 * altro blocco. Allora devo fare chiamata ricorsiva per visitare il blocco.
	 */

	/*
	 * altra soluzione (per gestire anche casi in cui c'è un if annidato o un
	 * while per esempio): - considero i singoli elementi function (annotati
	 * con @Test) - mi prendo la lista di NODI - scorro la lista prendendomi
	 * solo quelli che sono "ELEMENT_NODE" - tra questi conto gli assert
	 */

	/*
	 * in result si trovano le corrispondeze tra metodoDiTest e numero di assert
	 */
	public double analyze() {

		log.info("*** START EAGER TEST ANALYSIS ***");

		/*
		 * gestire caso di più metodi di test che possono presentare molti
		 * assert. Per esempio si potrebbe considerare una HashMap<String,int>
		 * dove ad ogni test si fa corrispondere un intero che rappresenta il
		 * numero di assert che sono stati trovati. fallo fare nel metodo
		 * getAssertsNumber
		 */

		eagerTestResult = new ArrayList<String>();
		numOfAssertResult = new HashMap<String, Integer>();
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xmlTest);
			doc.getDocumentElement().normalize();

			// leggo i metodi che appartengono alla classe sotto test
			scAnalyzer = new SourceClassAnalyzer(xmlClass);
			classMethods = scAnalyzer.getClassMethods();
			for (String s : classMethods) {
				log.info(s);
			}

			// leggo la lista di nodi function
			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) list.item(i);

					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi assert
					if (testChecker.isTestMethod(functionElement))
						checkAssertMethods(functionElement);
				}
			}

			for (String s : numOfAssertResult.keySet()) {
				log.info("assert number for  " + s + ": " + numOfAssertResult.get(s));
			}

		} catch (ParserConfigurationException e) {
			log.error(ToolConstant.PARSE_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (SAXException e) {
			log.error(ToolConstant.SAX_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(ToolConstant.IO_EXCEPTION_MSG);
			e.printStackTrace();
		}

		log.info("*** END EAGER TEST ANALYSIS ***\n");

		return 0;
	}

	/*
	 * metodo che conta il numero degli assert all'interno di un test deve
	 * riempire result!
	 */
	private void checkAssertMethods(Element functionElement) {

		int numOfAssert = 0;
		String methodName = TestParseTool.readMethodNameByFunction(functionElement);

		// calcolo il numero di result
		NodeList nameMethodList = functionElement.getElementsByTagName(ToolConstant.NAME);
		for (int j = 0; j < nameMethodList.getLength(); j++) {
			if (methodMatcher.isAssertMethod(nameMethodList.item(j).getTextContent())) {
				numOfAssert++;

				/*
				 * devo vedere i parametri dell'assert mettere un metodo in
				 * SourceClassAnalyzer per farmeli restituire
				 */
				if (nameMethodList.item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element assertElement = (Element) nameMethodList.item(j);
					Element callElement = (Element) assertElement.getParentNode();
					analyzeAssertMethod(callElement);
				}

			}
		}

		numOfAssertResult.put(methodName, numOfAssert);
	}

	/*
	 * verifica se l'assert ha o meno come parametro un metodo della classe
	 * sotto test
	 */
	private void analyzeAssertMethod(Element callElement) {

		NodeList childList = callElement.getChildNodes();

		for (int i = 0; i < childList.getLength(); i++) {
			if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element currentElement = (Element) childList.item(i);
				if (currentElement.getNodeName().equals(ToolConstant.ARGUMENT_LIST)) {

					// usare paramAnalyzer per farsi restituire i parametri
					TreeSet<String> parameters = paramAnalyzer.getParameters(currentElement);

				}
			}
		}

	}

	@Override
	public void run() {
		analyze();
	}

}