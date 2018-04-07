package dataflowanalysis;

import java.util.ArrayList;
import java.util.Collection;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IField;
import com.ibm.wala.classLoader.IMethod;


/**
 * 
 * @author antoniods311
 *
 */
public class ClassFieldsReader {

	private IMethod method;

	/**
	 * @param data
	 */
	public ClassFieldsReader(IMethod method) {
		super();
		this.method = method;
	}
	
	/**
	 * 
	 * @return method's class fields
	 */
	public ArrayList<String> getClassFields(){
		
		ArrayList<String> fields = new ArrayList<String>();
		IClass declaringClass = method.getDeclaringClass();
		Collection<IField> allFields = declaringClass.getAllFields();  //prendo i fields della classe di test
		for(IField field : allFields)
			fields.add(field.getName().toString());
		
		return fields;
	}
	
	
	
}
