package detector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simian.CloneAnalyzer;
import simian.TestSmellCloneAnalyzer;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class TestCodeDuplicationDetector extends Thread {

	private ToolData data;
	private static Logger log;
	private CloneAnalyzer duplicationAnalyzer;
	private int codeDuplicationAbs;
	private double codeDuplicationPerc;

	/**
	 * Constructor for TestCloneDetector class
	 * 
	 * @param files
	 */
	public TestCodeDuplicationDetector(ToolData data) {
		this.data = data;
		this.codeDuplicationAbs = data.getThresholdsContainer().getCodeDuplicationAbs();
		this.codeDuplicationPerc = data.getThresholdsContainer().getCodeDuplicationPerc();
		this.duplicationAnalyzer = new TestSmellCloneAnalyzer();
		log = LogManager.getLogger(TestCodeDuplicationDetector.class.getName());
	}
	
	/**
	 * This method computes analysis for test code duplication
	 * 
	 * @return number of duplicated lines in test files
	 */
	public int analyze() {

		log.info("*** START CODE DUPLICATION ANALYSIS ***");
		
		int numberOfDuplicatedLines = duplicationAnalyzer.getCloneLinesNumber(data.getTestClasses());
		log.info("number of cloned lines: "+numberOfDuplicatedLines);
		
		log.info("*** END CODE DUPLICATION ANALYSIS ***\n");
		
		return numberOfDuplicatedLines; //non necessario restituire questo valore
	}
	
	@Override
	public void run() {
		analyze();
	}

}
