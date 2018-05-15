package main;

import detector.*;
import threshold.ThresholdContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

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
	private static String test_cases_java_dir;
	private static String production_classes_dir;
	private static String test_cases_jar_path;
	private static String exclusion_file;
	private static String log4jConfig;
	
	public static void main(String[] args) throws URISyntaxException {	
		
		String configFileParam = args[0];
		ToolConstant.CONFIG_FILE_PATH = configFileParam;
		
		/*
		 * 0. Lettura delle properties per settare le directories
		 * di input/output e le soglie per il tool e settaggio
		 * dei parametri (anche log4j)
		 */
		Properties prop = new Properties();
		InputStream input = null;
		ThresholdContainer container = null;
		try{
			input = new FileInputStream(configFileParam);
			prop.load(input);
			
			//directories
			test_cases_jar_path = prop.getProperty(ToolConstant.TEST_CASES_JAR_DIR);
			production_classes_dir = prop.getProperty(ToolConstant.PRODUCTION_CLASS_JAVA_DIR);
			test_cases_java_dir = prop.getProperty(ToolConstant.TEST_CASES_JAVA_DIR);
			exclusion_file = prop.getProperty(ToolConstant.EXCLUSION_FILE);
			log4jConfig = prop.getProperty(ToolConstant.LOG4J_CONFIG);
			
			//Thresholds
			container = new ThresholdContainer();
			container.setAssertionRouletteAbs(Integer.parseInt(prop.getProperty(ToolConstant.ASSERTION_ROULETTE_ABS)));
			container.setAssertionRoulettePerc(Double.parseDouble(prop.getProperty(ToolConstant.ASSERTION_ROULETTE_PERC)));
			container.setEagerTestAbs(Integer.parseInt(prop.getProperty(ToolConstant.EAGER_TEST_ABS)));
			container.setEagerTestPerc(Double.parseDouble(prop.getProperty(ToolConstant.EAGER_TEST_PERC)));
			container.setIndirectTestingAbs(Integer.parseInt(prop.getProperty(ToolConstant.INDIRECT_TESTING_ABS)));
			container.setIndirectTestingPerc(Double.parseDouble(prop.getProperty(ToolConstant.INDIRECT_TESTING_PERC)));
			container.setGeneralFixtureAbs(Integer.parseInt(prop.getProperty(ToolConstant.GENERAL_FIXTURE_ABS)));
			container.setGeneralFixturePerc(Double.parseDouble(prop.getProperty(ToolConstant.GENERAL_FIXTURE_PERC)));
			container.setMysteryGuestAbs(Integer.parseInt(prop.getProperty(ToolConstant.MYSTERY_GUEST_ABS)));
			container.setMysteryGuestPerc(Double.parseDouble(prop.getProperty(ToolConstant.MYSTERY_GUEST_PERC)));
			container.setSensitiveEqualityAbs(Integer.parseInt(prop.getProperty(ToolConstant.SENSITIVE_EQUALITY_ABS)));
			container.setSensitiveEqualityPerc(Double.parseDouble(prop.getProperty(ToolConstant.SENSITIVE_EQUALITY_PERC)));
			container.setCodeDuplicationAbs(Integer.parseInt(prop.getProperty(ToolConstant.CODE_DUPLICATION_ABS)));
			container.setCodeDuplicationPerc(Double.parseDouble(prop.getProperty(ToolConstant.CODE_DUPLICATION_PERC)));
			container.setLazyTestAbs(Integer.parseInt(prop.getProperty(ToolConstant.LAZY_TEST_ABS)));
			container.setLazyTestPerc(Double.parseDouble(prop.getProperty(ToolConstant.LAZY_TEST_PERC)));
			
			//Log4j setup
			LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
			File logConfigFile = new File(log4jConfig);
			context.setConfigLocation(logConfigFile.toURI());
			log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
			
		}
		catch(IOException io){
			log.info("Error reading configuration file!");
			io.printStackTrace();
		}
		finally{
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		jxmlTranslator = new JavaToXmlTranslator();
		log.info("-------------------------------------START ANALYSIS-------------------------------------\n");
		
		/*
		 * 1. costruire call graph
		 * 2. traduzione production classes
		 * 3. traduzione casi di test
		 * (3a. calcolare tutti i metodi delle production classes)
		 * 4. costruire oggetto ToolData
		 * 5. costruire i detector
		 * 6. lanciare i detector
		 */
		
		File jarInput = new File(test_cases_jar_path);
		WalaCallGraphBuilder builder;
		try {
			// 1.Costruzione Call Graph
			log.info("Building Call Graph...");
			builder = new WalaCallGraphBuilder(jarInput,exclusion_file);
			CallGraph callGraph = builder.buildCallGraph();
			log.info("done\n");
			
			// 2.Traduzione delle production classes
			log.info("Production classes translation...");
			ArrayList<File> xmlProdClasses = new ArrayList<File>();
			File prodClassDir = new File(production_classes_dir);
			String prodClasses[] = prodClassDir.list();
			for(int i=0; i<prodClasses.length; i++){
				if(FilenameUtils.getExtension(prodClasses[i]).equalsIgnoreCase(ToolConstant.JAVA_EXTENSION)){
					jxmlTranslator.load(new File(prodClasses[i]), ToolConstant.PRODUCTION_CLASS);
					xmlProdClasses.add(jxmlTranslator.translate());
				}
			}
			log.info("done\n");			
			
			// 3.Traduzione dei casi di test
			log.info("Test classes transaltion...");
			ArrayList<File> javaTestCases = new ArrayList<File>();
			ArrayList<File> xmlTestCases = new ArrayList<File>();
			File testCasesDir = new File(test_cases_java_dir);
			String testCases[] = testCasesDir.list();
			for(int i=0; i<testCases.length; i++){
				if(FilenameUtils.getExtension(testCases[i]).equalsIgnoreCase(ToolConstant.JAVA_EXTENSION)){
					jxmlTranslator.load(new File(testCases[i]), ToolConstant.TEST_CLASS);
					xmlTestCases.add(jxmlTranslator.translate());
					javaTestCases.add(new File(test_cases_java_dir+testCases[i]));
				}
			}
			log.info("done\n");
			
			// 3a. Calcolo di tutti i metodi delle production class
			ProductionClassAnalyzer prodClassAnalyzer = new ProductionClassAnalyzer(xmlProdClasses);
			HashSet<ToolMethodType> productionClassesMethods = prodClassAnalyzer.getClassMethods();
			
			// 4. Costruzione oggetto ToolData
			ToolData data = new ToolData();
			data.setCallGraph(callGraph);
			data.setProductionClasses(xmlProdClasses);
			data.setTestClasses(xmlTestCases);
			data.setJavaTestClasses(javaTestCases);
			data.setProductionMethods(productionClassesMethods);
			data.setThresholdsContainer(container);
				
			//Esecuzione delle analisi usando i diversi detector
			ArrayList<Thread> detectors = new ArrayList<Thread>();
//			detectors.add(new AssertionRouletteDetector(data)); //ok
//			detectors.add(new EagerTestDetector(data)); //ok
//			detectors.add(new IndirectTestingDetector(data));//ok
//			detectors.add(new GeneralFixtureDetector(data)); //ok
//			detectors.add(new MysteryGuestDetector(data)); //ok
//			detectors.add(new SensitiveEqualityDetector(data)); //ok
			detectors.add(new TestCodeDuplicationDetector(data)); //ok
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
		log.info("-------------------------------------END ANALYSIS-------------------------------------\n");
	}

}
