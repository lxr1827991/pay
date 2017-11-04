package com.lxr.pay.wxpay;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import com.lxr.commons.https.HttpClientConnectionManager;
import com.lxr.pay.wxpay.utils.XmlUtils;


/**
 * 统一下单
 * @author Administrator
 *
 */
public class UnifiedOrder {

	
	/**
	 * 发起统一下单
	 * @param url https://api.mch.weixin.qq.com/pay/unifiedorder
	 * @param xmlParam 统一下单参数
	 * @return prepay_id 支付会话id
	 */
	public static String getPayNo(String url, String xmlParam) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient);
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,true);
		HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
		String prepay_id = "";
		try {
			httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			if (jsonStr.indexOf("FAIL") != -1) {
				return prepay_id;
			}
			Map map = XmlUtils.doXMLParse(jsonStr);
			String return_code = (String) map.get("return_code");
			prepay_id = (String) map.get("prepay_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepay_id;
	}


	

}