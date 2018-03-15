package detector;

import java.io.File;

import com.ibm.wala.ipa.callgraph.CallGraph;

/**
 * 
 * @author antoniods311
 *
 */
public class GeneralFixtureDetector extends Thread {
	
private File xml;
	
	public GeneralFixtureDetector(File xml, CallGraph callGraph){
		this.xml = xml;
	}

	@Override
	public void run(){
		analyze();
	}
	
	public double analyze() {
		// TODO Auto-generated method stub
		return 0;
	}

}
