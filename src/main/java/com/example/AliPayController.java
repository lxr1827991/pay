package com.example;

import com.lxr.pay.alipay.AliPrePay;
import com.lxr.pay.alipay.Alipay;
import com.lxr.pay.alipay.AlipayFactory;

public class AliPayController {

	Alipay alipay;
	
	static {
		//初始化支付类
		Alipay alipay  = AlipayFactory.getPay("/alipay.xml");
		
	}
	
	
	private String doPay() {
		//支付数据填充
		AliPrePay prePay = new AliPrePay();
		
		prePay.setSubject("余额充值");
		prePay.setBody("洪城停车余额充值");
		prePay.setTotalAmount("10");
		String string =  alipay.doAppPay(prePay);
		
		return string;

	}
	
	
}
