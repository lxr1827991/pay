package com.lxr.pay;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lxr.commons.exception.ApplicationException;

public class QrcodeUtils {

	
	public static void createQrcode(String url,OutputStream out) {
		
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
