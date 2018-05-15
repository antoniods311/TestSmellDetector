package simian;

import java.util.ArrayList;

import com.harukizaemon.simian.CheckSummary;

public class SimianResult {
	
	private CheckSummary checkSummary;
	private ArrayList<BlockSet> clones;
	
	/**
	 * @param checkSummary
	 * @param clones
	 */
	public SimianResult(CheckSummary checkSummary, ArrayList<BlockSet> clones) {
		super();
		this.checkSummary = checkSummary;
		this.clones = clones;
	}
	/**
	 * @return the checkSummary
	 */
	public CheckSummary getCheckSummary() {
		return checkSummary;
	}
	/**
	 * @param checkSummary the checkSummary to set
	 */
	public void setCheckSummary(CheckSummary checkSummary) {
		this.checkSummary = checkSummary;
	}
	/**
	 * @return the clones
	 */
	public ArrayList<BlockSet> getSet() {
		return clones;
	}
	/**
	 * @param clones the clones to set
	 */
	public void setSet(ArrayList<BlockSet> clones) {
		this.clones = clones;
	}	
}
