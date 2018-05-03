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
import util.TestParseTool;
import util.ToolConstant;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class SensitiveEqualityDetector extends Thread {

	private ToolData data;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private HashMap<String, HashMap<String,Integer>> sensitiveEqualityResults;
	private TestMethodChecker testChecker;
	private MethodMatcher methodMatcher;
	private static Logger log;
	private int threshold = 1;
	
	public SensitiveEqualityDetector(ToolData data){
		this.data = data;
		this.testChecker = new TestMethodChecker();
		this.methodMatcher = new MethodMatcher();
		this.sensitiveEqualityResults = new HashMap<String,HashMap<String,Integer>>();
		log = LogManager.getLogger(EagerTestDetector.class.getName());
	}

	/**
	 * This method computes sensitive equality analysis
	 * for a single test case (xml file)
	 * 
	 * @param xml
	 * @return
	 */
	public double analyze(File xml) {

		
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		HashMap<String, Integer> result = new HashMap<String, Integer>();

		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) list.item(i);

					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi toString
					if (testChecker.isTestMethod(functionElement)) {
						String methodName = TestParseTool.readMethodNameByFunction(functionElement);
						int numberOfToString = checkToString(functionElement);
						result.put(methodName, numberOfToString);
						
					}
				}
			}

			sensitiveEqualityResults.put(xml.getName(), result);
			
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
		
		return 0;
	}

	/**
	 * This method calculates how many times a test
	 * method calls toString()
	 * 
	 * @param functionElement
	 * @return
	 */
	private int checkToString(Element functionElement) {

		int numOfToString = 0;
		NodeList nameList = functionElement.getElementsByTagName(ToolConstant.NAME);
		for(int i=0; i<nameList.getLength(); i++){
			Element nameElement = (Element) nameList.item(i);
			if(methodMatcher.isToStringMethod(nameElement.getTextContent()))
				numOfToString++;
		}
		
		return numOfToString;
	}

	@Override
	public void run() {
		log.info("*** START SENSITIVE EQUALITY ANALYSIS ***");
		for (File file : data.getTestClasses())
			this.analyze(file);
		computeResults();
		log.info("*** END SENSITIVE EQUALITY ANALYSIS ***\n");
	}

	/**
	 * This method checks how many times 
	 * a test method calls toString()
	 */
	private void computeResults() {
		
		for(String testCaseName : sensitiveEqualityResults.keySet()){
			HashMap<String,Integer> result = sensitiveEqualityResults.get(testCaseName);
			for(String testMethod : result.keySet()){
				int numToString = result.get(testMethod);
				if(numToString >= threshold)
					log.info(testMethod+" calls toString() "+numToString+" times");
			}
			
		}
		
		
	}

}
