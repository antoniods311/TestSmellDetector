package detector;

import java.io.File;

/**
 * 
 * @author antoniods311
 *
 */
public class AssertionRouletteDetector implements Detector {
	
private File xml;
	
	public AssertionRouletteDetector(File xml){
		this.xml = xml;
	}
	
	@Override
	public void run(){
		analyze();
	}

	@Override
	public int analyze() {
		// TODO Auto-generated method stub
		return 0;
	}

}
