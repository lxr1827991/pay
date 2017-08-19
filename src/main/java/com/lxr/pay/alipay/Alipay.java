package com.lxr.pay.alipay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.management.Query;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import net.sf.json.JSONObject;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;


public class Alipay {
	
	
	/**
	 * @see https://doc.open.alipay.com/doc2/detail.htm?treeId=200&articleId=105351&docType=1#s2
	 */
	public static final String CODE_OK = "10000";
	

	AlipayClient alipayClient;
	AlipayConfigurator alipayConfigurator;
	
	
	
	
//
protected static final String URL = "https://openapi.alipaydev.com/gateway.do";

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
	
	
	public void doPay(PrePay prePay,HttpServletResponse httpResponse) throws IOException {
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
		  model.setTotalAmount(prePay.getTotalAmount());
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
	
	
	
	public String scanPay(PrePay prePay) throws AlipayException {
		AlipayTradePrecreateRequest alipayTradePrecreateRequest = new AlipayTradePrecreateRequest();//鍒涘缓API瀵瑰簲鐨剅equest绫�
		alipayTradePrecreateRequest.setNotifyUrl(prePay.getNotifyUrl()==null?alipayConfigurator.notifyUrl:prePay.getNotifyUrl());
		AlipayTradePayModel model = new AlipayTradePayModel();
		  
		alipayTradePrecreateRequest.setBizModel(model);
		
		model.setOutTradeNo(prePay.getOutTradeNo());
		model.setStoreId(prePay.getStoreId());
		model.setTotalAmount(prePay.getTotalAmount());
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
	
	
	
	 public void scanPay(PrePay prePay,OutputStream out) throws AlipayException {
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
		String sign = "qs7vUcwVFXNFv80pe/pGRt0UV7c9yvQK8GMSRZvjJu5BQkWlFKo"
				+"l/hm83tEWD4chXr2NOwA3fB fMTP5gheWYCIpZR01C3Yz2ds7S4CN3iad4I brbFEiq/Fzz9zG3PUF9T"
				+" dxCttie5adT7ccTgKlsRR1HLL tCexo4/HwP/Fbsv4/N/kV1QXOCjUMdyKXNripbYsaBlfvjiKGy/W4"
				+"GiQzDk508xeqvRpt3kSwadoi/aUcPTefrT yxCPitAQV6CpbdOCLpyfVElSzJEZYqpX2GdpEz5e4F0lY"
				+"tGfuBw9 ufMOC H8to4DmeQW6mXZh mqE9gCyJqUcSJFhvl eMw==";
		String sign_type = "RSA";
		String content
		= 
		 "app_id=2016080300159818&auth_app_id=2016080300159818&buyer_id=2088102172232336&b"
		+"uyer_logon_id=stw***@sandbox.com&buyer_pay_amount=199.99&charset=UTF-8&fund_bill"
		+"_list=[{\"amount\":\"199.99\",\"fundChannel\":\"ALIPAYACCOUNT\"}]&gmt_create=2017-04-22 "
		+"09:45:15&gmt_payment=2017-04-22 09:45:28&invoice_amount=199.99&notify_id=862fd3d"
		+"b0b0a809ff4fcfd22ae2f05fijq&notify_time=2017-04-22 09:45:29&notify_type=trade_st"
		+"atus_sync&open_id=20881017170417409193523670119533&out_trade_no=1232435465765879"
		+"&point_amount=0.00&receipt_amount=199.99&seller_email=fjncfe6410@sandbox.com&sel"
		+"ler_id=2088102169808953&&subject=xxx鍟嗗搧&total_amount=199.99&trade_no=2017042221001004330200278920&trade_status=TRADE_SUCCESS&versi"
		+"on=1.0";
		
		String pk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2wF+na+fGS5aWySFSnIVCulpPmLgXMa5FH52X1EI1Cub2rUuWX2YnyyWOyLcXmnm9l3vA1b74eDBsi+5TM59aWtHTCafsbxS4+URUsZYbQhrysCCc2ZMR2fMqINTU2bUsJl6qidSSNbFVH84IpElZyxOZUDK4Lt4EucmjXJw6JVknbwb0F9HrX+tnOramhF8ffej4EmJnwK8dKd8DaQf76Cp1EHfhPzW+YyRhCWl6D5janZ4bsfSbqRB2bLY4pXecKAXXoXvrjQ/xGMA1yiz9z0iFsCIDK5MSoy8nUDq1tiDPXepRMeAD2Z31QI5b7Cva2Qx/Iis8oVV2PlOX5+eaQIDAQAB";
		Base64 base64 = new Base64();
		
		System.out.println(sign.length());
		System.out.println(RSAEncrypt.verify(content.getBytes(), pk,sign));
	}
	
	
}
