package main;

import detector.*;
import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
		
		//Rappresentazione XML del caso di test
		String inputTc = ToolConstant.XML_DIR+fileName;
		String inputClass = ToolConstant.XML_DIR+classFileName;
		jxmlTranslator = new JavaToXmlTranslator();
		jxmlTranslator.load(new File(inputTc));
		File xmlTest = jxmlTranslator.translate();
		jxmlTranslator.load(new File(inputClass));
		File xmlClass = jxmlTranslator.translate();
		
		
		//Costruzione ArrayList per clone detection
		ArrayList<File> cloneFiles = new ArrayList<File>();
		cloneFiles.add(new File(ToolConstant.TEST_CASES_DIR+fileName));
		cloneFiles.add(new File(ToolConstant.TEST_CASES_DIR+cloneFileName));
		
		//Esecuzione delle analisi usando i diversi detector
		ArrayList<Detector> detectors = new ArrayList<Detector>();
//		detectors.add(new AssertionRouletteDetector(xmlTest));
		detectors.add(new EagerTestDetector(xmlTest,xmlClass));
//		detectors.add(new GeneralFixtureDetector(xmlTest));
//		detectors.add(new MysteryGuestDetector(xmlTest));
//		detectors.add(new SensitiveEqualityDetector(xmlTest));
//		detectors.add(new TestCodeDuplicationDetector(cloneFiles));
		
		for(Detector d: detectors){
			d.run();
			//Vedere come attendere terminazione di tutti prima di porcedere. Dovrebbe essere il join().
		}
		
		//Restituzione dei risultati.
		log.info("...End analysis\n");
	}

}
