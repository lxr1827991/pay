package com.lxr.pay.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.lxr.commons.exception.ApplicationException;
import com.lxr.pay.HttpUtils;

public class WxOAuthUtils {

	
	public final static String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	
	public final static String USERIFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	
	
	public static JSONObject requestToken(String appid,String secret,String code) {
		
		JSONObject ret = JSONObject.parseObject(HttpUtils.getString(TOKEN_URL+"?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code"));
		
		if(ret.getString("errcode")!=null)
			throw new ApplicationException(ret.toString());
		
		return ret;
		
	}
	
	public static JSONObject requestUserinfo(String accessToken,String openid) {
		
		JSONObject ret = JSONObject.parseObject(HttpUtils.getString(USERIFO_URL+"?access_token="+accessToken+"&openid="+openid+"&lang=zh_CN"));
		
		if(ret.getString("errcode")!=null)
			throw new ApplicationException(ret.toString());
		
		return ret;
		
	}
	
	public static void main(String[] args) {
JSONObject ret = JSONObject.parseObject("{}");
		
		if(ret.getString("errcode")!=null)
			throw new ApplicationException(ret.toString());
	}
	
}
