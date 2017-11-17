package com.lxr.pay.alipay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class AlipayNotify {
	
	
	Map<String, String> notify;
	
	public AlipayNotify(Map<String, String> map) {
		notify = map;
	}
	
	public boolean isSuccess() {
		
		return "TRADE_SUCCESS".equals(notify.get("trade_status"));

	}
	
	
	/**
	 * 商户订单号
	 * @return
	 */
	public String getOutTradeNo() {
		return notify.get("out_trade_no");

	}
	
	/**
	 * 
	 * @return
	 */
	public String getTotalAmount() {
		return notify.get("total_amount");

	}
	
	/**
	 * 支付宝交易凭证号
	 * @return
	 */
	public String getTradeNo() {
		return notify.get("trade_no");

	}
	
	public Long getPayTime() {
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(notify.get("gmt_payment"));

			return date.getTime();
		} catch (Exception e) {
			
		}
		
		return null;
		
	}
	
	
	

}
