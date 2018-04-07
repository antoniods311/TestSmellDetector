package dataflowanalysis;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import util.ToolConstant;

public class SetUpMethodAnalyzer {

	private File xml;
	private DocumentBuilderFactory docbuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document doc;
	
	public SetUpMethodAnalyzer(File xml){
		this.xml = xml;
	}
	
	/**
	 * 
	 * @return a set of created object names
	 */
	public HashSet<String> getCreatedSet(){
		
		HashSet<String> createdSet = new HashSet<String>();
		
		docbuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = docbuilderFactory.newDocumentBuilder();
			doc = documentBuilder.parse(xml);
			doc.getDocumentElement().normalize();
			
			/*
			 *  far ricevere un parametro a questo metodo. Il
			 *  parametro deve essere un element che sia
			 *  un metodo di setUp in modo da poterlo analizzare 
			 */
			
		} catch (ParserConfigurationException e) {
			System.out.println(ToolConstant.PARSE_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println(ToolConstant.SAX_EXCEPTION_MSG);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(ToolConstant.IO_EXCEPTION_MSG);
			e.printStackTrace();
		}
		return createdSet;
	}
	
}
