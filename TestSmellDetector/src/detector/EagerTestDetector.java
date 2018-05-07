package detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;
import util.tooldata.ToolData;

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
import result.ResultContainer;

/**
 * 
 * @author antoniods311
 *
 */
public class EagerTestDetector extends Thread {

	private ToolData data;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private static Logger log;
	private DataFlowMethodAnalyzer methodAnalyzer;
	private ArrayList<ResultContainer> eagerTestResults;
	private int threshold = 1;
	private int eagerTestAbs;
	private double eagerTestPerc;
	
	public EagerTestDetector(ToolData data){
		this.data = data;
		this.eagerTestAbs = data.getThresholdsContainer().getEagerTestAbs();
		this.eagerTestPerc = data.getThresholdsContainer().getEagerTestPerc();
		this.testChecker = new TestMethodChecker();
		this.eagerTestResults = new ArrayList<ResultContainer>(); 
		log = LogManager.getLogger(EagerTestDetector.class.getName());
	}

	public double analyze(File xml) {

		docbuilderFactory = DocumentBuilderFactory.newInstance();
		HashMap<String, HashSet<String>> testedMethods = new HashMap<String, HashSet<String>>();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			// leggo la lista di nodi function
			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) list.item(i);

					// Se entro ho trovato un metodo di test
					if (testChecker.isTestMethod(functionElement)){
						String methodName = TestParseTool.readMethodNameByFunction(functionElement);
						CGNode node;
						Iterator<CGNode> iter = data.getCallGraph().iterator();
						while (iter.hasNext()) {
							node = iter.next();
							IMethod iMethod = node.getMethod();
							MethodReference methodRef = iMethod.getReference();
							TypeReference typeRef = methodRef.getDeclaringClass();
							ClassLoaderReference classLoaderRef = typeRef.getClassLoader();

							if (classLoaderRef.getName().toString()
									.equalsIgnoreCase(ToolConstant.APPLLICATION_CLASS_LOADER)
									&& iMethod.getName().toString().equalsIgnoreCase(methodName)) {
								methodAnalyzer = new DataFlowMethodAnalyzer(node);
								
								HashSet<String> methodsTested = methodAnalyzer.getPCMethodsTestedByTestMethod(data,methodName);
								testedMethods.put(methodName, methodsTested); //tutti i metodi testati della PC nel metodo di test										
							}
						}
					}
				}
			}
			
			eagerTestResults.add(new ResultContainer(xml, testedMethods));

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
		
		return 0;
	}

	@Override
	public void run() {
		log.info("*** START EAGER TEST ANALYSIS ***");;
		for (File file : data.getTestClasses())
			this.analyze(file);
		computeResults();
		log.info("*** END EAGER TEST ANALYSIS ***\n");
	}

	/**
	 * This method computes results for Eager Test analysis
	 */
	private void computeResults() {
		
		for(ResultContainer eager : eagerTestResults){
			for(String testMtd : eager.getTestedMethods().keySet()){
				int numberOfTestedMethods = eager.getTestedMethods().get(testMtd).size();
				if(numberOfTestedMethods > threshold){
					log.info("Eager Test found! "+testMtd+" tests "+numberOfTestedMethods+" PC methods");
				}
			}
		}
		
	}
	
	/*
	 * metodo che conta il numero degli assert all'interno di un test deve
	 * riempire result!
	 */
//	private void checkAssertMethods(Element functionElement) {
//
//		int numOfAssert = 0;
//		String methodName = TestParseTool.readMethodNameByFunction(functionElement);
//
//		// calcolo il numero di result
//		NodeList nameMethodList = functionElement.getElementsByTagName(ToolConstant.NAME);
//		for (int j = 0; j < nameMethodList.getLength(); j++) {
//			if (methodMatcher.isAssertMethod(nameMethodList.item(j).getTextContent())) {
//				numOfAssert++;
//
//				/*
//				 * devo vedere i parametri dell'assert mettere un metodo in
//				 * SourceClassAnalyzer per farmeli restituire
//				 */
//				if (nameMethodList.item(j).getNodeType() == Node.ELEMENT_NODE) {
//					Element assertElement = (Element) nameMethodList.item(j);
//					Element callElement = (Element) assertElement.getParentNode();
//					analyzeAssertMethod(callElement);
//				}
//
//			}
//		}
//
//		numOfAssertResult.put(methodName, numOfAssert);
//	}

	/*
	 * verifica se l'assert ha o meno come parametro un metodo della classe
	 * sotto test
	 */
//	private void analyzeAssertMethod(Element callElement) {
//
//		NodeList childList = callElement.getChildNodes();
//
//		for (int i = 0; i < childList.getLength(); i++) {
//			if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
//				Element currentElement = (Element) childList.item(i);
//				if (currentElement.getNodeName().equals(ToolConstant.ARGUMENT_LIST)) {
//
//					// usare paramAnalyzer per farsi restituire i parametri
//					ArrayList<String> parameters = paramAnalyzer.getParameters(currentElement); //metodo ancora da implementare
//
//				}
//			}
//		}
//
//	}


}