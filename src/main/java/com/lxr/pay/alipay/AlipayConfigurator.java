package com.lxr.pay.alipay;

public class AlipayConfigurator {
	
	
	
	// 商户appid
	public  String appId = null;
	//商户门店编号
	public String storeId = "";
	//
	public String sellerId="";

	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public  String notifyUrl = "";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public  String returnUrl = "";
	// 返回格式		FORMAT	参数返回格式，只支持json	json（固定）
	public  String format = "json";
	// 请求和签名使用的字符编码格式，支持GBK和UTF-8	开发者根据实际工程编码配置
	public  String charset = "utf-8";
	//应用私钥
	public  String appPrivateKey = null;

	//应用工钥
	public  String appPublicKey = null;

	// 支付宝公钥
	public  String alipayPublicKey = null;

	//商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	public  String signType = "RSA2";
	
}
