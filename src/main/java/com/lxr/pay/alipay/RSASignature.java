package com.lxr.pay.alipay;


import java.security.KeyFactory;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.spec.PKCS8EncodedKeySpec;  
import java.security.spec.X509EncodedKeySpec;  

import org.apache.commons.codec.binary.Base64;
  
  
  
/** 
 * RSA签名验签类 
 */  
public class RSASignature{  
      
    /** 
     * 签名算法 
     */  
    public static final String RSA = "SHA1WithRSA";
    
    public static final String RSA2= "SHA256WithRSA";
  
    /** 
    * RSA签名 
    * @param content 待签名数据 
    * @param privateKey 商户私钥 
    * @param encode 字符集编码 
    * @return 签名值 
    */  
    public static String sign(String content, String privateKey, String encode)  
    {  
    	
    	Base64 base64 = new Base64();
    	
        try   
        { 
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( base64.decode(privateKey) );   
              
            KeyFactory keyf                 = KeyFactory.getInstance("RSA");  
            PrivateKey priKey               = keyf.generatePrivate(priPKCS8);  
  
            java.security.Signature signature = java.security.Signature.getInstance(RSA);  
  
            signature.initSign(priKey);  
            signature.update( content.getBytes(encode));  
  
            byte[] signed = signature.sign();  
              
            return new String(base64.encode(signed));  
        }  
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
          
        return null;  
    }  
      
    public static String sign(String content, String privateKey)  
    {  Base64 base64 = new Base64();
        try   
        {  
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( base64.decode(privateKey) );   
            KeyFactory keyf = KeyFactory.getInstance("RSA");  
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);  
            java.security.Signature signature = java.security.Signature.getInstance(RSA);  
            signature.initSign(priKey);  
            signature.update( content.getBytes());  
            byte[] signed = signature.sign();  
            return new String(base64.encode(signed));  
        }  
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  
      
    /** 
    * RSA验签名检查 
    * @param content 待签名数据 
    * @param sign 签名值 
    * @param publicKey 分配给开发商公钥 
    * @rsa RSA，RSA2
    * @param encode 字符集编码 
    * @return 布尔值 
    */  
    public static boolean check(String content, String sign, String publicKey,String rsa,String encode)  
    {  Base64 base64 = new Base64();
        try
        {  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            byte[] encodedKey = base64.decode(publicKey);  
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));  
  
          
            java.security.Signature signature = java.security.Signature.getInstance(rsa);  
          
            signature.initVerify(pubKey);  
            signature.update( content.getBytes(encode) );  
          
            boolean bverify = signature.verify(base64.decode(sign) );  
            return bverify;  
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
          
        return false;  
    }  
      
    public static boolean checkRSA2(String content, String sign, String publicKey)  
    {  
    	return check(content, sign, publicKey, RSA2, "UTF-8");
    } 
    
    public static boolean checkRSA(String content, String sign, String publicKey)  
    {  
    	return check(content, sign, publicKey, RSA, "UTF-8");
    } 
      
}  