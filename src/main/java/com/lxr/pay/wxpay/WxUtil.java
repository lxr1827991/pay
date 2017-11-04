package com.lxr.pay.wxpay;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.lxr.commons.exception.ApplicationException;
import com.lxr.pay.wxpay.utils.MD5Util;
import com.lxr.pay.wxpay.utils.TenpayUtil;





public class WxUtil {
	
	public static boolean checkNotify(Map<String, String> result  ) {
		
		return true;
	}
	
	
	// 获取package的签名包
		public static String genPackage(SortedMap<String, String> packageParams,String partnerKey)throws UnsupportedEncodingException {
			String sign = createSign(packageParams,partnerKey);

			StringBuffer sb = new StringBuffer("<xml>");
			Set es = packageParams.entrySet();
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				String v = (String) entry.getValue();
				if (v == null) {
					throw new RuntimeException(k+"=null");
				}
				sb.append("<"+k + ">" + v + "</"+k + ">" );
			}

			// 去掉最后一个&
			String packageValue = sb.append("<sign>"+sign+"</sign></xml>").toString();
//			System.out.println("UrlEncode后 packageValue=" + packageValue);
			return packageValue;
		}

		
		
		/**
		 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
		 */
		public static String createSign(SortedMap<String, String> packageParams,String partnerKey) {
			StringBuffer sb = new StringBuffer();
			Set es = packageParams.entrySet();
			Iterator it = es.iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String k = (String) entry.getKey();
				String v = (String) entry.getValue();
				if (null != v && !"".equals(v) && !"sign".equals(k)
						&& !"key".equals(k)) {
					sb.append(k + "=" + v + "&");
				}
			}
			sb.append("key=" + partnerKey);
			System.out.println("md5 sb:" + sb);
			String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8")
					.toUpperCase();
			System.out.println("packge签名:" + sign);
			return sign;

		}
		
		
		
		// 特殊字符处理
		public static String UrlEncode(String src) throws UnsupportedEncodingException {
			String result = "";
			try {
				result = URLEncoder.encode(src, "UTF-8").replace("+", "%20");
			} catch (Exception e) {
				System.out.println(src);
				e.printStackTrace();
			}
			return result;
		}

		
		/**
		 * 微信生成签名随机字符串，不大于32位
		 * @return
		 */
		public static String createNonceStr() {
			String currTime = TenpayUtil.getCurrTime();
			// 8位日期
			String strTime = currTime.substring(8, currTime.length());
			// 四位随机数
			return TenpayUtil.buildRandom(4) + "";

		}
		
		public static Map<String, String> getResult(String xml){
			 
	        Document document = null;
			try {
				document = DocumentHelper.parseText(xml);
			} catch (DocumentException e) {
				throw new ApplicationException("xml解析错误："+e.getMessage());
			} 
	        
 	        Element root = document.getRootElement();
 	        
 	        if(!root.getName().equals("xml"))
 	        	throw new ApplicationException("根标签错误："+root.getName());
 	        
 	        List<Element> elements = root.elements();
 	        
 	       Map<String, String> map = new HashMap<>();
 	        for (Element element : elements) {
				map.put(element.getName(), element.getStringValue());
			}
 	        
	        
	        
	        return map;
		}
		
		
		public static BigDecimal fen2yuan(int i) {
			if(i<0)
				throw new ApplicationException("金额数据不可小于0");

			return new BigDecimal(i).divide(new BigDecimal(100));
			
			
		}
		
		public static int yuan2fen(BigDecimal i) {
			if(i.compareTo(BigDecimal.ZERO)==-1)
				throw new ApplicationException("金额数据不可小于0");

			 return i.multiply(new BigDecimal(100)).intValue();
			
			
		}
		
		
		public static void main(String[] args) {
			
			System.out.println(fen2yuan(0));
			System.out.println(yuan2fen(new BigDecimal(0)));
		}
		
}
