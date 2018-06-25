package com.lxr.pay.wxpay;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lxr.commons.exception.ApplicationException;
import com.lxr.pay.wxpay.bean.WxNativeOrder;
import com.lxr.pay.wxpay.bean.WxOrder;



/**
 *模式二扫码
 * 微信扫码
 *@author lxr
 *@see https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_4
 */
public class WXNativePay extends WXPay{

	
	public WXNativePay(WXConfig config) {
		super(config);
	}
	
	

	/**
	 * 统一下单并获取扫码url
	 * @param preOrder
	 * @return
	 * @throws UnifiedorderException
	 */
	public String doNativePayUrl2(WxOrder preOrder) {
		Map<String, String> map= super.unifiedOrder(preOrder, TRADE_TYPE_NATIVE);
		return map.get("code_url");
	}

	
	@Override
	protected void onUnifiedOrder(Map<String, String> map, WxOrder order) {
		WxNativeOrder nativeOrder = (WxNativeOrder)order;
		map.put("openid", nativeOrder.getOpenid());
		
		
		map.put("product_id", nativeOrder.getProductId());
		map.put("spbill_create_ip", nativeOrder.getServerIp());
	}
	
	
	
	
	
	
	
	
	
}
