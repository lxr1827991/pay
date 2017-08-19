package com.lxr.pay.alipay;

public class AlipayException extends Exception{

	public AlipayException() {
		super();
	}

	public AlipayException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AlipayException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AlipayException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AlipayException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
