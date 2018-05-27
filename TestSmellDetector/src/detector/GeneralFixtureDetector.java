package detector;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dataflowanalysis.ClassFieldsReader;
import dataflowanalysis.SetUpMethodAnalyzer;
import util.ClassNameExtractor;
import util.PackageTool;
import util.TestMethodChecker;
import util.TestParseTool;
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
	private TestMethodChecker testChecker;
	private static Logger log;
	private int generalFixtureAbs;
	private double generalFixturePerc;

	/**
	 * Constructor for GeneralFixtureDetector object
	 * 
	 * @param data
	 */
	public GeneralFixtureDetector(ToolData data) {
		this.data = data;
		this.generalFixtureAbs = data.getThresholdsContainer().getGeneralFixtureAbs();
		this.generalFixturePerc = data.getThresholdsContainer().getGeneralFixturePerc();
		this.testChecker = new TestMethodChecker();
		log = LogManager.getLogger(GeneralFixtureDetector.class.getName());
	}

	public double analyze(File xml) {

		int testMethodNumber = 0;
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		HashMap<String, HashMap<String, Boolean>> results = new HashMap<String, HashMap<String, Boolean>>();

		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();
			
			//Leggo il package
			String classPackage = PackageTool.constructPackage(doc);

			/*
			 * Quando analizzo il primo metodo di test della classe devo fare
			 * diverse cose: 1. chiamare ClassFieldReader per leggere i fields
			 * della classe 2. analizzare tutti i metododi di setUp per creare il
			 * set "createdSet" 3. creare un sottoinsieme di elementi comuni a 1
			 * e 2
			 * 
			 * Questi punti li devo fare solo per il primo metodo di test dal
			 * momento che fatti una volta vanno bene per tutti i metodi di
			 * test.
			 */
			boolean isFirstMethod = true;
			ClassFieldsReader fieldReader;

			SetUpMethodAnalyzer setUpAnalyzer;
			HashSet<String> fieldsSet, createdSet, commonElements = new HashSet<String>();
			NodeList functionList = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < functionList.getLength(); i++) {
				if (functionList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) functionList.item(i);
					// Se entro ho trovato un metodo di test
					if (testChecker.isTestMethod(functionElement)) {
						testMethodNumber++;
						String methodName = TestParseTool.readMethodNameByFunction(functionElement);
						if (isFirstMethod) {
							isFirstMethod = false;

							// 1. calcolo i fields
							fieldReader = new ClassFieldsReader(data, methodName);
							//fieldsSet = fieldReader.getClassFields();
							fieldsSet = fieldReader.getClassFields(classPackage);

							// 2. trovo e analizzo i setUp per creare il
							// "createdSet"
							setUpAnalyzer = new SetUpMethodAnalyzer(xml);
							createdSet = setUpAnalyzer.getCreatedSet();

							// 3. trovo elementi comuni a 1 e 2
							commonElements = new HashSet<String>();
							for (String element : fieldsSet) {
								if (createdSet.contains(element))
									commonElements.add(element);
							}
//							for(String x : commonElements){
//								System.out.println(methodName+" ce: "+x);
//							}
							
						}

						/*
						 * gli elementi comuni sono quelli che devono essere
						 * usati per valutare se c'è o meno un general fixture
						 */

						// inizializzo i risultati a false
						results.put(methodName, initializeNewMap(commonElements));

						// analizzo le call per trovare il nome della variabile
						// chiamata
						HashSet<String> callNames = findCallNames(functionElement);
						for(String varName : callNames){
							if(commonElements.contains(varName)){
								results.get(methodName).remove(varName);
								results.get(methodName).put(varName, true);
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

//		for(String mn : results.keySet()){
//			
//			System.out.println(mn);
//			for(String var : results.get(mn).keySet()){
//				System.out.println(var+"->"+results.get(mn).get(var)+",");				
//			}
//			System.out.println("---------------");
//		}
		
		String testClassName = ClassNameExtractor.extractClassNameFromPath(xml.getName())
				.replace(ToolConstant.MINUS, ToolConstant.DOT);
		int generalFixMethodNumber = 0;
		
		for(String mn : results.keySet()){
			int numOfNoUse = 0;
			for(String var : results.get(mn).keySet()){
				if(!results.get(mn).get(var)) numOfNoUse++;
			}
			
			if(numOfNoUse >= generalFixtureAbs){
				log.info("Value: Found General Fixture for "+ testClassName+"."+mn);
				generalFixMethodNumber++;
			}
			
		}
		
		double noUse = generalFixMethodNumber;
		double tmn = testMethodNumber;
		double perc = (noUse/tmn)*100;
		DecimalFormat df = new DecimalFormat("####0.00");
		if(perc >= generalFixturePerc)
			log.info("Perc: Percentage of GF test smell in "+ testClassName+ " "+df.format(perc)+"%");
		
		
		return 0;
	}

	/**
	 * 
	 * @param functionElement
	 * @return all calls names in method
	 * 
	 *         call 
	 *        	 |--- name 
	 *         			|--- name -> "varName"
	 * 
	 */
	private HashSet<String> findCallNames(Element functionElement) {

		HashSet<String> result = new HashSet<String>();

		NodeList funElChildList = functionElement.getElementsByTagName(ToolConstant.CALL);
		for (int i = 0; i < funElChildList.getLength(); i++) {
			if (funElChildList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element call = (Element) funElChildList.item(i);
				NodeList callChildList = call.getChildNodes();
				for (int j = 0; j < callChildList.getLength(); j++) {
					Node item = callChildList.item(j);
					if (item.getNodeType() == Node.ELEMENT_NODE && item.getNodeName() == ToolConstant.NAME) {

						NodeList nameChildList = item.getChildNodes();
						if (nameChildList != null) {
							for (int k = 0; k < nameChildList.getLength(); k++) {
								Node nameVarNode = nameChildList.item(k);
								if (nameVarNode.getNodeType() == Node.ELEMENT_NODE) {
									result.add(nameVarNode.getTextContent());
								}
							}
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @param commonElements
	 * @return a map common element --> false
	 */
	private HashMap<String, Boolean> initializeNewMap(HashSet<String> commonElements) {

		HashMap<String, Boolean> newMap = new HashMap<String, Boolean>();
		for (String element : commonElements)
			newMap.put(element, false);

		return newMap;

	}

	@Override
	public void run() {
		log.info("*** START GENERAL FIXTURE ANALYSIS ***");
		for (File file : data.getTestClasses())
			this.analyze(file);
		// computeResults();
		log.info("*** END GENERAL FIXTURE ANALYSIS ***\n");
	}

}
