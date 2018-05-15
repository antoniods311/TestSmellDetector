package detector;

import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.harukizaemon.simian.Block;

import simian.BlockSet;
import simian.CloneAnalyzer;
import simian.SimianResult;
import simian.TestSmellCloneAnalyzer;
import util.ClassNameExtractor;
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
		
		SimianResult simianResult = duplicationAnalyzer.execSimianAnalysis(data.getJavaTestClasses());
		
		int numberOfDuplicatedLines = simianResult.getCheckSummary().getDuplicateLineCount();
		
		if(numberOfDuplicatedLines >= codeDuplicationAbs){
			log.info("VALUE: # cloned lines: "+numberOfDuplicatedLines);
		}
		
//		double percentageOfDuplicatedLines = simianResult.getCheckSummary().getDuplicateLinePercentage();
		double loc = simianResult.getCheckSummary().getTotalSignificantLineCount();
		double dloc = simianResult.getCheckSummary().getDuplicateLineCount();
		double perc = (dloc/loc)*100;
		DecimalFormat df = new DecimalFormat("####0.00");
		if(perc >= codeDuplicationPerc){
			log.info("PERCENTAGE: % cloned lines: "+df.format(perc)+"%");
		}
		
		log.info("Block Sets:");
		for(BlockSet bs : simianResult.getSet()){
			log.info("start set");
			for(Block block : bs.getClones()){
				log.info(ClassNameExtractor.extractClassNameFromPath(block.getSourceFile().getFilename())+ 
						" lines: "+block.getStartLineNumber() +"-"+block.getEndLineNumber());
			}
			log.info("end set\n");
		}
		
		log.info("*** END CODE DUPLICATION ANALYSIS ***\n");
		
		return numberOfDuplicatedLines; //non necessario restituire questo valore
	}
	
	@Override
	public void run() {
		analyze();
	}

}
