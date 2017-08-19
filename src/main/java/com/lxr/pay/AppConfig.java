package com.lxr.pay;



public class AppConfig {

	static String name ="xxx商城";
	
	
	static String host = "huji820.oicp.net";

	
	
	static String imgHost = "http://localhost";
	
	static int  imgPort = 80;
	
	public static String getName() {
	
		return name;
	}

	public static void setName(String name) {
		AppConfig.name = name;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		AppConfig.host = host;
	}
	
	
	
	public static String getImgUrl(String path,String root){
		String p = "/"+root+"/"+path;
		
	
			return imgHost+p;
		
	}
	
	
	
	
	
}
