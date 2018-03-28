package simian;

import java.io.File;
import java.util.List;

public interface CloneAnalyzer {

	/**
	 * This method calculates number of 
	 * clone lines in a list of files
	 * 
	 * @param files
	 * @return the number of clone lines
	 */
	public int getCloneLinesNumber(List<File> files);
	
}
