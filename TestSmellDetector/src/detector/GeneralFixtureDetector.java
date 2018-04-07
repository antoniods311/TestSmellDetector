package detector;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import util.ToolConstant;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class GeneralFixtureDetector extends Thread {
	
	private ToolData data;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private static Logger log;
	
	/**
	 * Constructor for GeneralFixtureDetector object
	 * 
	 * @param data
	 */
	public GeneralFixtureDetector(ToolData data){
		this.data = data;
		log = LogManager.getLogger(GeneralFixtureDetector.class.getName());
	}

	
	public double analyze(File xml) {
		
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();
			
			/*
			 * Quando analizzo il primo metodo di test della classe devo fare
			 * diverse cose:
			 * 1. chiamare ClassFieldReader per leggere i fields della classe
			 * 2. analizzare tutti i metodoi di setUp per creare il set "createdSet"
			 * 3. creare un sottoinsieme di elementi comuni a 1 e 2
			 * 
			 * Questi punti li devo fare solo per il primo metodo di test dal momento
			 * che fatti una volta vanno bene per tutti i metodi di test.
			 */
			
			
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
	public void run(){
		log.info("*** START GENERAL FIXTURE ANALYSIS ***");
		for (File file : data.getTestClasses())
			this.analyze(file);
//		computeResults();
		log.info("*** END GENERAL FIXTURE ANALYSIS ***\n");
	}

}
