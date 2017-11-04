package com.lxr.pay.wxpay;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lxr.commons.exception.ApplicationException;
import com.lxr.commons.utils.SignUtil;


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
	public String createQrcodeUrl(PreOrder preOrder)throws UnifiedorderException {
		Map<String, String> map= super.unifiedOrder(preOrder, TRADE_TYPE_NATIVE);
		return map.get("code_url");
	}

	
	
	public void createQrcode(PreOrder prePay,OutputStream out) {
		String url = createQrcodeUrl(prePay);
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
					throw new ApplicationException(e);
				} 
	          // 

	}
	
	
	
	
	
	
	
}
