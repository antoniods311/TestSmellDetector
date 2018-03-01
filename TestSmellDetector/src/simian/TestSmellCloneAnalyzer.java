package simian;

import java.io.File;
import java.util.List;

public class TestSmellCloneAnalyzer implements CloneAnalyzer{
	
	@Override
	public void setRootFile(File root) {
		
	}

	@Override
	public int getCloneLinesNumber(List<File> files) {
		
		//ogni file va aggiunto alla lisa di file da analizzare con Simian
		
		return 0;
	}

	@Override
	public int getCloneLinesNumber(File root, List<File> files) {
		// TODO Auto-generated method stub
		return 0;
	}

}
