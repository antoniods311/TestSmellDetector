package callgraph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.wala.classLoader.ClassLoaderFactory;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.AnalysisOptions.ReflectionOptions;
import com.ibm.wala.ipa.callgraph.impl.AllApplicationEntrypoints;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.io.FileProvider;

import util.ToolConstant;

public class WalaCallGraphBuilder {

	private File exclusionFile;
	private AnalysisScope scope;
	private IClassHierarchy classHierarchy;
	private Iterable<Entrypoint> entrypoint;
	private AnalysisOptions options;
	private Properties properties;
	private AllApplicationEntrypoints allAppEntrypoints;
	private CallGraphBuilder builder;
	private CallGraph callGraph;
	private static Logger log;
	
	/**
	 * The class constructor
	 *
	 * @param jarInput
	 * @param walaPropertiesFile 
	 * @throws IOException
	 * @throws WalaException
	 * @throws InvalidClassFileException 
	 * @throws IllegalArgumentException 
	 */
	public WalaCallGraphBuilder(File jarInput, String exclusionFile, String walaPropertiesFile) throws IOException, WalaException, IllegalArgumentException{
		this.exclusionFile = new FileProvider().getFile(exclusionFile);
		scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(jarInput.getAbsolutePath(), this.exclusionFile);
		classHierarchy = ClassHierarchy.make(scope);
		entrypoint = Util.makeMainEntrypoints(scope, classHierarchy);
		options = new AnalysisOptions(scope,entrypoint);
		options.setReflectionOptions(ReflectionOptions.NONE);
		WalaProperties.setPropertyFileName(walaPropertiesFile);
		properties = WalaProperties.loadProperties();
		allAppEntrypoints = new AllApplicationEntrypoints(scope,classHierarchy);
		options.setEntrypoints(allAppEntrypoints);
		builder = Util.makeZeroCFABuilder(options, new AnalysisCache(), classHierarchy, scope);
		callGraph = null;
		log = LogManager.getLogger(WalaCallGraphBuilder.class.getName());
	}
	
	
	
	/**
	 * The class constructor
	 *
	 * @param jarInput
	 * @param walaPropertiesFile 
	 * @throws IOException
	 * @throws WalaException
	 * @throws InvalidClassFileException 
	 * @throws IllegalArgumentException 
	 */
	public WalaCallGraphBuilder(String binaryDirPath, String scopeLocation, String exclusionFile, String walaPropertiesFile) throws IOException, WalaException, IllegalArgumentException{
		this.exclusionFile = new FileProvider().getFile(exclusionFile);
		File scopeFile = createScopeFile(binaryDirPath,scopeLocation);
		scope = AnalysisScopeReader.readJavaScope(scopeFile.getAbsolutePath(), this.exclusionFile, ClassLoaderFactory.class.getClassLoader());
		classHierarchy = ClassHierarchy.make(scope);
		entrypoint = Util.makeMainEntrypoints(scope, classHierarchy);
		options = new AnalysisOptions(scope,entrypoint);
		options.setReflectionOptions(ReflectionOptions.NONE);
		WalaProperties.setPropertyFileName(walaPropertiesFile);
		properties = WalaProperties.loadProperties();
		allAppEntrypoints = new AllApplicationEntrypoints(scope,classHierarchy);
		options.setEntrypoints(allAppEntrypoints);
		builder = Util.makeZeroCFABuilder(options, new AnalysisCache(), classHierarchy, scope);
		callGraph = null;
		log = LogManager.getLogger(WalaCallGraphBuilder.class.getName());
	}
	
	
	/**
	 * This method creates scope file using binary directory content
	 * 
	 * @param binaryDirPath
	 * @param scopeLocation
	 * @return
	 */
	private File createScopeFile(String binaryDirPath, String scopeLocation) {
		
		File scopeFile = new File(scopeLocation+File.separatorChar+ToolConstant.SCOPE_FILE_NAME);
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(scopeFile.getAbsolutePath()), "utf-8"));
		    writer.write(ToolConstant.PRIMORDIAL_STD_LIB+"\n");
		    writer.write(ToolConstant.PRIMORDIAL_JAR_FILE+"\n");
		    writer.write(ToolConstant.APPLICATION_BIN_DIR+ binaryDirPath);
		    writer.close();
		} catch (IOException ex) {
		    log.info(ToolConstant.WRITING_SCOPE_FILE_ERROR);
		    ex.printStackTrace();
		}
			
		return scopeFile;
	}



	/**
	 * This method builds a call graph
	 * 
	 * @return callGraph
	 */
	public CallGraph buildCallGraph(){
		try {
			callGraph = builder.makeCallGraph(options, null);
		} catch (IllegalArgumentException e) {
			log.error(ToolConstant.WALA_ILLEGAL_ARG_ERROR);
			e.printStackTrace();
		} catch (CallGraphBuilderCancelException e) {
			log.error(ToolConstant.CALL_GRAPH_BUILDER_ERROR);
		}
		return callGraph;
	}
	
	/**
	 * 
	 * @return exclusionFile
	 */
	public File getExclusionFile() {
		return exclusionFile;
	}

	/**
	 * 
	 * @return AnalysisScope object
	 */
	public AnalysisScope getScope() {
		return scope;
	}

	/**
	 * 
	 * @return IClassHierarchy object
	 */
	public IClassHierarchy getClassHierarchy() {
		return classHierarchy;
	}

	public Iterable<Entrypoint> getEntrypoint() {
		return entrypoint;
	}

	public AnalysisOptions getOptions() {
		return options;
	}

	public Properties getProperties() {
		return properties;
	}

	public AllApplicationEntrypoints getAllAppEntrypoints() {
		return allAppEntrypoints;
	}

	public CallGraphBuilder getBuilder() {
		return builder;
	}

	public CallGraph getCallGraph() {
		return callGraph;
	}
	
	
	

}
