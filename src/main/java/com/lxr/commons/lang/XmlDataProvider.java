package com.lxr.commons.lang;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XmlDataProvider extends AbstractDataProvider{
	
	public static final String ROOT_NAME = "root";
	
	public static final String ENTRY_NAME = "entry";
	
	boolean debug = true;
	
	Document doc;
	
	
	
	
	@Override
	public String getString(String expression) {
		String[] nodes = expression.split("\\.");
		try {
			
			String xpath ="/"+ROOT_NAME;
			for (int i=0;i<nodes.length;i++) {
				xpath+="/entry[@key='"+nodes[i]+"']";
			}
			if(debug)System.out.println(xpath);
				Node node = getDom().selectSingleNode(xpath);
				if(node!=null)return node.getStringValue();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private Document getDom() throws DocumentException {
		if(debug||doc==null){
		 SAXReader reader = new SAXReader();
         InputStream in = getClass().getClassLoader().getResourceAsStream("myConfig.xml");
         doc = reader.read(in);
		}
         return doc;
	}

	public static void main(String[] args) {
		String h = new XmlDataProvider().getString("safety_get.safety_getUserByCodeAndPwd");
		System.err.println(h);
		//InputStream in = XmlProvideInterface.class.getClassLoader().getResourceAsStream("myConfig.xml");
		//System.out.println(in);
	
	}
	

}
