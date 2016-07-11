/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicreator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EmptyStackException;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import xmlsaxparser.BaseElement;
import xmlsaxparser.UTIL;

/**
 *
 * @author Alexander
 */
public class GUICreatorOnSAX extends DefaultHandler {
	private Stack<BaseElement> stackOfElements = new Stack<BaseElement>();
	private BaseElement rootElement;
	private StringBuffer textContent = new StringBuffer();
	
	public BaseElement getRootElement() {
		return rootElement;
	}
	
        @Override
	public void characters(char[] buffer, int start, int length) {
		textContent.append(buffer, start, length);
	  }
	
        @Override
	public void endDocument() {
		System.out.println("Document ended");
	}

        @Override
	public void endElement(java.lang.String uri, java.lang.String localName, java.lang.String qName) {
		System.out.println("Element ended");
		try{
		stackOfElements.peek().setLabel(textContent.toString());
		}catch(UnsupportedOperationException ex){
			System.out.println("UnsupportedOperationException");
		}
		stackOfElements.pop();
	}

        @Override
	public void startDocument() {
		System.out.println("Document started");
	}

        @Override
	public void startElement(java.lang.String uri, java.lang.String localName, java.lang.String qName,
			Attributes attributes) {
		textContent.setLength(0);
		System.out.println(uri);
		try {
			System.out.println("Element started");
			System.out.println(qName);

			Class<?> currentClass=Class.forName(qName);
			
			BaseElement currentElement = (BaseElement) currentClass.newInstance();
			
			for (int i = 0; i < attributes.getLength(); i++) {
				Method setter=currentClass.getMethod("set"+attributes.getQName(i), new Class[]{Class.forName("java.lang.String")});
				setter.invoke(currentElement, attributes.getValue(i));
			}
			
			try{
			stackOfElements.peek().add(currentElement);
			}catch(EmptyStackException ex){
				rootElement=currentElement;
				System.out.println("Root element");
			}
			stackOfElements.push(currentElement);
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DefaultHandler handler = new GUICreatorOnSAX();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = null;
		try {
			parser = factory.newSAXParser();
			parser.parse("old.xml", handler);
			UTIL.draw(((GUICreatorOnSAX) handler).getRootElement(), "Example");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.xml.sax.SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
