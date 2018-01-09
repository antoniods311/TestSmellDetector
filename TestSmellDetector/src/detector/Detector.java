package detector;

public interface Detector extends Runnable{

	public void run();
	public int analyze();
	
}
