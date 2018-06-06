package com.lxr.pay.wxpay.bean;

public class WxJspaiOrder extends WxOrder{

	//
	String openid;
	//jsapi支付
	String userIp;

	
	
	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	
}
