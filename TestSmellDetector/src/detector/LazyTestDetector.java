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

public class LazyTestDetector extends Thread {

	private CallGraph callGraph;
	private ArrayList<File> xmlList;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private static Logger log;

	/**
	 * Constructor method
	 * 
	 * @param xmlList
	 * @param graph
	 */
	public LazyTestDetector(ArrayList<File> xmlList, CallGraph graph) {
		this.xmlList = xmlList;
		this.callGraph = graph;
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

		log.info("*** START MYSTERY GUEST ANALYSIS ***");

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

		log.info("*** END MYSTERY GUEST ANALYSIS ***\n");

		return 0;
	}

	@Override
	public void run() {
		for (File file : xmlList) {
			analyze(file);
		}
	}
	
}
