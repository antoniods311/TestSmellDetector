package simian;

import java.io.File;
import java.util.List;

import com.harukizaemon.simian.CheckSummary;

public interface CloneAnalyzer {

	/**
	 * This method calculates number of 
	 * clone lines in a list of files
	 * 
	 * @param files
	 * @return the number of clone lines
	 */
	public SimianResult execSimianAnalysis(List<File> files);
	
}
