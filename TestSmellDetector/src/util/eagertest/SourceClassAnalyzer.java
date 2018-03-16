package util.eagertest;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;
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
import util.TestParseTool;
import util.ToolConstant;

public class SourceClassAnalyzer {
	
	private File sourceXml;
	private TreeSet<String> classMethods;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private static Logger log;
	
	public SourceClassAnalyzer(File sourceXml){
		this.sourceXml = sourceXml;
		this.classMethods = new TreeSet<String>();
		log = LogManager.getLogger(SourceClassAnalyzer.class.getName());
	}
	
	/*
	 * Controlla se il metodo passato come parametro
	 * è o meno un metodo della classe "sorgente",
	 * quella per la quale sono stati scritti i TC.
	 */
	public boolean isSourceMethod(String method){
		
		boolean isMethod = false;
		
		/*
		 * devo leggere l'xml e vedere se method
		 * appartiene a sourceXml.
		 */
	
		return isMethod;
	}
	
	/*
	 * restituisce tutti i metodi della production class
	 * sotto test.
	 */
	public TreeSet<String> getClassMethods(){
		
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(sourceXml);
			doc.getDocumentElement().normalize();
			
			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) list.item(i);
					String methodName = TestParseTool.readMethodNameByFunction(functionElement);
					classMethods.add(methodName);
				}
			}
		} catch (ParserConfigurationException e) {
			log.error(ToolConstant.PARSE_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (SAXException e) {
			log.error(ToolConstant.SAX_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(ToolConstant.IO_EXCEPTION_MSG);
			e.printStackTrace();
		}
		
		return classMethods;
	}

	public File getSourceXml() {
		return sourceXml;
	}

	public void setSourceXml(File sourceXml) {
		this.sourceXml = sourceXml;
	}

}
