package com.lxr.pay.wxpay;

public class WXConfig {
	
	public static final String API_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	public static final String API_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	public static final String API_CLOSEORDER = "https://api.mch.weixin.qq.com/pay/closeorder";
	
	/**这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全*/
	//public static final String PARTNERKEY = "12345678998765432112345677654321";
	//应用id
	public String APPID = null;
	//应用秘钥
	public String APPSECRET = null;
		
		/*商户号*/
	public String MCHID = null;
		/**这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全*/
	public String PARTNERKEY = null;
	
	public String NOTFIY_URL = null;
	
	
}
