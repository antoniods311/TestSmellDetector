package simian;

import java.io.File;
import java.util.ArrayList;

import com.harukizaemon.simian.AuditListener;
import com.harukizaemon.simian.Block;
import com.harukizaemon.simian.CheckSummary;
import com.harukizaemon.simian.Options;
import com.harukizaemon.simian.SourceFile;

public class CloneAuditListener implements AuditListener{
	
	private SimianResult result;
	private static int index = 0;

	@Override
	public void block(Block block) {
		result.getSet().get(index).getClones().add(block);
	}

	@Override
	public void startCheck(Options options) {
		result = new SimianResult(null, new ArrayList<BlockSet>());
	}
	
	@Override
	public void endCheck(CheckSummary checkSummary) {
		result.setCheckSummary(checkSummary);
		TestSmellCloneAnalyzer.setSimianResult(result);
	}

	@Override
	public void startSet(int lineCount, String fingerprint) {
		result.getSet().add(new BlockSet(new ArrayList<Block>()));
	}
	
	@Override
	public void endSet(String arg0) {
		index++;
	}

	@Override
	public void error(File arg0, Throwable arg1) {

	}

	@Override
	public void fileProcessed(SourceFile sourceFile) {
		
	}
}
