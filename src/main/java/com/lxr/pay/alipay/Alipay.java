package com.lxr.pay.alipay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.If;

import net.sf.json.JSONObject;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lxr.commons.exception.ApplicationException;


public class Alipay {
	
	
	/**
	 * @see https://doc.open.alipay.com/doc2/detail.htm?treeId=200&articleId=105351&docType=1#s2
	 */
	public static final String CODE_OK = "10000";
	

	AlipayClient alipayClient;
	AlipayConfigurator alipayConfigurator;
	
	
	
	
//https://openapi.alipay.com/gateway.do 
protected static final String URL = "https://openapi.alipay.com/gateway.do";

//
protected  String format = "json";
//
protected  String charset = "UTF-8";



	
	public Alipay(AlipayConfigurator configurator){
		
		alipayConfigurator = configurator;
		if(alipayConfigurator==null)alipayConfigurator = newAlipayConfigurator();
		init();
	}
	
	public void init() {
	alipayClient = new DefaultAlipayClient(
				URL
				,alipayConfigurator.appId
				,alipayConfigurator.appPrivateKey
				,alipayConfigurator.format
				,alipayConfigurator.charset
				,alipayConfigurator.alipayPublicKey
				,alipayConfigurator.signType);

	}
	
	
	
	
	
	
	
	public void doPay(AliPrePay prePay,HttpServletResponse httpResponse) throws IOException {
		  AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//鍒涘缓API瀵瑰簲鐨剅equest
		  if(prePay.getReturnUrl()!=null)
			  alipayRequest.setReturnUrl(prePay.getReturnUrl());
			  else
		    alipayRequest.setReturnUrl(alipayConfigurator.returnUrl);
		  if(prePay.getNotifyUrl()!=null)
			  alipayRequest.setNotifyUrl(prePay.getNotifyUrl());
			  else
		    alipayRequest.setNotifyUrl(alipayConfigurator.notifyUrl);//鍦ㄥ叕鍏卞弬鏁颁腑璁剧疆鍥炶烦鍜岄�鐭ュ湴鍧�
		  AlipayTradePayModel model = new AlipayTradePayModel();
		  
		  alipayRequest.setBizModel(model);
		  model.setOutTradeNo(prePay.getOutTradeNo());
		  model.setSubject(prePay.getSubject());
		  model.setTotalAmount(prePay.getTotalAmount()+"");
		  model.setProductCode(prePay.getProductCode());
		  model.setSellerId(alipayConfigurator.sellerId);//娌欑閽卞寘涓殑浠樻鐮�
		  
		    String form="";
		    try {
		        form = alipayClient.pageExecute(alipayRequest).getBody(); //璋冪敤SDK鐢熸垚琛ㄥ崟
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		    }
		    httpResponse.setContentType("text/html;charset=" + alipayConfigurator.charset);
		    httpResponse.getWriter().write(form);//鐩存帴灏嗗畬鏁寸殑琛ㄥ崟html杈撳嚭鍒伴〉闈�
		    httpResponse.getWriter().flush();
		    httpResponse.getWriter().close();

	}
	
	
	
	
	/**
	 * 
	 * @param outTradeNo
	 * @return
	 */
	@Deprecated
	public Object doQuery(String outTradeNo) {
		
		if(outTradeNo==null||outTradeNo.equals(""))
			throw new ApplicationException("outTradeNo:"+outTradeNo);
		
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		
		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		
		model.setOutTradeNo(outTradeNo);
		
		
		request.setBizModel(model);
		request.setNotifyUrl(alipayConfigurator.notifyUrl);
		AlipayTradeQueryResponse response;
		try {
			response = alipayClient.sdkExecute(request);
			
			return response;
		} catch (Exception e) {
			throw new ApplicationException(e);
		} 
		

	}
	
	
	
	
	
	public String doAppPay(AliPrePay prePay){
		
		
		try {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
	
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		if(!StringUtils.isEmpty(prePay.getSubject()))
		model.setSubject(URLEncoder.encode(prePay.getSubject(), "utf-8"));
		if(!StringUtils.isEmpty(prePay.getBody()))
		model.setBody(URLEncoder.encode(prePay.getBody(), "utf-8"));
		model.setOutTradeNo(prePay.getOutTradeNo());
		model.setTimeoutExpress("30m");
		model.setProductCode("QUICK_MSECURITY_PAY");
		model.setTotalAmount(prePay.getTotalAmount()+"");
		
		request.setBizModel(model);
		request.setNotifyUrl(alipayConfigurator.notifyUrl);
		AlipayTradeAppPayResponse response;
		
			response = alipayClient.sdkExecute(request);
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new ApplicationException(e);
		} 
		
	}

	
	
	
	
	
	
	public String testPay() {
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", alipayConfigurator.appId
				, alipayConfigurator.appPrivateKey, "json", "utf-8", alipayConfigurator.alipayPublicKey, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("我是测试数据");
		model.setSubject("App支付测试Java");
		model.setOutTradeNo("21432543");
		model.setTimeoutExpress("30m");
		model.setTotalAmount("0.01");
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl("商户外网可以访问的异步地址");
		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
		        System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
		        
		        return response.getBody();
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		}
		
		return null;

	}
	
	
	/**
	 * 扫码支付
	 * @param prePay
	 * @return
	 * @throws AlipayException
	 */
	public String scanPay(AliPrePay prePay) throws AlipayException {
		AlipayTradePrecreateRequest alipayTradePrecreateRequest = new AlipayTradePrecreateRequest();//鍒涘缓API瀵瑰簲鐨剅equest绫�
		alipayTradePrecreateRequest.setNotifyUrl(prePay.getNotifyUrl()==null?alipayConfigurator.notifyUrl:prePay.getNotifyUrl());
		AlipayTradePayModel model = new AlipayTradePayModel();
		  
		alipayTradePrecreateRequest.setBizModel(model);
		
		model.setOutTradeNo(prePay.getOutTradeNo());
		model.setStoreId(prePay.getStoreId());
		model.setTotalAmount(prePay.getTotalAmount()+"");
		model.setSubject(prePay.getSubject());

	/*	alipayTradePrecreateRequest.setBizContent("{" +
		"    \"out_trade_no\":\""+prePay.getOutTradeNo()+"\"," +
		"    \"total_amount\":\""+prePay.getTotalAmount()+"\"," +
		"    \"subject\":\""+prePay.getSubject()+"\"," +
		"    \"store_id\":\""+prePay.getStoreId()+"\"," +
		"    \"timeout_express\":\"15m\"}");//璁剧疆涓氬姟鍙傛暟
*/		AlipayTradePrecreateResponse response;
		try {
			response = alipayClient.execute(alipayTradePrecreateRequest);
			
			if(!response.isSuccess())
				throw new AlipayException("鑾峰彇鎵爜澶辫触锛�"+response.getBody());
			
			return response.getQrCode();
			
		} catch (Exception e) {
			throw new AlipayException(e);

		}
		
			

	}
	
	
	
	 public void scanPay(AliPrePay prePay,OutputStream out) throws AlipayException {
		String url = scanPay(prePay);
		 try {
	            int qrcodeWidth = 300;
	            int qrcodeHeight = 300;
	            String qrcodeFormat = "png";
	            HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
	            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	           
					BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
				

	            BufferedImage image = new BufferedImage(qrcodeWidth, qrcodeHeight, BufferedImage.TYPE_INT_RGB);
	           // Random random = new Random();
	            MatrixToImageWriter.writeToStream(bitMatrix, qrcodeFormat, out);
	           // MatrixToImageWriter.writeToFile(bitMatrix, qrcodeFormat, out);
					///ImageIO.write(image, qrcodeFormat, out);
				} catch (Exception e) {
					throw new AlipayException(e);
				} 
	           // 
	            
	     

	}
	
	public void cancelTrade(String out_trade_no,String trade_no) throws AlipayException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizContent("{" +
		"    \"out_trade_no\":\""+((out_trade_no==null)?"":out_trade_no)+"\"," +
		"    \"trade_no\":\""+((trade_no==null)?"":trade_no)+"\"" +
		"  }");
		
		try {
			AlipayTradeCancelResponse response = alipayClient.execute(request);
			if(!response.isSuccess())
				throw new AlipayApiException("鍙栨秷璁㈠崟澶辫触锛�"+response.getBody());
		} catch (AlipayApiException e) {
			throw new AlipayException(e);
		}
	
	}
	
	
	
	
	
	
	public TradeQuery queryTrade(String out_trade_no,String trade_no) throws AlipayException {
		AlipayTradeQueryResponse response = queryTrade4response(out_trade_no,trade_no);
		String body = response.getBody();
		System.out.println(body);
			return new TradeQuery(JSONObject.fromObject(body).getJSONObject("alipay_trade_query_response"));
	}
	
	
	public AlipayTradeQueryResponse queryTrade4response(String out_trade_no,String trade_no) throws AlipayException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();//鍒涘缓API瀵瑰簲鐨剅equest绫�
		request.setBizContent("{" +
				"    \"out_trade_no\":\""+((out_trade_no==null)?"":out_trade_no)+"\"," +
				"    \"trade_no\":\""+((trade_no==null)?"":trade_no)+"\"" +
				"  }"); //璁剧疆涓氬姟鍙傛暟
		try {
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if(!response.isSuccess())
				throw new AlipayException("鏌ヨ澶辫触"+response.getBody());
			return response;
		} catch (Exception e) {
			throw new AlipayException(e);
		}
		
	}
	
	
	
	public boolean notifyCheck(Map<String, String> paramsMap) {
	
		
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		try {
			return AlipaySignature.rsaCheckV1(paramsMap, alipayConfigurator.alipayPublicKey, charset, "RSA2");
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return false;
		}
		
		
				

	}
	
	
	
	public boolean checkSign(Map<String, String> params) {
		String sign = params.remove("sign");
		String signType = params.remove("sign_type");
		String content = getSignStr(params);
		
		//System.out.println(sign);
		//System.out.println(signType);
		//System.out.println(content);
		
		return  RSASignature.check(content,sign,alipayConfigurator.alipayPublicKey,"RSA2".equals(signType)?RSASignature.RSA2:RSASignature.RSA,"UTF-8");

	}
	
	
	
	public static String getSignStr(Map<String, String> params) {
		
		
		
		SortedMap<String, String> packageParams;
		if(params instanceof SortedMap)
			packageParams = (SortedMap)params;
		else packageParams = new TreeMap<String, String>(params);
			
		
		
		String	charset = "utf-8";
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v)) {
				sb.append(k + "=" + URLDecoder.decode(v) + "&");
			}
		}
		
	
		return sb.substring(0, sb.length()-1);
	}
	
	
	
	public AlipayClient getAlipayClient() {
		return alipayClient;
	}
	
	public static AlipayConfigurator newAlipayConfigurator() {
		return new AlipayConfigurator();

	}
	

	
	public static void main(String[] args) throws Exception {
		
		Map<String, String> map = new HashMap<>();
		
		map.put("body", "洪城停车余额充值");
		map.put("subject", "余额充值");
		map.put("sign_type", "RSA2");
		map.put("buyer_logon_id", "183****0283");
		map.put("auth_app_id", "2016122104481405");
		map.put("notify_type", "trade_status_sync");
		map.put("out_trade_no", "20171116170043252889240541613341");
		map.put("point_amount", "0.00");
		map.put("version", "1.0");
		map.put("fund_bill_list", "[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]");
		map.put("buyer_id", "2088222092183919");
		map.put("total_amount", "0.01");
		map.put("trade_no", "2017111621001104910577998773");
		map.put("notify_time", "2017-11-16 17:00:51");
		map.put("charset", "utf-8");
		map.put("invoice_amount", "0.01");
		map.put("trade_status", "TRADE_SUCCESS");
		map.put("gmt_payment", "2017-11-16 17:00:50");
		//map.put("sign", "fOUkMrztQ+zctOr7yYVxDObPOHvs9LtF5FgBiopTnomN3zTfCtURHl1gZbp9h1fp0cnuDZ/yqkG5SlIi9LO/g34z84QesTrIeet6oJJ0eekUzaTlDwWSyb1mE4ZwL3igqnKHb9TzLHK7iZzmEylO5LmGER2CuW59xm9Xh1hgPnXaGk/+BsGZ6utOcSp8ejEnjMgFxq6IsJSV829vlgxk5IuxBhp+7XuTiqjxCmyt7zCiY9fLwJUmWSTEKQVpLHtNwlF5wWUJsEsPoRHHJHbGUcgUCZH6pKvggb43+pYFa/euaenZsf0LtW6OiGQsdNDYMoIQTW+sso5zTnyiE6MQSA==");
		map.put("gmt_create", "2017-11-16 17:00:50");
		map.put("buyer_pay_amount", "0.01");
		map.put("receipt_amount", "0.01");
		map.put("app_id", "2016122104481405");
		map.put("seller_id", "2088521296419681");
		map.put("notify_id", "4dc1e9f745c487c369780cdda656824n0x");
		map.put("seller_email", "zhtc@hcykt.com");
		
		System.out.println(AlipayFactory.getPay("/alipay.xml").notifyCheck(map));
				
		
	}
	
	
}
