package com.lxr.pay.alipay;



public class AlipayFactory {

	public Alipay getPay(String path) {
		return new Alipay(getConfig(path));

		
	}
	
	
	public AlipayConfigurator getConfig(String path) {
		return null;

	}
	
	
}
