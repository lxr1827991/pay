package com.example;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.pay.alipay.AliPrePay;
import com.lxr.pay.alipay.Alipay;
import com.lxr.pay.alipay.AlipayFactory;
import com.lxr.pay.alipay.AlipayNotify;

public class AliPayController {

	static Alipay alipay;
	
	static {
		//初始化支付类
		alipay  = AlipayFactory.getPay("/alipay.xml");
		
	}
	
	
	private String doPay() {
		//支付数据填充
		AliPrePay prePay = new AliPrePay();
		
		prePay.setSubject("余额充值");
		prePay.setBody("洪城停车余额充值");
		prePay.setTotalAmount(10.0);
		String string =  alipay.doAppPay(prePay);
		
		return string;

	}
	
	private String notify(HttpServletRequest request) {

		try {
			//获取所有通知
			Map<String, String> param = getParamMap(request);
			System.out.println("alipay notify"+param);
		if(!alipay.notifyCheck(param))
			throw new ApplicationException("签名错误");
		
		AlipayNotify notify = new AlipayNotify(param);
			//支付是否成功
			if(!notify.isSuccess()){
				//确认支付订单
				//rechargeService.doAlipayConfirm(notify.getOutTradeNo(),2, null, null);
				
			}else {
				//支付失败
				//rechargeService.doAlipayConfirm(notify.getOutTradeNo(),1, notify.getPayTime()+"", notify.getTradeNo());
				
			}
			

		
			
		}catch (ApplicationException e) {
			
			
			return "fail";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}finally {
			
		}
		
	
		
		return "success";

	}
	
	public static void main(String[] args) {
		System.out.println(new AliPayController().doPay());
	}
	
	
public Map<String, String> getParamMap(HttpServletRequest request) {
		
		Map<String, String[]> map = request.getParameterMap();
		
		Map<String, String> map2 = new HashMap<>();
		
		for (Map.Entry<String, String[]> entry : map.entrySet()) {  
			  
		    if(entry.getValue()!=null&&entry.getValue().length>0)
		    	map2.put(entry.getKey(), entry.getValue()[0]);
		  
		}
		
		
		return map2;

	}
}
