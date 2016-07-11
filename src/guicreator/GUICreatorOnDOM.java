/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicreator;

import java.awt.Color;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import xmlsaxparser.BaseElement;
import xmlsaxparser.UTIL;

/**
 *
 * @author Alexander
 */
public class GUICreatorOnDOM {

	private static void buildStructure(BaseElement root, Element rootElem) {
		
		try {
			root.setBounds(Integer.parseInt(rootElem.getAttribute("X")), Integer.parseInt(rootElem.getAttribute("Y")),
					Integer.parseInt(rootElem.getAttribute("W")), Integer.parseInt(rootElem.getAttribute("H")));
			
			try {
				root.setBackground(Color.decode(rootElem.getAttribute("COLOR")));
			} catch (NumberFormatException ex) {
				System.out.println("No Color");
			}
			
			try {
				root.setLabel(rootElem.getTextContent());
			} catch (UnsupportedOperationException e) {
				System.out.println("UnsupportedOperationException" + root.getClass());
			}
			
			for (int i = 0; i < rootElem.getChildNodes().getLength(); i++) {
				Node currentNode = rootElem.getChildNodes().item(i);
				if (currentNode instanceof Element) {
					BaseElement currentElement = (BaseElement) Class.forName(currentNode.getNodeName()).newInstance();
					buildStructure(currentElement, (Element) currentNode);
					root.add(currentElement);
				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse("old.xml");
			
			Element root = document.getDocumentElement();
			Object rootElem = Class.forName(root.getTagName()).newInstance();
			buildStructure((BaseElement) rootElem, root);
			
			if (rootElem instanceof BaseElement)
				UTIL.draw((BaseElement) rootElem, "Ex");
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.xml.sax.SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
