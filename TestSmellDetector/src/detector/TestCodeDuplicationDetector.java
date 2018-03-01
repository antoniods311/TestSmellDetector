package detector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 * @author antoniods311
 *
 */
public class TestCodeDuplicationDetector implements Detector {

	private static Logger log;

	public TestCodeDuplicationDetector() {
		log = LogManager.getLogger(TestCodeDuplicationDetector.class.getName());
	}

	@Override
	public void run() {
		analyze();
	}

	@Override
	public double analyze() {

		log.info("*** START CODE DUPLICATION ANALYSIS ***");
		
		//esecuzione del tool o utilizzo della API di Simian
		
		
		log.info("*** END CODE DUPLICATION ANALYSIS ***");
		
		return 0;
	}

}
