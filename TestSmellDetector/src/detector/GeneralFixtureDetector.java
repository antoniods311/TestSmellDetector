package detector;

import java.io.File;

/**
 * 
 * @author antoniods311
 *
 */
public class GeneralFixtureDetector implements Detector {
	
private File xml;
	
	public GeneralFixtureDetector(File xml){
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
