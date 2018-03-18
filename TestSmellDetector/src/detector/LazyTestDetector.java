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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import dataflowanalysis.DataFlowMethodAnalyzer;
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
	private HashMap<String,HashSet<String>> callPaths;
	private static Logger log;

	/**
	 * @param data
	 */
	public LazyTestDetector(ToolData data) {
		this.data = data;
		this.testChecker = new TestMethodChecker();
		this.methodAnalyzer = null;
		this.callPaths = new HashMap<String,HashSet<String>>();
		log = LogManager.getLogger(LazyTestDetector.class.getName());
	}

	/**
	 * This method returns results of the single xml file analysis
	 * 
	 * @param xml
	 * @return
	 */
	public double analyze(File xml) {

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
								methodAnalyzer.calculatePCMethodsCall(data,methodName);
								callPaths.put(methodName, methodAnalyzer.getResults());
								
								
							}
						}
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

		
		for (String key : callPaths.keySet()) {
			System.out.print("testMethod: "+key+" -> ");
			for(String meth : callPaths.get(key)){
				System.out.print(meth+" ");
			}
			System.out.println();
		}

		return 0;
	}

	@Override
	public void run() {
		log.info("*** START LAZY ANALYSIS ***");

		for (File file : data.getTestClasses())
			this.analyze(file);

		log.info("*** END LAZY TEST ANALYSIS ***\n");
	}

}
