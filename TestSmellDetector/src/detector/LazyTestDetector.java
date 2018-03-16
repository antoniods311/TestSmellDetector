package detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

import com.ibm.wala.ipa.callgraph.CallGraph;

import util.TestMethodChecker;
import util.ToolConstant;
import util.tooldata.ToolData;

public class LazyTestDetector extends Thread {

	private CallGraph callGraph;
	private ArrayList<File> xmlTestCasesList;
	private ArrayList<File> xmlProdClassesList;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private static Logger log;
	
	
	/**
	 * @param data
	 */
	public LazyTestDetector(ToolData data){
		this.xmlTestCasesList = data.getTestClasses();
		this.xmlProdClassesList = data.getProductionClasses();
		this.callGraph = data.getCallGraph();
		this.testChecker = new TestMethodChecker();
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
						
						//far partire l'analisi sul metodo
						
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

		return 0;
	}

	@Override
	public void run() {
		log.info("*** START LAZY ANALYSIS ***");
		
		for (File file : xmlTestCasesList)
			analyze(file);
		
		log.info("*** END LAZY TEST ANALYSIS ***\n");
	}
	
}
