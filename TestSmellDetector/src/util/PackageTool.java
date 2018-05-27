package util;

import java.util.ArrayList;

import javax.xml.soap.Node;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author antoniods311
 *
 */
public class PackageTool {

	/**
	 * 
	 * @param document
	 * @return a String which represents a package
	 */
	public static String constructPackage(Document doc) {

		String stringPackage = "";
		ArrayList<String> directories = new ArrayList<String>();
		NodeList packList = doc.getElementsByTagName(ToolConstant.PACKAGE);
		for (int i = 0; i < packList.getLength(); i++) {
			if (packList.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element packageElement = (Element) packList.item(i);
				NodeList firstNameList = packageElement.getChildNodes();
				for (int j = 0; j < firstNameList.getLength(); j++) {
					if (firstNameList.item(j).getNodeType() == Node.ELEMENT_NODE) {
						Element firstNameEl = (Element) firstNameList.item(j);
						if (firstNameEl.getNodeName().equals("name")) {
							NodeList dirNames = firstNameEl.getChildNodes();
							for (int k = 0; k < dirNames.getLength(); k++) {
								if (dirNames.item(k).getNodeType() == Node.ELEMENT_NODE) {
									Element dirEl = (Element) dirNames.item(k);
									if (dirEl.getNodeName().equals("name")) {
										// pack=pack+"."+dirEl.getTextContent();
										directories.add(dirEl.getTextContent());
									}
								}
							}
						}
					}
				}
			}
		}
		
		boolean isFirstDir = true;
		for(String dir : directories){
			if(isFirstDir){
				stringPackage = dir;
				isFirstDir = false;
			}else{
				stringPackage = stringPackage + ToolConstant.DOT + dir;
			}
		}

		return stringPackage;
	}

}
