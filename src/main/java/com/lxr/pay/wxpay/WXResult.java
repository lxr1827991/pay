package com.lxr.pay.wxpay;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;




/**
 * 用于生成和微信服务器交互的返回数据
 * @author lxr
 *
 */
public class WXResult {
	
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";
	
	 public static String getScanCallBackXmlResult(String return_code,String return_msg, String result_code,String err_code_des,Map<String, String> inputPars) {
		 SortedMap<String, String> result = new TreeMap<String,String>();
		 result.put("return_code", result_code);
		 result.put("return_msg", return_msg);
		 
		 result.put("return_code", result_code);
		 result.put("return_msg", return_msg);
		
		 
		 
		 return "";

	}
	
	 
	 public static String getScanCallBackXmlResult(Map<String, String> inputPars) {
			return getScanCallBackXmlResult(SUCCESS, "", SUCCESS, "", inputPars);

	}
	 
	public static String getScanCallBackXmlResult(String return_code,String return_msg) {
			return getScanCallBackXmlResult(return_code, return_msg, "", "", null);

	}

	
	
	/**
	 *微信支付通知 返回
	 * @param return_code
	 * @param return_msg
	 * @return
	 */
	public static String getPayNotifyResult(String return_code,String return_msg) {
		return "<xml>"
				+"<return_code><![CDATA["+return_code+"]]></return_code>"
				+" <return_msg><![CDATA["+return_msg+"]]></return_msg>"
			  	+"</xml>";

}
	
}
