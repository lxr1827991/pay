package com.lxr.pay.wxpay;

public class UnifiedorderException extends RuntimeException{
	
	String msg;
	

	public UnifiedorderException() {
		super();
	}

	public UnifiedorderException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnifiedorderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnifiedorderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public UnifiedorderException(String message,String msg) {
		super(message);
		setMsg(msg);
		// TODO Auto-generated constructor stub
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	

}
