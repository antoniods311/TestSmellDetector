package detector;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import dataflowanalysis.DataFlowMethodAnalyzer;
import util.ClassNameExtractor;
import util.MethodMatcher;
import util.PackageTool;
import util.PathTool;
import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;
import util.prodclass.ToolMethodType;
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
	private DataFlowMethodAnalyzer methodAnalyzer;
	private HashSet<ToolMethodType> customToString;
	private int sensitiveEqualityAbs;
	private int globalTestMethodNumber;
	private double sensitiveEqualityPerc;
	
	public SensitiveEqualityDetector(ToolData data){
		this.data = data;
		this.sensitiveEqualityAbs = data.getThresholdsContainer().getSensitiveEqualityAbs();
		this.sensitiveEqualityPerc = data.getThresholdsContainer().getSensitiveEqualityPerc();
		this.testChecker = new TestMethodChecker();
		this.methodMatcher = new MethodMatcher();
		this.customToString = new HashSet<ToolMethodType>();
		this.sensitiveEqualityResults = new HashMap<String,HashMap<String,Integer>>();
		this.globalTestMethodNumber = 0;
		log = LogManager.getLogger(EagerTestDetector.class.getName());
	}

	/**
	 * This method computes sensitive equality analysis
	 * for a single test case (XML file)
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
			
			//Leggo il package
			String classPackage = PackageTool.constructPackage(doc);
			
			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) list.item(i);

					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi toString
					if (testChecker.isTestMethod(functionElement)) {
						globalTestMethodNumber++;
						String methodName = TestParseTool.readMethodNameByFunction(functionElement);
						//int numberOfToString = checkToString(functionElement);
						int numberOfToString = getNumOfToStringAssertParams(methodName,classPackage);
						result.put(methodName, numberOfToString);
						
					}
				}
			}

			sensitiveEqualityResults.put(ClassNameExtractor.extractClassNameFromPath(xml.getName()), result);
			
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
	 * method uses toString() as assert method parameter
	 * 
	 * @param methodName
	 * @return numOfToString
	 */
	private int getNumOfToStringAssertParams(String methodName,String classPackage){
		
		int numOfToString = 0;
		//1. trovare il nodo corrispondente al metodo di test
		CGNode node;
		Iterator<CGNode> iter = data.getCallGraph().iterator();
		while (iter.hasNext()) {
			node = iter.next();
			IMethod iMethod = node.getMethod();
			MethodReference methodRef = iMethod.getReference();
			TypeReference typeRef = methodRef.getDeclaringClass();
			ClassLoaderReference classLoaderRef = typeRef.getClassLoader();
			String pack = PathTool.pathToPackage(typeRef.getName().getPackage().toString());

			if (classLoaderRef.getName().toString()
					.equalsIgnoreCase(ToolConstant.APPLLICATION_CLASS_LOADER)
					&& iMethod.getName().toString().equalsIgnoreCase(methodName)
					&& pack.equals(classPackage)) {
				methodAnalyzer = new DataFlowMethodAnalyzer(node);
				
				//2. customizzare ToolData
				customToString.add(new ToolMethodType(ToolConstant.OBJECT_CLASS, ToolConstant.TO_STRING));
				ToolData customData = new ToolData();
				customData.setCallGraph(data.getCallGraph());
				customData.setProductionClasses(data.getProductionClasses());
				customData.setTestClasses(data.getTestClasses());
				customData.setThresholdsContainer(data.getThresholdsContainer());
				customData.setProductionMethods(customToString);
				
				//3. fare l'analisi
				HashSet<String> toStringCalls = methodAnalyzer.getPCMethodsTestedByTestMethod(customData,
						methodName);
				
				for(String s : toStringCalls){
					if(s!=null && s.equals(ToolConstant.TO_STRING))
						numOfToString++;
				}	
			}
		}
		
		return numOfToString;
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
		
		int numOfSE = 0;
		
		for(String testCaseName : sensitiveEqualityResults.keySet()){
			HashMap<String,Integer> result = sensitiveEqualityResults.get(testCaseName);
			for(String testMethod : result.keySet()){
				int numToString = result.get(testMethod);
				if(numToString >= sensitiveEqualityAbs){
					log.info("Sensitive Equality found: "+testCaseName+"."+testMethod);
					//log.info(testMethod+" uses toString() "+numToString+" times");
					numOfSE++;
				}
			}
		}
		
		double nSE = numOfSE;
		double totTM = globalTestMethodNumber;
		double perc = (nSE/totTM)*100;
		DecimalFormat df = new DecimalFormat("####0.00");
		if(perc >= sensitiveEqualityPerc)
			log.info("Perc: Sensitive Equality %:  "+df.format(perc));
	
		
	}

}
