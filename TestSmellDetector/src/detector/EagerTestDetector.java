package detector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import util.MethodMatcher;
import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author antoniods311
 *
 */
public class EagerTestDetector implements Detector {

	private File xml;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private HashMap<String, Integer> result;
	private TestMethodChecker testChecker;
	private MethodMatcher methodMatcher;
	private static Logger log;

	public EagerTestDetector(File xml) {
		this.xml = xml;
		testChecker = new TestMethodChecker();
		methodMatcher = new MethodMatcher();
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
	@Override
	public double analyze() {

		log.info("*** START EAGER TEST ANALYSIS ***");

		/*
		 * gestire caso di più metodi di test che possono presentare molti
		 * assert. Per esempio si potrebbe considerare una HashMap<String,int>
		 * dove ad ogni test si fa corrispondere un intero che rappresenta il
		 * numero di assert che sono stati trovati. fallo fare nel metodo
		 * getAssertsNumber
		 */

		result = new HashMap<String, Integer>();
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

					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi assert
					if (testChecker.isTestMethod(functionElement))
						calculateAssertsNumber(functionElement);
				}
			}

			for (String s : result.keySet()) {
				log.info("assert number for  " + s + ": " + result.get(s));
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

		log.info("*** END EAGER TEST ANALYSIS ***\n");

		return 0;
	}

	/*
	 * metodo che conta il numero degli assert all'interno di un test deve
	 * riempire result!
	 */
	private void calculateAssertsNumber(Element functionElement) {

		int numOfAssert = 0;
		String methodName = TestParseTool.readMethodNameByFunction(functionElement);

		// calcolo il numero di result
		NodeList callList = functionElement.getElementsByTagName(ToolConstant.CALL);
		for (int i = 0; i < callList.getLength(); i++) {
			Element call = (Element) callList.item(i);
			NodeList nameMethodList = call.getElementsByTagName(ToolConstant.NAME);
			int j;
			for (j = 0; j < nameMethodList.getLength(); j++) {
				if (methodMatcher.isAssertMethod(nameMethodList.item(j).getTextContent()))
					numOfAssert++;
			}
		}
		result.put(methodName, numOfAssert);
	}

	@Override
	public void run() {
		analyze();
	}

}