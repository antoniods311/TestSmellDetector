package detector;

import java.io.File;

import com.ibm.wala.ipa.callgraph.CallGraph;

/**
 * 
 * @author antoniods311
 *
 */
public class GeneralFixtureDetector implements Detector {
	
private File xml;
	
	public GeneralFixtureDetector(File xml, CallGraph callGraph){
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
