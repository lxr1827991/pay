package com.lxr.pay.wxpay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.lxr.commons.exception.ApplicationException;



public class WxpayFactory {

	
	
	public static <T> T getPay(Class<T> cls,String p) {
		
		WXConfig config = getConfig(p);
		
		return getPay(cls, config);

	}
	
public static <T> T getPayByXml(Class<T> cls,String p) {
		
		WXConfig config;
		try {
			config = getXmlConfig(p);
		} catch (DocumentException e) {
			throw new ApplicationException(e);
		}
		
		return getPay(cls, config);

	}
	
public static <T> T getPay(Class<T> cls,WXConfig config) {
		
		
		
		WxpayContext.config = config;
		
		if(cls == WxJsapiPay.class)
			return (T) new WxJsapiPay(config);
		
		if(cls == WXNativePay.class)
			return (T) new WXNativePay(config);
		
		if(cls == WxAppPay.class)
			return (T) new WxAppPay(config);
		
		return null;

	}
	
	
	private static WXConfig getConfig(String path) {
		Properties properties = new Properties();
		try {
			properties.load(WxpayFactory.class.getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			throw new ApplicationException(e);
		}
		
		WXConfig config = new WXConfig(); 
		
		config.APPID = properties.getProperty("appid");
		config.APPSECRET = properties.getProperty("appsecret");
		config.MCHID = properties.getProperty("mchid");
		config.PARTNERKEY = properties.getProperty("parinerkey");
		config.NOTFIY_URL = properties.getProperty("notify_url");
		return config;

	}
	
	private static WXConfig getXmlConfig(String xmlPath) throws DocumentException {
		
		
		
		SAXReader reader = new SAXReader();  
		
		
	    Document   document = reader.read(WxpayFactory.class.getResourceAsStream(xmlPath)); 
	
	    Element root = document.getRootElement();
	    
	    if(!root.getName().equals("xml"))
	    	throw new ApplicationException("根标签"+root.getName()+"错误");
	    WXConfig config = new WXConfig(); 
	    config.APPID = root.element("appid").getStringValue().trim();;
		config.APPSECRET = root.element("appsecret").getStringValue().trim();
		config.MCHID = root.element("mchid").getStringValue().trim();
		config.PARTNERKEY = root.element("parinerkey").getStringValue().trim();
		config.NOTFIY_URL = root.element("notfiy_url").getStringValue().trim();
	    
	    return config;
	    

	}
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//InputStream inputStream = WxpayFactory.class.getResourceAsStream("/wxconfig.xml");
		
		WxAppPay appPay = WxpayFactory.getPayByXml(WxAppPay.class, "/wxconfig.xml");
	
		int i = 0;
	}

	
}
