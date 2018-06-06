package com.example;

import com.lxr.pay.alipay.AlipayFactory;
import com.lxr.pay.wxpay.WXConfig;
import com.lxr.pay.wxpay.WxAppPay;
import com.lxr.pay.wxpay.WxJsapiPay;
import com.lxr.pay.wxpay.WxpayFactory;

public class wxController {
	
	
	
	
	
	@SuppressWarnings("unused")
	private void getWxpay() {
	
		WxJsapiPay jsapiPay = WxpayFactory.getPay(WxJsapiPay.class, "wxpay.properties");
		
		
		String o = null;
	}
	
	
	private void getAlipay() {
		

	}
	
	public static void main(String[] args) {
		new wxController().getWxpay();
	}

}
