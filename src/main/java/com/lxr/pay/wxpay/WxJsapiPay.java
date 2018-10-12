package com.lxr.pay.wxpay;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.lxr.commons.exception.ApplicationException;
import com.lxr.commons.https.HttpClientConnectionManager;
import com.lxr.pay.wxpay.bean.WxJspaiOrder;
import com.lxr.pay.wxpay.bean.WxOrder;
import com.lxr.pay.wxpay.utils.Sha1Util;




/**
 * @see https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_4
 * @author Administrator
 *
 */
public class WxJsapiPay extends WXPay{
	
	

	
	public WxJsapiPay(WXConfig config) {
		super(config);
	}
	
	/**
	 * 获取jsapi支付参数
	 * @param preOrder
	 * @return
	 * @throws UnifiedorderException
	 */
	public Map<String, String> getJspaiPayParam(WxJspaiOrder preOrder)throws UnifiedorderException {
		Map<String, String> result = super.unifiedOrder(preOrder, TRADE_TYPE_JSAPI);
		
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		//finalpackage.put("prepay_id", prepay_id);//这个数据不参与生成支付参数
		finalpackage.put("appId", wxConfig.APPID);
		finalpackage.put("timeStamp", Sha1Util.getTimeStamp());
		finalpackage.put("nonceStr", WxUtil.createNonceStr());
		finalpackage.put("package", "prepay_id=" +result.get("prepay_id"));
		finalpackage.put("signType", "MD5");
		finalpackage.put("paySign", WxUtil.createSign(finalpackage, wxConfig.PARTNERKEY));
		return finalpackage;
	}
   
	
	@Override
	protected void onUnifiedOrder(Map<String, String> map,WxOrder order) {
		
		map.put("openid", ((WxJspaiOrder)order).getOpenid());
		map.put("spbill_create_ip", ((WxJspaiOrder)order).getUserIp());
	}
	
	
	@Override
	public boolean isHandleNotify(Map<String, String> notifyMap) {
		// TODO Auto-generated method stub
		if(!super.isHandleNotify(notifyMap))
			return false;
		return TRADE_TYPE_JSAPI.equals(notifyMap.get("trade_type"));
	}
	
	/**
	 * 
	 * @param code 用户受权后微信返回的code
	 * 正确返回{ "access_token":"ACCESS_TOKEN",    
			 "expires_in":7200,    
			 "refresh_token":"REFRESH_TOKEN",    
			 "openid":"OPENID",    
			 "scope":"SCOPE" } 
			 错误时微信会返回JSON数据包如下（示例为Code无效错误）:
			{"errcode":40029,"errmsg":"invalid code"} 
	 */
	public String getOpenId(String code) {
		JSONObject rest = WxOAuthUtils.requestToken(wxConfig.APPID, wxConfig.APPSECRET, code);
				
		return rest.getString("openid");
	
		}
	
	public JSONObject getAccessToken(String code) {
		JSONObject rest = WxOAuthUtils.requestToken(wxConfig.APPID, wxConfig.APPSECRET, code);
				
		return rest;
	
		}
	
	
	/**	@param
	 * @param state重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 * @return 
	 * @throws UnsupportedEncodingException 
	 */
	public  String getWeiXinOAuthUrl(String redirectUrl,String state){
	
		try {
			 //scope 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
			return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+wxConfig.APPID+"&redirect_uri="+URLEncoder.encode(redirectUrl, "utf-8")+"&response_type=code&scope=snsapi_base&state="+state+"#wechat_redirect";
		} catch (UnsupportedEncodingException e) {
			throw new ApplicationException(e);
		}
	}
	

	 

	
}
