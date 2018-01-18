package detector;

import java.io.File;

/**
 * 
 * @author antoniods311
 *
 */
public class SensitiveEqualityDetector implements Detector {
	
	private File xml;
	
	public SensitiveEqualityDetector(File xml){
		this.xml = xml;
	}
	
	@Override
	public void run(){
		analyze();
	}

	@Override
	public double analyze() {
		// TODO Auto-generated method stub
		return 0;
	}

}
