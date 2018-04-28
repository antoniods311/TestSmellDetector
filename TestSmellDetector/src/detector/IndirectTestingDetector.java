package detector;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import dataflowanalysis.CallSiteAnalyzer;
import dataflowanalysis.DataFlowMethodAnalyzer;
import util.TestMethodChecker;
import util.TestParseTool;
import util.ToolConstant;
import util.prodclass.ToolMethodType;
import util.tooldata.ToolData;

/**
 * 
 * @author antoniods311
 *
 */
public class IndirectTestingDetector extends Thread {

	private ToolData data;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	private TestMethodChecker testChecker;
	private static Logger log;
	private DataFlowMethodAnalyzer methodAnalyzer;
	private HashSet<ToolMethodType> testedMethods;
	private HashSet<ToolMethodType> notTestedMethods;

	public IndirectTestingDetector(ToolData data) {
		this.data = data;
		this.testChecker = new TestMethodChecker();
		this.testedMethods = new HashSet<ToolMethodType>();
		this.notTestedMethods = new HashSet<ToolMethodType>();
		log = LogManager.getLogger(IndirectTestingDetector.class.getName());
	}

	private void analyze(File xml) {
		docbuilderFactory = DocumentBuilderFactory.newInstance();

		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName(ToolConstant.FUNCTION);
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element functionElement = (Element) list.item(i);
					// Se entro ho trovato un metodo di test
					if (testChecker.isTestMethod(functionElement)) {
						String methodName = TestParseTool.readMethodNameByFunction(functionElement);
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
								methodAnalyzer = new DataFlowMethodAnalyzer(node);

								/*
								 * 1. Analisi normale: recupero i metodi delle
								 * PC testati dal metodo di test ottenendo [A]
								 * 2. Per il metodo di test faccio analisi sui
								 * call-site: controllo se l'i-esimo elemento di
								 * [A] è il metodo chiamato nel
								 * CallSiteReference. In questo caso mi prendo
								 * la classe del metodo chiamato, costruisco un
								 * ToolMethodType e lo aggiungo a un
								 * HashSet<ToolMethodType> [B] --> [B] deve
								 * essere lo stesso per tutti i metodi di test,
								 * non devo averne "n" ma uno solo.
								 */

								/*
								 * 1. Recupero i metodi della PC testati dal
								 * test method
								 */
								HashSet<String> testedMethodsNames = methodAnalyzer.getPCMethodsTestedByTestMethod(data,
										methodName);

								/*
								 * 2. Analisi sui call-sites
								 */
								CallSiteAnalyzer callSiteAnalyzer = new CallSiteAnalyzer(data, node);
								testedMethods = callSiteAnalyzer.analyzeCallSite(testedMethods, testedMethodsNames);

							}
						}
					}
				}
			}

		} catch (ParserConfigurationException e) {
			log.error(ToolConstant.PARSE_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (SAXException e) {
			log.error(ToolConstant.SAX_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(ToolConstant.IO_EXCEPTION_MSG);
			e.printStackTrace();
		}
	}

	
	/**
	 * @return indirect tested methods
	 */
	private HashSet<ToolMethodType> chekIndirectTesting() {

		HashSet<ToolMethodType> indirectTestedMethods = new HashSet<ToolMethodType>();

		for (ToolMethodType tmt : notTestedMethods) {
			Iterator<CGNode> iter = data.getCallGraph().iterator();
			while (iter.hasNext()) {
				CGNode node = iter.next();
				IMethod iMethod = node.getMethod();
				MethodReference methodRef = iMethod.getReference();
				TypeReference typeRef = methodRef.getDeclaringClass();
				ClassLoaderReference classLoaderRef = typeRef.getClassLoader();

				if (classLoaderRef.getName().toString().equalsIgnoreCase(ToolConstant.APPLLICATION_CLASS_LOADER)) {
					Iterator<CallSiteReference> csi = node.iterateCallSites();
					while (csi.hasNext()) {
						CallSiteReference csr = csi.next();
						String className = csr.getDeclaredTarget().getDeclaringClass().getName().getClassName()
								.toString();
						String methodName = csr.getDeclaredTarget().getName().toString();
						
						if (tmt.getClassType().equals(className) && tmt.getMethodName().equals(methodName)) {

							/*
							 * Se entro in questo if ho trovato una chiamata al
							 * metodo non testato. Ora devo vedere se il
							 * chiamante (metodo rappresentato dal node) è un
							 * metodo testato o meno. Nel caso sia testato ho un
							 * Indirect Testing
							 */
							String callerMethod = iMethod.getName().toString();
							String callerClass = iMethod.getDeclaringClass().getName().getClassName().toString();
							ToolMethodType indTestedMethod = new ToolMethodType(callerClass, callerMethod);
							if(testedMethods.contains(indTestedMethod))
								indirectTestedMethods.add(indTestedMethod);

						}
					}
				}
			}
		}

		return indirectTestedMethods;
	}

	/**
	 * Calculate difference set: {production classes methods} - {tested methods}
	 */
	private void computeDifferenceSet() {

		/*
		 * Modifico le informazioni sui production methods mantenendo solo il
		 * nome della classe e quindi rimuovendo il path
		 */

		HashSet<ToolMethodType> customPCMethods = customizePCMethodsSet(data.getProductionMethods());

		/*
		 * Calcolo i metodi non testati
		 */
		for (ToolMethodType tmt : customPCMethods) {
			if (!testedMethods.contains(tmt)) {
				notTestedMethods.add(tmt);
			}
		}

		// for(ToolMethodType t : testedMethods)
		// System.out.println("metodo testato: "+t);
		// System.out.println("----------------------------------------------------------------------");

//		for (ToolMethodType ntm : notTestedMethods)
//			System.out.println("metodo non testato: " + ntm);

	}

	/**
	 * @param productionMethods
	 * @return
	 */
	private HashSet<ToolMethodType> customizePCMethodsSet(HashSet<ToolMethodType> productionMethods) {

		HashSet<ToolMethodType> customPCMethods = new HashSet<ToolMethodType>();
		for (ToolMethodType tmt : productionMethods) {
			/*
			 * rimuovo il path da ogni class e poi aggiungo il nuovo elemento a
			 * customPCMethods
			 */
			String completePath = tmt.getClassType();
			StringTokenizer tokenizer = new StringTokenizer(completePath, "/");
			String last = "";
			while (tokenizer.hasMoreTokens())
				last = tokenizer.nextToken();

			String className = "";
			tokenizer = new StringTokenizer(last, ".");
			if (tokenizer.hasMoreElements())
				className = tokenizer.nextToken();

			customPCMethods.add(new ToolMethodType(className, tmt.getMethodName()));
		}

		return customPCMethods;
	}

	@Override
	public void run() {
		log.info("*** START INDIRECT TESTING ANALYSIS ***");
		;
		for (File file : data.getTestClasses())
			this.analyze(file);

		computeDifferenceSet();
		HashSet<ToolMethodType> indirectTestedMethod = chekIndirectTesting();
		if(indirectTestedMethod.size() > 0)
			log.info("Indirect Tested Methods Found: "+indirectTestedMethod.size());

		log.info("*** END INDIRECT TESTING ANALYSIS ***\n");
	}
}
