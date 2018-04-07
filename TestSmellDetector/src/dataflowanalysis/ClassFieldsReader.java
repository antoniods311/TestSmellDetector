package dataflowanalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IField;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import util.ToolConstant;
import util.tooldata.ToolData;


/**
 * 
 * @author antoniods311
 *
 */
public class ClassFieldsReader {

	private String methodName;
	private ToolData data;

	/**
	 * @param data
	 */
	public ClassFieldsReader(ToolData data, String methodName) {
		super();
		this.data = data;
		this.methodName = methodName;
	}
	
	/**
	 * 
	 * @return method's class fields
	 */
	public HashSet<String> getClassFields(){
		
		HashSet<String> fields = new HashSet<String>();
		CGNode node;
		Iterator<CGNode> iter = data.getCallGraph().iterator();
		while (iter.hasNext()) {
			node = iter.next();
			IMethod iMethod = node.getMethod();
			MethodReference methodRef = iMethod.getReference();
			TypeReference typeRef = methodRef.getDeclaringClass();
			ClassLoaderReference classLoaderRef = typeRef.getClassLoader();

			if (classLoaderRef.getName().toString()
					.equalsIgnoreCase(ToolConstant.APPLLICATION_CLASS_LOADER)
					&& iMethod.getName().toString().equalsIgnoreCase(methodName)) {
												
				IClass declaringClass = iMethod.getDeclaringClass();	
				Collection<IField> allFields = declaringClass.getAllFields();  //prendo i fields della classe di test
				for(IField field : allFields)
					fields.add(field.getName().toString());
			}
		}
		
		
		
		
		
		return fields;
	}
	
	
	
}
