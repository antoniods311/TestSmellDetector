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

import result.MysteryGuestResult;
import util.FileApiChecker;
import util.TestParseTool;
import util.ToolConstant;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class MysteryGuestDetector extends Thread {

	private ToolData data;
	private ArrayList<MysteryGuestResult> results;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private FileApiChecker fileApiChecker;
	private static Logger log;

	public MysteryGuestDetector(ToolData data) {
		this.data = data;
		this.results = new ArrayList<MysteryGuestResult>();
		this.fileApiChecker = new FileApiChecker();
		log = LogManager.getLogger(MysteryGuestDetector.class.getName());
	}

	/*
	 * Per individuare un MysteryGuest: 1. Cercare i "tipi" specifici coinvolti
	 * nell'uso dei file 2. Cercare i metodi tipicamente utilizzati nella
	 * gestione dei file.
	 */
	/**
	 * This method computes analysis for Mistery Guest
	 * test smell
	 * 
	 * @param xml
	 * @return
	 */
	public double analyze(File xml) {
		
		HashMap<String, ArrayList<String>> typeResult = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> callResult = new HashMap<String, ArrayList<String>>();
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
					/*
					 * In questo caso non serve controllare se sono in un metodo
					 * di test o meno perché l'uso dei file va individuato anche
					 * nei metodi accessori e non solo in quelli di test.
					 */
					calculateFileApiTypes(functionElement,typeResult);
					calculateFileApiFunctions(functionElement,callResult);
				}
			}
			
			results.add(new MysteryGuestResult(xml.getName(), typeResult, callResult));
			
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

//		for (String s : typeResult.keySet()) {
//			ArrayList<String> list = typeResult.get(s);
//			for (String t : list) {
//				log.info("file API types for method " + s + ": " + t);
//			}
//		}
//		
//		for(String s: callResult.keySet()){
//			ArrayList<String> list = callResult.get(s);
//			for(String t: list){
//				log.info("file API called method for method " + s + ": " + t);
//			}
//		}
		
		

		return 0;
	}
	
	@Override
	public void run() {
		log.info("*** START MYSTERY GUEST ANALYSIS ***");
		for (File file : data.getTestClasses())
			this.analyze(file);
		computeResults();
		log.info("*** END MYSTERY GUEST ANALYSIS ***\n");
	}

	private void computeResults() {
		
		for(MysteryGuestResult testCase : results){
			for(String methodName : testCase.getCallResult().keySet()){
				ArrayList<String> apiCalls = testCase.getCallResult().get(methodName);
				for(String call : apiCalls){
					log.info("File API method use: "+methodName+" calls "+call);
				}
			}
			
			for(String methodName : testCase.getTypeResult().keySet()){
				ArrayList<String> typeUses = testCase.getTypeResult().get(methodName);
				for(String use : typeUses){
					log.info("File API type use: "+methodName+" uses "+use);
				}
			}
			
		}
		
	}
	
	
	/*
	 * metodo che legge le chiamate a metodi di API per la gestione dei file
	 */
	private void calculateFileApiFunctions(Element functionElement,HashMap<String, ArrayList<String>> functionResult) {

		String methodName = TestParseTool.readMethodNameByFunction(functionElement);
		functionResult.put(methodName, new ArrayList<String>());
		NodeList callList = functionElement.getElementsByTagName(ToolConstant.CALL);
		for (int i = 0; i < callList.getLength(); i++) {
			Element call = (Element) callList.item(i);
			NodeList nameMethodList = call.getElementsByTagName(ToolConstant.NAME);
			for (int j = 0; j < nameMethodList.getLength(); j++) {
				Element callNode = (Element) nameMethodList.item(j);
				if (fileApiChecker.isFileApiFunction(callNode)) {
					// per il metodo corrente aggiungo le chiamate alla API di
					// gestione dei file
					String calledMethod = callNode.getTextContent();
					functionResult.get(methodName).add(calledMethod);
				}

			}
		}
	}

	/*
	 * metodo che legge i tipi definite nelle API di gestione file
	 */
	private void calculateFileApiTypes(Element functionElement,HashMap<String, ArrayList<String>> typeResult) {

		String methodName = TestParseTool.readMethodNameByFunction(functionElement);
		typeResult.put(methodName, new ArrayList<String>());
		NodeList typeNodeList = functionElement.getElementsByTagName(ToolConstant.TYPE);

		for (int i = 0; i < typeNodeList.getLength(); i++) {
			Element typeNode = (Element) typeNodeList.item(i);
			if (fileApiChecker.isFileApiType(typeNode)) {
				// per il metodo corrente vengono aggiunti i tipi.
				String type = typeNode.getTextContent();
				typeResult.get(methodName).add(type);
			}
		}

	}

	
}
