package translator;

import java.io.File;

/**
 * 
 * @author antoniods311
 *
 */
public interface Translator {

	public File translate();
	public int load(File file);
	
}
