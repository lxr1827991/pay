package com.lxr.pay.wxpay;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.commons.https.HttpClientConnectionManager;
import com.lxr.pay.wxpay.bean.WxOrder;
import com.lxr.pay.wxpay.utils.MessageXMLUtil;
import com.lxr.pay.wxpay.utils.RequestHandler;
import com.lxr.pay.wxpay.utils.XmlUtils;




public class WXPay {
	
	
	public static final String API_UNIFIEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	public static final String API_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	public static final String API_CLOSEORDER = "https://api.mch.weixin.qq.com/pay/closeorder";

	
	public static final String STATE_SUCCESS = "SUCCESS";
	
	public static final String STATE_FAIL = "FAIL";
	
	public static final String TRADE_TYPE_JSAPI = "JSAPI";
	
	public static final String TRADE_TYPE_NATIVE = "NATIVE";
	
	public static final String TRADE_TYPE_APP = "APP";
	
	
	
	WXConfig wxConfig;
	
	public WXPay(WXConfig config) {
		this.wxConfig = config;
	}
	
	public WXConfig getWxConfig() {
		return wxConfig;
	}





	public void setWxConfig(WXConfig wxConfig) {
		this.wxConfig = wxConfig;
	}



	/**
	 * jsapi统一下单并返回jsapi支付参数
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> unifiedOrder(WxOrder order,String trade_type) throws UnifiedorderException {
		
		
		
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", wxConfig.APPID);
		packageParams.put("mch_id", wxConfig.MCHID);
		packageParams.put("nonce_str", WxUtil.createNonceStr());
		packageParams.put("body", order.getContent());
		packageParams.put("out_trade_no", order.getOutTradeNo());
		packageParams.put("total_fee", WxUtil.yuan2fen(order.getTotalFee())+"");
		packageParams.put("trade_type", trade_type);
		
		
		if(order.getAttach()!=null)
			packageParams.put("attach", order.getAttach());
		
		if(order.getNotifyUrl()==null) order.setNotifyUrl(getWxConfig().NOTFIY_URL);
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = order.getNotifyUrl();
		packageParams.put("notify_url", notify_url);
		
		
		onUnifiedOrder(packageParams,order);
		
		
		try {
			String allParameters = WxUtil.genPackage(packageParams, wxConfig.PARTNERKEY);
		
			Map<String, String> map = XmlUtils.doXMLParse(post(API_UNIFIEDORDER, allParameters));
			
			if(!STATE_SUCCESS.equals(map.get("return_code")))
				throw new UnifiedorderException("请求支付失败","请求："+allParameters+"，返回："+map+"");
			return map;
		}catch (UnifiedorderException e) {
				throw e;
		} catch (Exception e) {
			throw new UnifiedorderException(e);
		}

	
	}
	
	
	public boolean isHandleNotify(Map<String, String> notifyMap) {
		if(wxConfig.APPID.equals(notifyMap.get("appid")))
		return true;
		
		return false;
		
	}
	
	
	
	protected void onUnifiedOrder(Map<String, String> map,WxOrder order) {
		

	}
	
	
	
	
	/**
	 * 
	 * @param transaction_id 微信订单号
	 * @param out_trade_no 商户订单号
	 * @return
	 */
	public WxOrderQuery queryOrder(String transaction_id,String out_trade_no) {
		SortedMap<String, String> queryParam = new TreeMap<String, String>();
		queryParam.put("appid", wxConfig.APPID);
		queryParam.put("mch_id", wxConfig.MCHID);
		if(transaction_id!=null)
		queryParam.put("transaction_id",transaction_id );
		if(out_trade_no!=null)
		queryParam.put("out_trade_no", out_trade_no);
		queryParam.put("nonce_str", WxUtil.createNonceStr());
		queryParam.put("sign_type", "MD5");
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(wxConfig.APPID, wxConfig.APPSECRET, wxConfig.PARTNERKEY);
			try {
				String allParameters = reqHandler.genPackage(queryParam);
				String xml = post(API_ORDERQUERY, allParameters);
				WxOrderQuery query = new WxOrderQuery();
				query.result=XmlUtils.doXMLParse(xml);
				return query;
			} catch (Exception e) {
				throw new ApplicationException(e);
			}
			

	}
	
	
	/**
	 * 关闭订单
	 * @param out_trade_no 商户订单号
	 */
	public void closeOrder(String out_trade_no) {
		SortedMap<String, String> queryParam = new TreeMap<String, String>();
		queryParam.put("appid", wxConfig.APPID);
		queryParam.put("mch_id", wxConfig.MCHID);
		queryParam.put("out_trade_no", out_trade_no);
		queryParam.put("nonce_str", WxUtil.createNonceStr());
		queryParam.put("sign_type", "MD5");
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(wxConfig.APPID, wxConfig.APPSECRET, wxConfig.PARTNERKEY);
		try {
				String allParameters = reqHandler.genPackage(queryParam);
				String xml = post(API_CLOSEORDER, allParameters);
				Map<String, String> result =XmlUtils.doXMLParse(xml);
				
				if(!STATE_SUCCESS.equals(result.get("result_code")))
					throw new ApplicationException(result.get("result_msg"));
				
			} catch (Exception e) {
				throw new ApplicationException(e);
			}
			

	}
	
	
	/**
	 * 
	 * @param transaction_id 微信订单号
	 * @param out_trade_no 商户订单号
	 */
	public void refund(String transaction_id,String out_trade_no) {

	}
	
	
	/**
	 * 
	 * @param transaction_id 微信订单号
	 * @param out_trade_no 商户订单号
	 * @param out_refund_no 商户退款单号
	 * @param refund_id 微信退款单号
	 */
	public void queryRefund(String transaction_id,String out_trade_no,String out_refund_no,String refund_id) {
		
	}

	
	
	
	
	 @SuppressWarnings("resource")
	protected String post(String api,String content) throws ParseException, IOException {
		 DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient);
			DefaultHttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,true);
			HttpPost httpost = HttpClientConnectionManager.getPostMethod(api);
			httpost.setEntity(new StringEntity(content, "UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			return EntityUtils.toString(response.getEntity(), "UTF-8");
			
	}
	 
	 
	 public WxPayNotify getPayNotify(InputStream in) throws Exception{
		 return getPayNotify(MessageXMLUtil.parseXml(in)); 
		 
	 }
	 
	 public WxPayNotify getPayNotify(String xml) throws Exception{
		 return getPayNotify(WxUtil.getResult(xml)); 
	 }
	 
	 public WxPayNotify getPayNotify(Map<String, String> notify) throws Exception{
		 String return_code = notify.get("return_code").toString();
			if(!WXResult.SUCCESS.equals(return_code))
				throw new Exception(""+notify.get("return_msg"));
			
		
		
		SortedMap<String, String> sortedMap;
		
		if(notify instanceof SortedMap)
			sortedMap = (SortedMap)notify;
			else sortedMap = new TreeMap<>(notify);
			
			String sign = notify.remove("sign").toString();
			String createSign = WxUtil.createSign(sortedMap, wxConfig.PARTNERKEY);
			System.out.println("签名："+createSign+","+wxConfig.PARTNERKEY);
		if(!createSign.equals(sign))
			throw new ApplicationException("签名错误");
		return new WxPayNotify(sortedMap);

	}
	 
	 
	 public class WxPayNotify{
		  
		 Map<String, String> result;
		 
		 public WxPayNotify(Map<String, String> result) throws Exception {
				String return_code = result.get("return_code").toString();
				if(!WXResult.SUCCESS.equals(return_code))
					throw new Exception(""+result.get("return_msg"));
				this.result = result;
		}
		 
		 
		 public boolean isSuccess() {
			 return STATE_SUCCESS.equals(result.get("result_code"));

		}
		 
		 
		 
		 public String getErrMsg() {
			return result.get("err_code")+","+result.get("err_code_des");

		}
		 
		 
		 public String getAttach() {
			 return result.get("attach");

		}
		 
		 
		 public String getTradeType() {
				return result.get("trade_type");

			}
		 
		 public int getTotalFee() {
			  return Integer.parseInt(result.get("total_fee"));
		}
		 
		 public String getTransactionId(){
			 return result.get("transaction_id");
		 }
		 
		 public String getOutTradeNo(){
			 return result.get("out_trade_no");
		 }
		 
		 public String getTimeEnd(){
			 return result.get("time_end");
		 }
		 
		 public Map<String, String> getResult() {
			return result;
		}
	 }
	 
	 

	 
	 public class WxOrderQuery {
			
			Map<String, String> result;

			public boolean isTradeSuccess() {
				return STATE_SUCCESS.equals(result.get("trade_state"));

			}
			
			public String getOutTradeNo() {
				 return result.get("out_trade_no");
				
			}
			
			public String getTradeType() {
				if(STATE_FAIL.equals(result.get("return_code")))
					throw new ApplicationException(getErrMsg());
				return result.get("trade_type");
			}
			
			public String getTradeState() {
				if(STATE_FAIL.equals(result.get("return_code")))
					throw new ApplicationException(getErrMsg());
				return result.get("trade_state");

			}
			
			 public String popularTradeState() {
					switch (getTradeState()) {
					case "SUCCESS":
						return "支付成功";
					case "REFUND":
						return "转入退款";		
					case "NOTPAY":
						return "未支付";	
					case "CLOSED":
						return "已关闭";	
					case "REVOKED":
						return "已撤销（刷卡支付）";	
					case "USERPAYING":
						return "用户支付中";
					case "PAYERROR":
						return "支付失败(其他原因，如银行返回失败)";
					default:
						throw new ApplicationException("未知支付类型");
					}

				}
			
			public String getTotalFee() {
				
				return result.get("total_fee");

			}
			
			
			
			public String getErrMsg() {
				if(STATE_FAIL.equals(result.get("return_code")))
					return "return_msg:"+result.get("return_msg");
				
				if(STATE_FAIL.equals(result.get("result_code")))
					return "err_code:"+result.get("err_code")+",err_code_des:"+result.get("err_code_des");
				
					return "交易状态:"+result.get("trade_state");

			}
			
		}
}
