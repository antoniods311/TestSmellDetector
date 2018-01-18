package detector;

import java.io.File;

/**
 * 
 * @author antoniods311
 *
 */
public class MysteryGuestDetector implements Detector {
	
	private File xml;
	
	public MysteryGuestDetector(File xml){
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
