package util.eagertest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SourceClassAnalyzer {
	
	private File sourceXml;
	private List<String> classMethods;
	
	public SourceClassAnalyzer(File sourceXml){
		this.sourceXml = sourceXml;
		this.classMethods = new ArrayList<String>();
	}
	
	/*
	 * Controlla se il metodo passato come parametro
	 * è o meno un metodo della classe "sorgente",
	 * quella per la quale sono stati scritti i TC.
	 */
	public boolean isSourceMethod(String method){
		
		boolean isMethod = false;
		
		/*
		 * devo leggere l'xml e vedere se method
		 * appartiene a sourceXml.
		 */
	
		return isMethod;
	}
	
	/*
	 * restituisce tutti i metodi della classe
	 * sotto test.
	 */
	public List<String> getClassMethods(){
		
		
		return classMethods;
	}

	public File getSourceXml() {
		return sourceXml;
	}

	public void setSourceXml(File sourceXml) {
		this.sourceXml = sourceXml;
	}

}
