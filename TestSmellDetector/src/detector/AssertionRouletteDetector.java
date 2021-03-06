package detector;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.tools.Tool;
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

import result.AssertionRouletteResult;
import util.AssertParameterChecker;
import util.ClassNameExtractor;
import util.MethodMatcher;
import util.PackageTool;
import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class AssertionRouletteDetector extends Thread{

	private ToolData data;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private MethodMatcher methodMatcher;
	private AssertParameterChecker assertChecker;
	private ArrayList<AssertionRouletteResult> rouletteResults;
	private int testMethodNumber;
	private int assertionRouletteAbs;
	private double assertionRoulettePerc;

	private static Logger log;

	/**
	 * Constructor for AssertionRouletteDetector
	 * 
	 * @param data
	 */
	public AssertionRouletteDetector(ToolData data){
		this.data = data;
		this.assertionRouletteAbs = data.getThresholdsContainer().getAssertionRouletteAbs();
		this.assertionRoulettePerc = data.getThresholdsContainer().getAssertionRoulettePerc();
		this.rouletteResults = new ArrayList<AssertionRouletteResult>();
		this.testChecker = new TestMethodChecker();
		this.methodMatcher = new MethodMatcher();
		this.assertChecker = new AssertParameterChecker();
		this.testMethodNumber = 0;
		log = LogManager.getLogger(AssertionRouletteDetector.class.getName());
	}
	
	/**
	 * This method returns results of the single 
	 * XML file assertion roulette analysis
	 * 
	 * @param xml
	 * @return
	 */
	public double analyze(File xml) {
		
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();
			
			//Leggo il package
			String classPackage = PackageTool.constructPackage(doc);

			// leggo la lista di nodi function
			NodeList functionList = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < functionList.getLength(); i++) {
				if (functionList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) functionList.item(i);
					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi assert e controllarle
					if (testChecker.isTestMethod(functionElement)) {
						// aggiungere check sugli assert
						readNoMessageAsserts(functionElement,result);
						testMethodNumber++;
					}
				}
			}
			String packFromPaht = ClassNameExtractor.extractClassNameFromPath(xml.getName());
			rouletteResults.add(new AssertionRouletteResult(packFromPaht.replace(ToolConstant.MINUS, ToolConstant.DOT),result));
			
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
	
	@Override
	public void run() {
		log.info("*** START ASSERTION ROULETTE ANALYSIS ***");
		for (File file : data.getTestClasses())
			this.analyze(file);
		computeResults();
		log.info("*** END ASSERTION ROULETTE ANALYSIS ***\n");
	}

	/**
	 * This method computes results for assertion
	 * roulette analysis
	 */
	private void computeResults() {
		
		int assertionRouletteNumber = 0;
		
		for(AssertionRouletteResult arr : rouletteResults){
			for(String testMethod : arr.getNoMessageAssertMap().keySet()){
				ArrayList<String> noMsgAsserts = arr.getNoMessageAssertMap().get(testMethod);
				for(String element : noMsgAsserts){
					//log.info(arr.getTestCasesFile()+"."+testMethod+" calls "+element+" without message parameter");
					log.info("Assertion Roulette found in "+arr.getTestCasesFile()+"."+testMethod);
					assertionRouletteNumber++;
				}	
			}	
		}
		
		double asrNum = assertionRouletteNumber;
		double totMeth = testMethodNumber;
		double perc = (asrNum/totMeth)*100;
		DecimalFormat df = new DecimalFormat("####0.00");
		if(perc >= assertionRoulettePerc){
			log.info("Assertion Roulette %: "+df.format(perc)+"%");
		}
		
	}

	/**
	 * This method finds assert methods that 
	 * don't have "message" parameter
	 * 
	 * @param functionElement which represents the test method
	 * @param result a map testMethodName --> asserts without "message" parameter
	 */
	private void readNoMessageAsserts(Element functionElement, HashMap<String, ArrayList<String>> result) {

		String methodName = TestParseTool.readMethodNameByFunction(functionElement);
		result.put(methodName, new ArrayList<String>());
		
		// devo scorrere i name della function
		int numberOfAssertNoMsg = 0;
		NodeList nameList = functionElement.getElementsByTagName(ToolConstant.NAME);
		for (int j = 0; j < nameList.getLength(); j++) {
			Element nameElement = (Element) nameList.item(j);
			String nameElementContent = nameElement.getTextContent();
			if (methodMatcher.isAssertMethod(nameElementContent)) {
				// se entro ho trovato un metodo assert
				// e quindi devo vedere se ho il parametro message o meno
				if (nameElement.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
					Element call = (Element) nameElement.getParentNode();
					if (!assertChecker.hasMessageParameter(call, nameElementContent)) {
						numberOfAssertNoMsg++;
						if(numberOfAssertNoMsg >= assertionRouletteAbs)
							result.get(methodName).add(nameElement.getTextContent());
					}
				}
			}
		}
	}
}
