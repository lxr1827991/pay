package com.lxr.pay.wxpay;

import java.io.IOException;



import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.omg.PortableInterceptor.SUCCESSFUL;

import com.lxr.commons.https.HttpClientConnectionManager;





//凭证
public class WXTicket {

	private static long CTIME = 0;
	
	private static String TICKET = "";
	private static String TOKEN = "";
	private static long WX_ALIVE = 7000000;//有效时长
	
	public static String getTicket(WXConfig config) {
		if (TICKET == null || TICKET.isEmpty() || !isAlive()) {
			try {
				createNewTicket(config);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return TICKET;
	}
	
	
	/**
	 * 判断当前的TICKET是否还在有效期之内
	 * 微信的TICKET有效期为7200秒
	 * @return
	 */
	private static boolean isAlive() {
		long now = System.currentTimeMillis();
		
		if (now - CTIME > WX_ALIVE) {
			return false;
		}
		return true;
	}
	
	/**
	 * 创建一个新的凭证
	 * 1、首先获取access_toket,http请求方式: GET,https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
	 * 2、获取ticket凭证，https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=wx_card
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private static void createNewTicket(WXConfig config) throws ClientProtocolException, IOException {
		
		HttpGet httpGet = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+config.APPID+"&secret="+config.APPSECRET);
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient);
		
		HttpResponse response = httpclient.execute(httpGet);
		
		if(response.getStatusLine().getStatusCode()==200){
			String jsonStr = EntityUtils
					.toString(response.getEntity(), "UTF-8");
			
			JSONObject jsonObject = JSONObject.fromObject(jsonStr);
			
			String access_token = jsonObject.getString("access_token");
		
			if (access_token != null && !access_token.isEmpty()) {
				httpGet = HttpClientConnectionManager.getGetMethod("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi");
				
				response = httpclient.execute(httpGet);
				
				if(response.getStatusLine().getStatusCode()==200){
					jsonStr = EntityUtils
							.toString(response.getEntity(), "UTF-8");
					
					jsonObject = JSONObject.fromObject(jsonStr);
				
					if (jsonObject.getString("errcode").equals("0")) {
						TICKET = jsonObject.getString("ticket");
						CTIME = System.currentTimeMillis();
						TOKEN = access_token;
					}
				}
			}
		}
	}
	
	public static String getToken(WXConfig config) throws Exception {
		if (TICKET == null || TICKET.isEmpty() || !isAlive()) {
			try {
				createNewTicket(config);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return TOKEN;
	}
	public static void main(String args[]) throws Exception {
	}
}
