package main;

import detector.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.util.WalaException;

import callgraph.WalaCallGraphBuilder;
import translator.JavaToXmlTranslator;
import util.ToolConstant;

/**
 * 
 * @author antoniods311
 *
 */
public class TestSmellsAnalyzer {

	private static String fileName = "MyTest.java";
	private static String cloneFileName = "MyTestClone.java";
	private static String classFileName = "MyClass.java";
	private static JavaToXmlTranslator jxmlTranslator;
	private static Logger log;
	
	public static void main(String[] args) {
		
		log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
		log.info("Start analysis...\n");
		
		File prodClassDir = new File(ToolConstant.PRODUCTION_CLASS_DIR);
		String prodClasses[] = prodClassDir.list();
		for(int i=0; i<prodClasses.length; i++){
			System.out.println(prodClasses[i]);
		}
		
		
		//Costruzione Call Graph
		File jarInput = new File(ToolConstant.TEST_CASES_JAR_DIR+"calc.jar");
		WalaCallGraphBuilder builder;
		try {
			builder = new WalaCallGraphBuilder(jarInput);
			CallGraph callGraph = builder.buildCallGraph();
			
			/*
			 * 1. costruire call graph
			 * 2. traduzione production classes
			 * 3. traduzione cassi di test
			 * 4. calcolare tutti i metodi delle production classes
			 * 5. costruire oggetto ToolData
			 * 6. costruire i detector
			 * 7. lanciare i detector
			 */
			
//			File prodClassDir = new File(ToolConstant.PRODUCTION_CLASS_DIR);
//			String prodClasses[] = prodClassDir.list();
//			for(int i=0; i<prodClasses.length; i++){
//				System.out.println(prodClasses[i]);
//			}
			
			
			
			//Rappresentazione XML del caso di test
			String inputTc = ToolConstant.XML_DIR+fileName;
			String productionClass = ToolConstant.XML_DIR+classFileName;
			jxmlTranslator = new JavaToXmlTranslator();
			jxmlTranslator.load(new File(inputTc));
			File xmlTest = jxmlTranslator.translate();
			
			jxmlTranslator.load(new File(productionClass));
			File xmlClass = jxmlTranslator.translate();
			
			
			//Costruzione ArrayList per clone detection
			ArrayList<File> cloneFiles = new ArrayList<File>();
			cloneFiles.add(new File(ToolConstant.TEST_CASES_JAVA_DIR+fileName));
			cloneFiles.add(new File(ToolConstant.TEST_CASES_JAVA_DIR+cloneFileName));
			
			//Esecuzione delle analisi usando i diversi detector
			ArrayList<Thread> detectors = new ArrayList<Thread>();
//			detectors.add(new AssertionRouletteDetector(xmlTest));
//			detectors.add(new EagerTestDetector(xmlTest,xmlClass,callGraph));
//			detectors.add(new GeneralFixtureDetector(xmlTest,callGraph));
//			detectors.add(new MysteryGuestDetector(xmlTest));
//			detectors.add(new SensitiveEqualityDetector(xmlTest));
//			detectors.add(new TestCodeDuplicationDetector(cloneFiles));
			
			for(Thread d: detectors){
				d.run();
				//Vedere come attendere terminazione di tutti prima di porcedere. Dovrebbe essere il join().
			}
			
			
		} catch (IOException | WalaException e) {
			log.error(ToolConstant.BUILD_CALL_GRAPH_ERROR);
			e.printStackTrace();
		}
		
		
		
		
		//Restituzione dei risultati.
		log.info("...End analysis\n");
	}

}
