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

		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();
			
			System.out.println("Root:\t"+doc.getDocumentElement().getNodeName());
			Element root = doc.getDocumentElement();
			System.out.println("Language:\t"+root.getAttribute("language"));
			
			//leggo la lista di nodi function
			NodeList list = doc.getElementsByTagName("function");
			for(int i=0; i<list.getLength(); i++){
				if(list.item(i).getNodeType() == Node.ELEMENT_NODE){
					Element functionElement = (Element) list.item(i);
					System.out.println(functionElement.getNodeName());
					
					if(isTestMethod(functionElement)){
						System.out.println("ho trovato un test");
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
	
	/*
	 * metodo che controlla se un metodo del TC è un test o meno.
	 * Si verifica che ci sia l'annotazione @Test
	 */
	private boolean isTestMethod(Element element) {

		boolean isTest = false;
		int i = 0;
		NodeList annotationList = element.getElementsByTagName("annotation");
		while(!isTest && i < annotationList.getLength()){
			Element annotationElement = (Element) annotationList.item(i);
			NodeList nameList = annotationElement.getElementsByTagName("name");
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