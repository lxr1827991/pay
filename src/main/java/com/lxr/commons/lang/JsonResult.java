package com.lxr.commons.lang;

public class JsonResult {
	//业务处理错误
	public static final int STATUS_FAIL = 1;
	//业务处理成功
	public static final int STATUS_SUCCESS = 0;
	//接口调用错误
	public static final int STATUS_CALL_ERROR = 4;
	//未认证
	public static final int STATUS_UN_AUTH = 5;
	//参数不合法
	public static final int STATUS_INVALID_PARAMETER= 100;
	
	int status = 0;
	
	String msg;
	
	Object data;
	
	public JsonResult() {
		
	}
	
	public JsonResult(int s) {
		
		status = s;
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
	public static JsonResult getFailResult(String msg) {
		return getResult(STATUS_FAIL,msg);

	}
	
	
	public static JsonResult getSuccessResult(Object obj) {
		return getResult(STATUS_SUCCESS,null,obj);

	}
	
	public static JsonResult getSuccessResult(String m) {
		return getResult(STATUS_SUCCESS,m,null);

	}
	
	
	
	public static JsonResult getResult(int s) {
		
		return getResult(s,null,null);

	}
	
	
	public static JsonResult getResult(int s,String m) {
		
		return getResult(s,m,null);

	}
	
	public static JsonResult getResult(int s,String m,Object d) {
		JsonResult result = new JsonResult(s);
		result.setMsg(m);
		result.setData(d);
		return result;

	}
	
}
