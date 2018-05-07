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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import dataflowanalysis.DataFlowMethodAnalyzer;
import result.ResultContainer;

import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;
import util.prodclass.ToolMethodType;
import util.tooldata.ToolData;

public class LazyTestDetector extends Thread {

	private ToolData data;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private DataFlowMethodAnalyzer methodAnalyzer;
	// private HashMap<String,HashSet<String>> callPaths; //metodi chiamati dal// metodo di test
	private ArrayList<ResultContainer> lazyTestResults;
	private static Logger log;
	private int lazyTestAbs;
	private double lazyTestPerc;

	/**
	 * @param data
	 */
	public LazyTestDetector(ToolData data) {
		this.data = data;
		this.lazyTestAbs = data.getThresholdsContainer().getLazyTestAbs();
		this.lazyTestPerc = data.getThresholdsContainer().getLazyTestPerc();
		this.testChecker = new TestMethodChecker();
		this.methodAnalyzer = null;
		// this.callPaths = new HashMap<String,HashSet<String>>();
		this.lazyTestResults = new ArrayList<ResultContainer>();
		log = LogManager.getLogger(LazyTestDetector.class.getName());
	}

	/**
	 * This method returns results of the single XML file analysis
	 * 
	 * @param xml
	 * @return
	 */
	public double analyze(File xml) {

		docbuilderFactory = DocumentBuilderFactory.newInstance();
		HashMap<String, HashSet<String>> testedMethods = new HashMap<String, HashSet<String>>();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			// leggo la lista di nodi function
			NodeList functionList = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < functionList.getLength(); i++) {
				if (functionList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) functionList.item(i);
					// Se entro ho trovato un metodo di test
					if (testChecker.isTestMethod(functionElement)) {
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
								// HashSet<String> methodsCalled = methodAnalyzer.calculatePCMethodsCall(data,methodName);
								// callPaths.put(methodName, methodsCalled);// tutti i metodi della PC chiamati nel metodo di test
								HashSet<String> methodsTested = methodAnalyzer.getPCMethodsTestedByTestMethod(data,methodName);
								testedMethods.put(methodName, methodsTested); //tutti i metodi testati della PC nel metodo di test									
																		
							}
						}
					}
				}
			}
			
			lazyTestResults.add(new ResultContainer(xml, testedMethods));
			
//			for (String key : testedMethods.keySet()) {
//				String s = "TM: " + key + " -> "; 
//				for (String meth : testedMethods.get(key)) {
//					s = s + meth + " ";
//				}
//				log.info(s);
//			}

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
		log.info("*** START LAZY ANALYSIS ***");
		for (File file : data.getTestClasses())
			this.analyze(file);
		computeResults();
		log.info("*** END LAZY TEST ANALYSIS ***\n");
	}

	/**
	 * This method computes results for Lazy Test analysis
	 */
	private void computeResults() {

		boolean isLazyTest = false;
		HashMap<String, Integer> tot = new HashMap<String, Integer>();
		for (ToolMethodType tmt : data.getProductionMethods())
			tot.put(tmt.getMethodName(), 0);
		/*
		 * scorro tutti i metodi della pc e poi vado ad aumentare il numero di
		 * chiamate per questo
		 */
		for (ResultContainer lazy : lazyTestResults) {
			for (String testMtd : lazy.getTestedMethods().keySet()) {
				for (String tm : lazy.getTestedMethods().get(testMtd)) {
					if (tot.containsKey(tm)) {
						int count = tot.get(tm);
						count++;
						tot.remove(tm);
						tot.put(tm, count);
					}
				}
			}
		}
		for (String key : tot.keySet()) {
			if (tot.get(key) > 0){
				isLazyTest = true;
				log.info("PC method " + key + " is tested " + tot.get(key)+" times");
			}
				
		}

		if (isLazyTest)
			log.info("Lazy Test found");

	}
	
//	private HashSet<String> assertsAnalysis(Element functionElement, String testMethod) {
//
//		/*
//		 * scorrere tutti gli assert di test e per ognuno di essi richiamare i
//		 * metodi di analisi della classe ParameterAnalyzer per analizzarne i
//		 * parametri. Se l'assert include tra i parametri un metodo della PC
//		 * aggiungere ad un set comune (a tutti gli assert) questo metodo. Dopo
//		 * di che aggiungere la coppia [methodName-set] a testedMethods
//		 */
//		HashSet<String> allAssertSet = new HashSet<String>();
//		NodeList nameMethodList = functionElement.getElementsByTagName(ToolConstant.NAME);
//		for (int j = 0; j < nameMethodList.getLength(); j++) {
//			if (methodMatcher.isAssertMethod(nameMethodList.item(j).getTextContent())) {
//				if (nameMethodList.item(j).getNodeType() == Node.ELEMENT_NODE) {
//					Element assertElement = (Element) nameMethodList.item(j); // questo
//																				// è
//																				// un
//																				// metodo
//																				// di
//																				// assert
//					NodeList childList = assertElement.getChildNodes();
//					for (int k = 0; k < childList.getLength(); k++) {
//						Node temp = childList.item(k);
//						if (temp.getNodeType() == Node.ELEMENT_NODE
//								&& temp.getNodeName().equals(ToolConstant.ARGUMENT_LIST)) {
//							Element argumentList = (Element) childList.item(k);
//
//							// chiamare metodo di ParameterAnalyzer per
//							// analizzare questo element argument_list
//							ParameterAnalyzer paramAnalyzer = new ParameterAnalyzer(data);
//							HashSet<String> singleAssertSet = paramAnalyzer.getPCCallsParameters(argumentList);
//							if (!singleAssertSet.isEmpty()) {
//								// se non è vuoto aggiungo gli elementi di
//								// questo set ad un set comune a tutti gli
//								// assert dello stesso test method
//								for (String s : singleAssertSet)
//									allAssertSet.add(s);
//							}
//
//						}
//
//					}
//
//				}
//
//			}
//
//		}
//		return allAssertSet;
//	}
	

}
