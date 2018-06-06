package com.lxr.pay.wxpay.bean;

public class WxNativeOrder extends WxOrder{

	//
	String openid;
	
	//native支付
	String serverIp;
	//native支付
	String productId;
	
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	
	
}
