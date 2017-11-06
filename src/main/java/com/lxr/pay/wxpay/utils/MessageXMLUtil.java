package com.lxr.pay.wxpay.utils;

import java.io.InputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;




/**
 * 消息工具类
 * @author Administrator
 *
 */
public class MessageXMLUtil {

	/**
	 * 解析微信发来的请求（XML）
	 * @param request
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request)
			throws Exception {
		// 将解析结果存储在 HashMap 中
		Map<String, String> map = new HashMap<String, String>();

		// 从 request 中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到 xml 根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}
	
	
	
	/**
	 * （XML）
	 * @param request
	 * @author lxr
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String formatXml(Map<String, String> par){
		
		StringBuilder builder = new StringBuilder("<xml>\n");
		
		for(Map.Entry<String, String> en:par.entrySet()){    
		builder.append("<").append(en.getKey()).append("><![CDATA[").append(en.getValue()).append("]]></").append(en.getKey()).append(">\n");		
		}  
		builder.append("</xml>");
		return "";
	}
	
	/**
	 * 解析微信发来的请求（XML）
	 * @param request
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(InputStream is)
			throws Exception {
		// 将解析结果存储在 HashMap 中
		Map<String, String> map = new HashMap<String, String>();

		// 从 request 中取得输入流
		InputStream inputStream = is;
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到 xml 根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}
	
	/**
	 * 解析微信发来的请求（XML）
	 * @param request
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static SortedMap<String, String> parseXml2(InputStream is)
			throws Exception {
		// 将解析结果存储在 HashMap 中
		SortedMap<String, String> map = new TreeMap();

		// 从 request 中取得输入流
		InputStream inputStream = is;
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到 xml 根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}
	
	/**
	 * 解析微信发来的请求（XML）
	 * @param request
	 * 
	 * @return
	 * @throws Exception
	 */

	public static SortedMap<String, String> parseXml2(String xml)
			throws Exception {
		// 将解析结果存储在 HashMap 中
		SortedMap<String, String> map = new TreeMap();

	
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(xml);
		// 得到 xml 根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		

		return map;
	}
	
	
	
}
