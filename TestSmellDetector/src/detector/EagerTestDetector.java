package detector;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import util.ToolConstant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author antoniods311
 *
 */
public class EagerTestDetector implements Detector{
	
	private File xml;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	
	public EagerTestDetector(File xml){
		this.xml = xml;
	}

	/*
	 * bisogna navigare ricorsivamente il doc xml in modo tale che per ogni function (annotata con @Test) che incontra
	 * vado a verificare il contenuto dell'elemento block. 
	 * Nel block ci possono essere 2 situazioni:
	 * - trovo un assert e allora devo aggiungerlo alla lista di assert trovati;
	 * - trovo un altro blocco. Allora devo fare chiamata ricorsiva per visitare il blocco.
	 */
	
	/*
	 * altra soluzione (per gestire anche casi in cui c'è un if annidato o un while per esempio):
	 * - considero i singoli elementi function (annotati con @Test)
	 * - mi prendo la lista di NODI
	 * - scorro la lista prendendomi solo quelli che sono "ELEMENT_NODE"
	 * - tra questi conto gli assert
	 */
	@Override
	public int analyze() {

		int numberOfAsserts = 0;
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();
	
			//leggo la lista di nodi function
			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for(int i=0; i<list.getLength(); i++){
				if(list.item(i).getNodeType() == Node.ELEMENT_NODE){
					Element functionElement = (Element) list.item(i);
					System.out.println(functionElement.getNodeName());
				
					//Se entro ho trovato un metodo di test e devo cercare le chiamate dei metodi assert
					if(isTestMethod(functionElement))
						numberOfAsserts = getAssertsNumber(functionElement);
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
		
		System.out.println("Eager Test --> numero di assert presenti: "+numberOfAsserts);
		
		return 0;
	}
	
	/*
	 * metodo che conta il numero degli assert all'interno di un test
	 */
	private int getAssertsNumber(Element functionElement){
		
		int numOfAssert = 0;
		int i;
		NodeList callList = functionElement.getElementsByTagName("call");
		System.out.println("callList: "+callList.getLength());
		for(i=0; i<callList.getLength(); i++){
			Element call = (Element) callList.item(i);
			NodeList nameMethodList = call.getElementsByTagName(ToolConstant.NAME);
			int j;
			for(j=0; j<nameMethodList.getLength(); j++){
				if(isAssertMethod(nameMethodList.item(j).getTextContent()))
					numOfAssert++;
			}
			
			
		}
			
		System.out.println("assert trovati: "+numOfAssert);
		return numOfAssert;
	}
	
	/*
	 * metodo che controlla se il nome del nodo passato è un assert
	 */
	private boolean isAssertMethod(String nodeValue) {
		
		boolean isAssert = false;
		int index = 0;
		while(index < ToolConstant.ASSERT_METHODS.length && !isAssert){
			if(nodeValue.equalsIgnoreCase(ToolConstant.ASSERT_METHODS[index]))
				isAssert = true;
			index++;
		}		
		System.out.println("assert analizzato: "+nodeValue);
		
		return isAssert;
	}

	/*
	 * metodo che controlla se un metodo del TC è un test o meno.
	 * Si verifica che ci sia l'annotazione @Test
	 */
	private boolean isTestMethod(Element element) {

		boolean isTest = false;
		int i = 0;
		NodeList annotationList = element.getElementsByTagName(ToolConstant.ANNOTATION);
		while(!isTest && i < annotationList.getLength()){
			Element annotationElement = (Element) annotationList.item(i);
			NodeList nameList = annotationElement.getElementsByTagName(ToolConstant.NAME);
			int j;
			for(j=0; j<nameList.getLength(); j++){
				Element nameElement = (Element) nameList.item(j);;
				if(nameElement.getTextContent().equalsIgnoreCase(ToolConstant.TEST_ANNOTATION))
					isTest = true;
				j++;
			}
			i++;
		}
		
		return isTest;
	}

	@Override
	public void run(){
		analyze();
	}

}