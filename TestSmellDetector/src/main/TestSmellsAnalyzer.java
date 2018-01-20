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
	private static Logger log;
	
	public static void main(String[] args) {
		
		log = LogManager.getLogger(TestSmellsAnalyzer.class.getName());
		log.info("Start analysis...");
		
		//Rappresentazione XML del caso di test
		String inputTc = ToolConstant.XML_DIR+fileName;
		JavaToXmlTranslator jxmlTranslator = new JavaToXmlTranslator();
		jxmlTranslator.load(new File(inputTc));
		File xml = jxmlTranslator.translate();
		
		//Esecuzione delle analisi usando i diversi detector
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		detectors.add(new AssertionRouletteDetector(xml));
		detectors.add(new EagerTestDetector(xml));
		detectors.add(new GeneralFixtureDetector(xml));
		detectors.add(new MysteryGuestDetector(xml));
		detectors.add(new TestCodeDuplicationDetector(xml));
		
		for(Detector d: detectors){
			d.run();
			//Vedere come attendere terminazione di tutti prima di porcedere. Dovrebbe essere il join().
		}
		
		//Restituzione dei risultati.
		log.info("...End analysis");
	}

}
