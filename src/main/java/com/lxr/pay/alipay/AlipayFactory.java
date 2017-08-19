package com.lxr.pay.alipay;

import com.lxr.commons.utils.BeanUtils;

public class AlipayFactory {

	public Alipay getPay(String path) {
		return new Alipay(getConfig(path));

		
	}
	
	
	public AlipayConfigurator getConfig(String path) {
		return null;

	}
	
	
}
