package simian;

import java.io.File;
import java.util.List;

import com.harukizaemon.simian.AuditListener;
import com.harukizaemon.simian.Checker;
import com.harukizaemon.simian.FileLoader;
import com.harukizaemon.simian.Language;
import com.harukizaemon.simian.Option;
import com.harukizaemon.simian.Options;
import com.harukizaemon.simian.StreamLoader;

public class TestSmellCloneAnalyzer implements CloneAnalyzer{

	private static SimianResult simianResult;

	@Override
	public SimianResult execSimianAnalysis(List<File> files) {
		
		//ogni file va aggiunto alla lisa di file da analizzare con Simian
		AuditListener auditListener = new CloneAuditListener();
		Options options = new Options();
		options.setThreshold(6);
		options.setOption(Option.IGNORE_STRINGS, true);
		options.setOption(Option.LANGUAGE, Language.JAVA);
//		options.setOption(Option.REPORT_DUPLICATE_TEXT, true);
		
		Checker checker = new Checker(auditListener, options);
		StreamLoader streamLoader = new StreamLoader(checker);
		FileLoader fileLoader = new FileLoader(streamLoader);
		
		for(File file : files)
			fileLoader.load(file);
			
		checker.check();
		
		return simianResult;
	}
	

	/**
	 * This method is used to set 
	 * SIMIAN tool analysis results
	 * 
	 * @param checkSummary
	 */
	public static void setSimianResult(SimianResult result) {
		simianResult = result;
		
	}

}
