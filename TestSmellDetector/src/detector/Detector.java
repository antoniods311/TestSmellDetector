package detector;

/**
 * 
 * @author antoniods311
 *
 */
public interface Detector extends Runnable{

	public void run();
	public double analyze();
	
}
