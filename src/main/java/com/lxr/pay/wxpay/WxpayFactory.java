package com.lxr.pay.wxpay;

import java.io.IOException;
import java.util.Properties;

import com.lxr.pay.ApplicationException;

public class WxpayFactory {

	
	
	public static <T> T getPay(Class<T> cls,String p) {
		
		WXConfig config = getConfig(p);
		
		WxpayContext.config = config;
		
		if(cls == WxJsapiPay.class)
			return (T) new WxJsapiPay(config);
		
		if(cls == WXNativePay.class)
			return (T) new WXNativePay(config);
		
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
		
		return config;

	}
	
}
