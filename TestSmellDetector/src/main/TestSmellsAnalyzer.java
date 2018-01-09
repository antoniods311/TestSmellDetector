package main;

import java.util.ArrayList;

import detector.Detector;
import translator.JavaToXmlTranslator;

public class TestSmellsAnalyzer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Start analysis...");
		
		//Rappresentazione XML del caso di test
		JavaToXmlTranslator jxmlTranslator = new JavaToXmlTranslator();
		String xml = jxmlTranslator.translate();
		
		ArrayList<Detector> detectors = new ArrayList<Detector>();
		for(Detector d: detectors){
			d.run();
			//Vedere come attendere terminazione di tutti prima di porcedere. Dovrebbe essere il join().
		}

	}

}
