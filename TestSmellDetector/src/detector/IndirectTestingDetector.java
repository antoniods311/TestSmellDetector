package detector;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import dataflowanalysis.DataFlowMethodAnalyzer;
import util.TestMethodChecker;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class IndirectTestingDetector extends Thread {

	private ToolData data;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private static Logger log;
	private DataFlowMethodAnalyzer methodAnalyzer;
	
	public IndirectTestingDetector(ToolData data){
		this.data = data;
		this.testChecker = new TestMethodChecker(); 
		log = LogManager.getLogger(IndirectTestingDetector.class.getName());
	}
	
	private void analyze(File file) {
		
		
	}
	
	
	@Override
	public void run() {
		log.info("*** START INDIRECT TESTING ANALYSIS ***");;
		for (File file : data.getTestClasses())
			this.analyze(file);
//		computeResults();
		
		log.info("*** END INDIRECT TESTING ANALYSIS ***\n");
	}
}
