package simian;

import java.io.File;
import java.util.List;

public interface CloneAnalyzer {

	public void setRootFile(File root);
	public int getCloneLinesNumber(List<File> files);
	
}
