package simian;

import java.io.File;
import com.harukizaemon.simian.AuditListener;
import com.harukizaemon.simian.Block;
import com.harukizaemon.simian.CheckSummary;
import com.harukizaemon.simian.Options;
import com.harukizaemon.simian.SourceFile;

public class CloneAuditListener implements AuditListener{

	@Override
	public void block(Block arg0) {
		
	}

	@Override
	public void endCheck(CheckSummary arg0) {
		TestSmellCloneAnalyzer.setAnalysisResult(arg0.getDuplicateLineCount());
	}

	@Override
	public void endSet(String arg0) {
		
	}

	@Override
	public void error(File arg0, Throwable arg1) {

	}

	@Override
	public void fileProcessed(SourceFile arg0) {
		
	}

	@Override
	public void startCheck(Options arg0) {
			
	}

	@Override
	public void startSet(int arg0, String arg1) {
		
	}

}
