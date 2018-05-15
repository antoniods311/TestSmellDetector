package simian;

import java.util.ArrayList;

import com.harukizaemon.simian.Block;

public class BlockSet {

	private ArrayList<Block> clones;

	/**
	 * @param clones
	 */
	public BlockSet(ArrayList<Block> clones) {
		super();
		this.clones = clones;
	}

	/**
	 * @return the clones
	 */
	public ArrayList<Block> getClones() {
		return clones;
	}

	/**
	 * @param clones the clones to set
	 */
	public void setClones(ArrayList<Block> clones) {
		this.clones = clones;
	}
	
	
}
