package com.lxr.pay;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.lxr.commons.exception.ApplicationException;

public class HttpUtils {

	public static String getString(String url) {
		CloseableHttpClient httpCilent = HttpClients.createDefault();//Creates CloseableHttpClient instance with default configuration.
		HttpGet httpGet = new HttpGet(url);
		try {
		  
		 	
		    HttpResponse httpResponse = httpCilent.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200)
                return EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
            else
            	throw new ApplicationException("请求错误：StatusCode="+httpResponse.getStatusLine().getStatusCode() );
               
		} catch (IOException e) {
		    throw new ApplicationException(e);
		}finally {
		    try {
		        httpCilent.close();//释放资源
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}

	}
	
}
