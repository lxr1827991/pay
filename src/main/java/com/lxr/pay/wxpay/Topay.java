package com.lxr.pay.wxpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lxr.commons.utils.RequestHandler;
import com.lxr.commons.utils.Sha1Util;
import com.lxr.commons.utils.TenpayUtil;
import com.lxr.commons.utils.TypeConverUtil;



public class Topay extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8796105436399720136L;
	
	/**
	 * 获取微信支付临时票据
	 * 生成签名之前必须先了解一下jsapi_ticket，jsapi_ticket是公众号用于调用微信JS接口的临时票据。
	 * 正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取。
	 * 由于获取jsapi_ticket的api调用次数非常有限，频繁刷新jsapi_ticket会导致api调用受限，影响自身业务，开发者必须在自己的服务全局缓存jsapi_ticket 。
	 * 参考以下文档获取access_token（有效期7200秒，开发者必须在自己的服务全局缓存access_token）：http://mp.weixin.qq.com/wiki/15/54ce45d8d30b6bf6758f68d2e95bc627.html
	 * 用第一步拿到的access_token 采用http GET方式请求获得jsapi_ticket（有效期7200秒，开发者必须在自己的服务全局缓存jsapi_ticket）：https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
	 * @return
	 */
	public static String WxJsTicket() {
		return null;
	}
	
	/**
	 * 获取JSAPI签名
	 * 
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static Map<String, String> WxJsApiCheck(String url) {

		String jsapi_ticket = WXTicket.getTicket();
		Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";
        
        String[] paramArr = new String[] { "jsapi_ticket=" + jsapi_ticket,
                "timestamp=" + timestamp, "noncestr=" + nonce_str, "url=" + url };
        Arrays.sort(paramArr);
        
        string1 = paramArr[0].concat("&"+paramArr[1]).concat("&"+paramArr[2])
                .concat("&"+paramArr[3]);
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            // 对拼接后的字符串进行 sha1 加密
            byte[] digest = crypt.digest(string1.toString().getBytes());
            signature = byteToHex(digest);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", WxpayContext.config.APPID);
		return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
    
    
    /**
     *  扫码支付统一下单
     * @param body
     * @param out_trade_no 和product_id一样
     * @param spbill_create_ip APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     * @param total_fee 
     * @param payNotifyUrl
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
	public static Map<String, String> nativeUniformOrder(String body, String out_trade_no, String spbill_create_ip, String total_fee,String payNotifyUrl) throws Exception{
    	// 网页授权后获取传递的参数
    			// String money = "0.01";
    			// 金额转化为分为单位
    			// float sessionmoney = Float.parseFloat(money);
    			// String finalmoney = String.format("%.2f", sessionmoney);
    			// finalmoney = finalmoney.replace(".", "");
    			//total_fee = TypeConverUtil.$Str(TypeConverUtil.$BigDecimal(total_fee).intValue());
    			// 商户相关资料
    			String appid = WxpayContext.config.APPID;
    			String appsecret = WxpayContext.config.APPSECRET;
    			String mch_id = WxpayContext.config.MCHID;//商户号
    			
    			//
    			String partnerkey = WxpayContext.config.PARTNERKEY;//在微信商户平台pay.weixin.com里自己生成的那个key

    			// 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
    			String currTime = TenpayUtil.getCurrTime();
    			// 8位日期
    			String strTime = currTime.substring(8, currTime.length());
    			// 四位随机数
    			String strRandom = TenpayUtil.buildRandom(4) + "";
    			// 10位序列号,可以自行调整。
    			String strReq = strTime + strRandom;

    			// 随机数
    			String nonce_str = strReq;


    			// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
    			String notify_url = payNotifyUrl+"?out_trade_no="+out_trade_no;

    			String trade_type = "NATIVE";
    			
    			// 非必输
    			 String product_id = "out_trade_no";
    			SortedMap<String, String> packageParams = new TreeMap<String, String>();
    			packageParams.put("appid", appid);
    			packageParams.put("mch_id", mch_id);
    			packageParams.put("nonce_str", nonce_str);
    			packageParams.put("body", body);
    			// packageParams.put("attach", attach);
    			packageParams.put("out_trade_no", out_trade_no);

    			// 这里写的金额为1 分到时修改
    			packageParams.put("total_fee", TypeConverUtil.$Str(TypeConverUtil.$int(total_fee)));
    			// packageParams.put("total_fee", "finalmoney");
    			packageParams.put("spbill_create_ip", spbill_create_ip);
    			packageParams.put("notify_url", notify_url);

    			packageParams.put("trade_type", trade_type);
    		

    			RequestHandler reqHandler = new RequestHandler(null, null);
    			reqHandler.init(appid, appsecret, partnerkey);

    			String sign = reqHandler.createSign(packageParams);
    			String xml = "<xml>" 
    					+ "<appid>" + appid + "</appid>" 
    					+ "<mch_id>"+ mch_id +"</mch_id>" 
    					+ "<nonce_str>" + nonce_str+ "</nonce_str>"
    					+ "<sign><![CDATA[" + sign + "]]></sign>"
    					+ "<body><![CDATA[" + body + "]]></body>"
    					+ "<out_trade_no>"+ out_trade_no+ "</out_trade_no>"
    					+

    					// 金额，这里写的1 分到时修改
    					"<total_fee>"+ total_fee+ "</total_fee>"
    					+
    					// "<total_fee>"+finalmoney+"</total_fee>"+
    					"<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
    					+ "<notify_url>" + notify_url + "</notify_url>"
    					+ "<trade_type>" + trade_type + "</trade_type>" 
    					+ "<product_id>" + product_id + "</product_id>"+ "</xml>";
    			String allParameters = "";
    			try {
    				allParameters = reqHandler.genPackage(packageParams);
    			} catch (Exception e) {
    				System.out.println("allParameters:"+packageParams);
    			}
    			String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    			String prepay_id = "";
    			try {
    				prepay_id = new UnifiedOrder().getPayNo(createOrderURL, xml);
    				if (prepay_id.equals("")) {   				
    					throw new Exception("统一支付接口获取预支付订单出错");
    				}
    			} catch (Exception e1) {
    				throw e1;
    			}
    			System.out.println("prepay_id:"+prepay_id);
    			SortedMap<String, String> finalpackage = new TreeMap<String, String>();
    			String appid2 = appid;
    			String timestamp = Sha1Util.getTimeStamp();
    			String nonceStr2 = nonce_str;
    			String prepay_id2 = "prepay_id=" + prepay_id;
    			String packages = prepay_id2;
    			finalpackage.put("prepay_id", prepay_id);//这个数据不参与生成支付参数
    			finalpackage.put("appId", appid2);
    			finalpackage.put("timeStamp", timestamp);
    			finalpackage.put("nonceStr", nonceStr2);
    			finalpackage.put("package", packages);
    			finalpackage.put("signType", "MD5");
    			String finalsign = reqHandler.createSign(finalpackage);
    			finalpackage.put("paySign", finalsign);
    			
    			System.out.println("jsapi_ticket:"+WXTicket.getTicket());
    			return finalpackage;
    	
    }
	
	

    /**---统一下单----
=======
    
    /**---扫码统一下单----
	 * @param openid 
	 * 			用oath授权得到的openid
	 * @param body 
	 * 			付款描述
	 * @param out_trade_no 
	 * 			订单号
	 * @param spbill_create_ip 客户的IP地址
	 * @param total_fee 总金额
	 * @return  json格式js支付参数
	 */
    @Deprecated
	public static String scanDopay(String openid, String body, String out_trade_no, String spbill_create_ip, String total_fee,String payNotifyUrl)throws Exception{
		// 网页授权后获取传递的参数
		// String money = "0.01";
		// 金额转化为分为单位
		// float sessionmoney = Float.parseFloat(money);
		// String finalmoney = String.format("%.2f", sessionmoney);
		// finalmoney = finalmoney.replace(".", "");
		//total_fee = TypeConverUtil.$Str(TypeConverUtil.$BigDecimal(total_fee).intValue());
		// 商户相关资料
		String appid = WxpayContext.config.APPID;
		String appsecret = WxpayContext.config.APPSECRET;
		String mch_id = WxpayContext.config.MCHID;//商户号
		
		//
		String partnerkey = WxpayContext.config.PARTNERKEY;//在微信商户平台pay.weixin.com里自己生成的那个key

		// 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;

		// 随机数
		String nonce_str = strReq;


		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = payNotifyUrl+"?out_trade_no="+out_trade_no;

		String trade_type = "NATIVE";
		
		// 非必输
		// String product_id = "";
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		// packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", TypeConverUtil.$Str(TypeConverUtil.$int(total_fee)));
		// packageParams.put("total_fee", "finalmoney");
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openid);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>"
				+ "<body><![CDATA[" + body + "]]></body>"
				+ "<out_trade_no>"
				+ out_trade_no
				+ "</out_trade_no>"
				+

				// 金额，这里写的1 分到时修改
				"<total_fee>"
				+ total_fee
				+ "</total_fee>"
				+
				// "<total_fee>"+finalmoney+"</total_fee>"+
				"<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
				+ "<notify_url>" + notify_url + "</notify_url>"
				+ "<trade_type>" + trade_type + "</trade_type>" + "<openid>"
				+ openid + "</openid>" + "</xml>";
		String allParameters = "";
		try {
			allParameters = reqHandler.genPackage(packageParams);
		} catch (Exception e) {
			System.out.println("allParameters:"+packageParams);
		}
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String prepay_id = "";
		try {
			prepay_id = new UnifiedOrder().getPayNo(createOrderURL, xml);
			if (prepay_id.equals("")) {
			
				throw new Exception("统一支付接口获取预支付订单出错");
			}
		} catch (Exception e1) {
			throw e1;
		}
		System.out.println("prepay_id:"+prepay_id);
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String appid2 = appid;
		String timestamp = Sha1Util.getTimeStamp();
		String nonceStr2 = nonce_str;
		String prepay_id2 = "prepay_id=" + prepay_id;
		String packages = prepay_id2;
		finalpackage.put("appId", appid2);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonceStr2);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");
		String finalsign = reqHandler.createSign(finalpackage);
		
		System.out.println("jsapi_ticket:"+WXTicket.getTicket());
		return "{timestamp:\"" + timestamp  //这里的也是小写~~
				+ "\",nonceStr:\"" + nonceStr2 + "\",package:\""
				+ packages + "\",signType: \"MD5" + "\",paySign:\""
				+ finalsign + "\"}";
	}
    
    
	/**---jsapi统一下单----
>>>>>>> .r46
	 * @param openid 
	 * 			用oath授权得到的openid
	 * @param body 
	 * 			付款描述
	 * @param out_trade_no 
	 * 			订单号
	 * @param spbill_create_ip 客户的IP地址
	 * @param total_fee 总金额
	 * @return  json格式js支付参数
	 */
	public static Map<String, String> dopayMap(String openid, String body, String out_trade_no, String spbill_create_ip, String total_fee,String payNotifyUrl)throws Exception{
		// 网页授权后获取传递的参数
		// String money = "0.01";
		// 金额转化为分为单位
		// float sessionmoney = Float.parseFloat(money);
		// String finalmoney = String.format("%.2f", sessionmoney);
		// finalmoney = finalmoney.replace(".", "");
		//total_fee = TypeConverUtil.$Str(TypeConverUtil.$BigDecimal(total_fee).intValue());
		// 商户相关资料
		String appid = WxpayContext.config.APPID;
		String appsecret = WxpayContext.config.APPSECRET;
		String mch_id = WxpayContext.config.MCHID;//商户号
		
		//
		String partnerkey = WxpayContext.config.PARTNERKEY;//在微信商户平台pay.weixin.com里自己生成的那个key

		// 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;

		// 随机数
		String nonce_str = strReq;


		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = payNotifyUrl+"?out_trade_no="+out_trade_no;

		String trade_type = "JSAPI";
		
		// 非必输
		 String product_id = "";
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		// packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", TypeConverUtil.$Str(TypeConverUtil.$int(total_fee)));
		// packageParams.put("total_fee", "finalmoney");
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openid);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>"
				+ "<body><![CDATA[" + body + "]]></body>"
				+ "<out_trade_no>"
				+ out_trade_no
				+ "</out_trade_no>"
				+

				// 金额，这里写的1 分到时修改
				"<total_fee>"
				+ total_fee
				+ "</total_fee>"
				+
				// "<total_fee>"+finalmoney+"</total_fee>"+
				"<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
				+ "<notify_url>" + notify_url + "</notify_url>"
				+ "<trade_type>" + trade_type + "</trade_type>" + "<openid>"
				+ openid + "</openid>" + "</xml>";
		String allParameters = "";
		try {
			allParameters = reqHandler.genPackage(packageParams);
		} catch (Exception e) {
			System.out.println("allParameters:"+packageParams);
		}
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String prepay_id = "";
		try {
			prepay_id = new UnifiedOrder().getPayNo(createOrderURL, xml);
			if (prepay_id.equals("")) {
			
				throw new Exception("统一支付接口获取预支付订单出错");
			}
		} catch (Exception e1) {
			throw e1;
		}
		
		System.out.println("prepay_id:"+prepay_id);
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String appid2 = appid;
		String timestamp = Sha1Util.getTimeStamp();
		String nonceStr2 = nonce_str;
		String prepay_id2 = "prepay_id=" + prepay_id;
		String packages = prepay_id2;
		finalpackage.put("prepay_id", prepay_id);//这个数据不参与生成支付参数
		finalpackage.put("appId", appid2);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonceStr2);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");
		String finalsign = reqHandler.createSign(finalpackage);
		finalpackage.put("paySign", finalsign);
		
		System.out.println("jsapi_ticket:"+WXTicket.getTicket());
		return finalpackage;
	}
    
	/**---统一下单----
	 * @param openid 
	 * 			用oath授权得到的openid
	 * @param body 
	 * 			付款描述
	 * @param out_trade_no 
	 * 			订单号
	 * @param spbill_create_ip APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	 * @param total_fee 总金额
	 * @return  json格式js支付参数
	 */
	public static String dopay(String openid, String body, String out_trade_no, String spbill_create_ip, String total_fee,String payNotifyUrl)throws Exception{
		// 网页授权后获取传递的参数
		// String money = "0.01";
		// 金额转化为分为单位
		// float sessionmoney = Float.parseFloat(money);
		// String finalmoney = String.format("%.2f", sessionmoney);
		// finalmoney = finalmoney.replace(".", "");
		total_fee = TypeConverUtil.$Str(TypeConverUtil.$BigDecimal(total_fee).intValue());
		// 商户相关资料
		String appid = WxpayContext.config.APPID;
		String appsecret = WxpayContext.config.APPSECRET;
		String mch_id = WxpayContext.config.MCHID;//商户号
		
		//
		String partnerkey = WxpayContext.config.PARTNERKEY;//在微信商户平台pay.weixin.com里自己生成的那个key

		// 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;

		// 随机数
		String nonce_str = strReq;


		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = payNotifyUrl+"?out_trade_no="+out_trade_no;

		String trade_type = "JSAPI";
		
		// 非必输
		// String product_id = "";
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		// packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", TypeConverUtil.$Str(TypeConverUtil.$int(total_fee)));
		// packageParams.put("total_fee", "finalmoney");
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openid);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>"
				+ "<body><![CDATA[" + body + "]]></body>"
				+ "<out_trade_no>"
				+ out_trade_no
				+ "</out_trade_no>"
				+

				// 金额，这里写的1 分到时修改
				"<total_fee>"
				+ total_fee
				+ "</total_fee>"
				+
				// "<total_fee>"+finalmoney+"</total_fee>"+
				"<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
				+ "<notify_url>" + notify_url + "</notify_url>"
				+ "<trade_type>" + trade_type + "</trade_type>" + "<openid>"
				+ openid + "</openid>" + "</xml>";
		String allParameters = "";
		try {
			allParameters = reqHandler.genPackage(packageParams);
		} catch (Exception e) {
			System.out.println("allParameters:"+packageParams);
		}
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String prepay_id = "";
		try {
			prepay_id = new UnifiedOrder().getPayNo(createOrderURL, xml);
			if (prepay_id.equals("")) {
			
				throw new Exception("统一支付接口获取预支付订单出错");
			}
		} catch (Exception e1) {
			throw e1;
		}
		System.out.println("prepay_id:"+prepay_id);
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String appid2 = appid;
		String timestamp = Sha1Util.getTimeStamp();
		String nonceStr2 = nonce_str;
		String prepay_id2 = "prepay_id=" + prepay_id;
		String packages = prepay_id2;
		finalpackage.put("appId", appid2);
		finalpackage.put("timeStamp", timestamp);
		finalpackage.put("nonceStr", nonceStr2);
		finalpackage.put("package", packages);
		finalpackage.put("signType", "MD5");
		String finalsign = reqHandler.createSign(finalpackage);
		
		System.out.println("jsapi_ticket:"+WXTicket.getTicket());
		return "{timestamp:\"" + timestamp  //这里的也是小写~~
				+ "\",nonceStr:\"" + nonceStr2 + "\",package:\""
				+ packages + "\",signType: \"MD5" + "\",paySign:\""
				+ finalsign + "\"}";
	}
	
	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
