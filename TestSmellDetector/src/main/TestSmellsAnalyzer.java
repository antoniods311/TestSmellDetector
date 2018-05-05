package main;

import detector.*;
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
	private static String TEST_CASES_JAVA_DIR;
	private static String PRODUCTION_CLASS_DIR;
	private static String TEST_CASES_JAR_PATH;
	private static String EXCLUSION_FILE;
	
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
		try{
			input = new FileInputStream(configFileParam);
			prop.load(input);
			TEST_CASES_JAR_PATH = prop.getProperty("jar_file");
			PRODUCTION_CLASS_DIR = prop.getProperty("java_pc_dir");
			TEST_CASES_JAVA_DIR = prop.getProperty("java_tc_dir");
			EXCLUSION_FILE = prop.getProperty("exclusion_file");
			String log4jConfig = prop.getProperty("log4j_config");
			
			/*
			 * Log4j setup
			 */
			LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
			File file = new File(log4jConfig);
			context.setConfigLocation(file.toURI());
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
		
		File jarInput = new File(TEST_CASES_JAR_PATH);
		WalaCallGraphBuilder builder;
		try {
			// 1.Costruzione Call Graph
			log.info("Building Call Graph...");
			builder = new WalaCallGraphBuilder(jarInput,EXCLUSION_FILE);
			CallGraph callGraph = builder.buildCallGraph();
			log.info("done\n");
			
			// 2.Traduzione delle production classes
			log.info("Production classes translation...");
			ArrayList<File> xmlProdClasses = new ArrayList<File>();
			File prodClassDir = new File(PRODUCTION_CLASS_DIR);
			String prodClasses[] = prodClassDir.list();
			for(int i=0; i<prodClasses.length; i++){
				if(FilenameUtils.getExtension(prodClasses[i]).equalsIgnoreCase(ToolConstant.JAVA_EXTENSION)){
					jxmlTranslator.load(new File(prodClasses[i]), ToolConstant.PRODUCTION_CLASS);
					xmlProdClasses.add(jxmlTranslator.translate());
					//log.info(prodClasses[i]+"...done");
				}
			}
			log.info("done\n");			
			
			// 3.Traduzione dei casi di test
			log.info("Test classes transaltion...");
			ArrayList<File> xmlTestCases = new ArrayList<File>();
			File testCasesDir = new File(TEST_CASES_JAVA_DIR);
			String testCases[] = testCasesDir.list();
			for(int i=0; i<testCases.length; i++){
				if(FilenameUtils.getExtension(testCases[i]).equalsIgnoreCase(ToolConstant.JAVA_EXTENSION)){
					jxmlTranslator.load(new File(testCases[i]), ToolConstant.TEST_CLASS);
					xmlTestCases.add(jxmlTranslator.translate());
					//log.info(testCases[i]+"...done");
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
			data.setProductionMethods(productionClassesMethods);
				
			//Esecuzione delle analisi usando i diversi detector
			ArrayList<Thread> detectors = new ArrayList<Thread>();
			detectors.add(new AssertionRouletteDetector(data)); //ok
			detectors.add(new EagerTestDetector(data)); //ok
			detectors.add(new IndirectTestingDetector(data));//ok
			detectors.add(new GeneralFixtureDetector(data)); //ok
			detectors.add(new MysteryGuestDetector(data)); //ok
			detectors.add(new SensitiveEqualityDetector(data)); //ok
			detectors.add(new TestCodeDuplicationDetector(data)); //ok
			detectors.add(new LazyTestDetector(data)); //ok
			
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
