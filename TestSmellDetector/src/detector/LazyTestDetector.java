package detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import com.ibm.wala.ipa.callgraph.CallGraph;

import util.ToolConstant;

public class LazyTestDetector extends Thread {

	private CallGraph graph;
	private ArrayList<File> xmlList;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private static Logger log;
	
	/**
	 * Constructor method
	 * @param xmlList
	 * @param graph
	 */
	public LazyTestDetector(ArrayList<File> xmlList, CallGraph graph) {
		this.xmlList = xmlList;
		this.graph = graph;
		log = LogManager.getLogger(LazyTestDetector.class.getName());
	}
	
	/**
	 * This method returns results of the single xml file analysis
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
		for(File file : xmlList){
			analyze(file);
		}
		
	}
}
