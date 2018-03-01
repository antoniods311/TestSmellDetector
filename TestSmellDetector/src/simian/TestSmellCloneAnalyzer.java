package simian;

import java.io.File;
import java.util.List;
import com.harukizaemon.simian.AuditListener;
import com.harukizaemon.simian.Checker;
import com.harukizaemon.simian.FileLoader;
import com.harukizaemon.simian.Option;
import com.harukizaemon.simian.Options;
import com.harukizaemon.simian.StreamLoader;

public class TestSmellCloneAnalyzer implements CloneAnalyzer{
	
	private static int numberOfCloneLines;
	
	@Override
	public void setRootFile(File root) {
		
	}

	@Override
	public int getCloneLinesNumber(List<File> files) {
		
		//ogni file va aggiunto alla lisa di file da analizzare con Simian
		AuditListener auditListener = new CloneAuditListener();
		Options options = new Options();
		options.setThreshold(6);
		options.setOption(Option.IGNORE_STRINGS, true);
		options.setOption(Option.LANGUAGE, "Java");
		
		Checker checker = new Checker(auditListener, options);
		StreamLoader streamLoader = new StreamLoader(checker);
		FileLoader fileLoader = new FileLoader(streamLoader);

		for(File file : files)
			fileLoader.load(file);
		
		checker.check();
		
		return numberOfCloneLines;
	}

	@Override
	public int getCloneLinesNumber(File root, List<File> files) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static void setAnalysisResult(int resultNumber){
		numberOfCloneLines = resultNumber;
	}

}
