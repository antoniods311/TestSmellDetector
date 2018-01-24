package detector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import util.TestMethodChecker;
import util.ToolConstant;

/**
 * 
 * @author antoniods311
 *
 */
public class AssertionRouletteDetector implements Detector {

	private File xml;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private static Logger log;

	public AssertionRouletteDetector(File xml) {
		this.xml = xml;
		log = LogManager.getLogger(AssertionRouletteDetector.class.getName());
	}

	@Override
	public double analyze() {
		
		log.info("*** START ASSERTION ROULETTE ANALYSIS ***");

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
					// Se entro ho trovato un metodo di test e devo cercare le
					// chiamate dei metodi assert e controllarle
					if (testChecker.isTestMethod(functionElement)){
						
						//aggiungere check sugli assert
						
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
		
		log.info("*** END ASSERTION ROULETTE ANALYSIS ***");
		return 0;
	}
	
	@Override
	public void run() {
		analyze();
	}

}
