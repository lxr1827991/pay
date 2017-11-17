package com.lxr.pay.alipay;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.lxr.commons.exception.ApplicationException;

import com.lxr.pay.wxpay.WxpayFactory;

public class AlipayFactory {

	public static Alipay getPay(String xmlPath) {
		try {
			return new Alipay(getXmlConfig(xmlPath));
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		
	}
	
	
	public static AlipayConfigurator getXmlConfig(String path) throws DocumentException {

		
		SAXReader reader = new SAXReader();  
		
		
	    Document   document = reader.read(WxpayFactory.class.getResourceAsStream(path)); 
	
	    Element root = document.getRootElement();
	    
	    if(!root.getName().equals("xml"))
	    	throw new ApplicationException("根标签"+root.getName()+"错误");
	    AlipayConfigurator config = new AlipayConfigurator(); 
	    config.appId = root.element("appid").getStringValue().trim();;
		config.appPublicKey = root.element("appPublicKey").getStringValue().trim();		
		config.appPrivateKey = root.element("appPrivateKey").getStringValue().trim();
		
		config.alipayPublicKey = root.element("alipayPublicKey").getStringValue().trim();
		config.notifyUrl = root.element("notifyUrl").getStringValue().trim();
	    
	    return config;
	    

	}
	

	
	
}
