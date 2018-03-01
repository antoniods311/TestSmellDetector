package detector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import simian.CloneAnalyzer;
import simian.TestSmellCloneAnalyzer;

/**
 * 
 * @author antoniods311
 *
 */
public class TestCodeDuplicationDetector implements Detector {

	private static Logger log;
	private CloneAnalyzer duplicationAnalyzer;
	private List<File> files = new ArrayList<File>();

	public TestCodeDuplicationDetector(List<File> files) {
		log = LogManager.getLogger(TestCodeDuplicationDetector.class.getName());
		duplicationAnalyzer = new TestSmellCloneAnalyzer();
	}

	@Override
	public void run() {
		analyze();
	}

	@Override
	public double analyze() {

		log.info("*** START CODE DUPLICATION ANALYSIS ***");
		
		int numberOfDuplicatedLines = duplicationAnalyzer.getCloneLinesNumber(files);
		log.info("number of cloned lines: "+numberOfDuplicatedLines);
		
		log.info("*** END CODE DUPLICATION ANALYSIS ***");
		
		return 0;
	}

}
