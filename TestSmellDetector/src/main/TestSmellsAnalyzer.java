package main;

import detector.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.util.WalaException;

import callgraph.WalaCallGraphBuilder;
import translator.JavaToXmlTranslator;
import util.ToolConstant;
import util.prodclass.ProductionClassAnalyzer;
import util.prodclass.ToolMethodType;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class TestSmellsAnalyzer {

	private static JavaToXmlTranslator jxmlTranslator;
	private static Logger log;
	
	public static void main(String[] args) {
		
		log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
		jxmlTranslator = new JavaToXmlTranslator();
		log.info("Start analysis...\n");
		
		/*
		 * 1. costruire call graph
		 * 2. traduzione production classes
		 * 3. traduzione casi di test
		 * (3a. calcolare tutti i metodi delle production classes)
		 * 4. costruire oggetto ToolData
		 * 5. costruire i detector
		 * 6. lanciare i detector
		 */
		
		File jarInput = new File(ToolConstant.TEST_CASES_JAR_DIR+"calc.jar");
		WalaCallGraphBuilder builder;
		try {
			// 1.Costruzione Call Graph
			builder = new WalaCallGraphBuilder(jarInput);
			CallGraph callGraph = builder.buildCallGraph();
			
			// 2.Traduzione delle production classes
			ArrayList<File> xmlProdClasses = new ArrayList<File>();
			File prodClassDir = new File(ToolConstant.PRODUCTION_CLASS_DIR);
			String prodClasses[] = prodClassDir.list();
			for(int i=0; i<prodClasses.length; i++){
				jxmlTranslator.load(new File(prodClasses[i]), ToolConstant.PRODUCTION_CLASS);
				xmlProdClasses.add(jxmlTranslator.translate());
			}
			
			// 3.Traduzione dei casi di test
			ArrayList<File> xmlTestCases = new ArrayList<File>();
			File testCasesDir = new File(ToolConstant.TEST_CASES_JAVA_DIR);
			String testCases[] = testCasesDir.list();
			for(int i=0; i<testCases.length; i++){
				jxmlTranslator.load(new File(testCases[i]), ToolConstant.TEST_CLASS);
				xmlTestCases.add(jxmlTranslator.translate());
			}
			
			// 3a. Calcolo di tutti i metodi delle production class
			ProductionClassAnalyzer prodClassAnalyzer = new ProductionClassAnalyzer(xmlProdClasses);
			HashSet<ToolMethodType> productionClassesMethods = prodClassAnalyzer.getClassMethods();
			
			// 4. Costruzione oggetto ToolData
			ToolData data = new ToolData();
			data.setCallGraph(callGraph);
			data.setProductionClasses(xmlProdClasses);
			data.setTestClasses(xmlTestCases);
			data.setProductionMethods(productionClassesMethods);
				
			//Esecuzione delle analisi usando i diversi detector
			ArrayList<Thread> detectors = new ArrayList<Thread>();
//			detectors.add(new AssertionRouletteDetector(data)); //ok
//			detectors.add(new EagerTestDetector(data)); //ok
			detectors.add(new IndirectTestingDetector(data));
//			detectors.add(new GeneralFixtureDetector(data));
//			detectors.add(new MysteryGuestDetector(data)); //ok
//			detectors.add(new SensitiveEqualityDetector(data)); //ok
//			detectors.add(new TestCodeDuplicationDetector(data)); //ok
//			detectors.add(new LazyTestDetector(data)); //ok
			
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
