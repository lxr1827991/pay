package com.example;

import com.lxr.pay.wxpay.WXConfig;
import com.lxr.pay.wxpay.WxAppPay;
import com.lxr.pay.wxpay.WxJsapiPay;
import com.lxr.pay.wxpay.WxpayContext;
import com.lxr.pay.wxpay.WxpayFactory;

public class wxController {
	
	
	
	
	
	@SuppressWarnings("unused")
	private void getWxpay() {
	
		WxJsapiPay jsapiPay = WxpayFactory.getPay(WxJsapiPay.class, "wxpay.properties");
		
		WXConfig config = WxpayContext.config;
		
		String o = null;
	}
	
	public static void main(String[] args) {
		new wxController().getWxpay();
	}

}
