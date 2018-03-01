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
	private static Logger log;
	
	public static void main(String[] args) {
		
		log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
		log.info("Start analysis...\n");
		
		//Rappresentazione XML del caso di test
		String inputTc = ToolConstant.XML_DIR+fileName;
		JavaToXmlTranslator jxmlTranslator = new JavaToXmlTranslator();
		jxmlTranslator.load(new File(inputTc));
		File xml = jxmlTranslator.translate();
		
		//Costruzione ArrayList per clone detection
		System.out.println(ToolConstant.TEST_CASES_DIR+fileName);
		System.out.println(ToolConstant.TEST_CASES_DIR+cloneFileName);
		ArrayList<File> cloneFiles = new ArrayList<File>();
		cloneFiles.add(new File(ToolConstant.TEST_CASES_DIR+fileName));
		cloneFiles.add(new File(ToolConstant.TEST_CASES_DIR+cloneFileName));
		
		//Esecuzione delle analisi usando i diversi detector
		ArrayList<Detector> detectors = new ArrayList<Detector>();
//		detectors.add(new AssertionRouletteDetector(xml));
//		detectors.add(new EagerTestDetector(xml));
//		detectors.add(new GeneralFixtureDetector(xml));
//		detectors.add(new MysteryGuestDetector(xml));
//		detectors.add(new SensitiveEqualityDetector(xml));
		detectors.add(new TestCodeDuplicationDetector(cloneFiles));
		
		for(Detector d: detectors){
			d.run();
			//Vedere come attendere terminazione di tutti prima di porcedere. Dovrebbe essere il join().
		}
		
		//Restituzione dei risultati.
		log.info("...End analysis\n");
	}

}
