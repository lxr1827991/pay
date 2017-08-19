package com.lxr.pay.alipay;

import net.sf.json.JSONObject;

public class TradeQuery {
	
	JSONObject result;
	
	public TradeQuery(JSONObject json) {
		this.result = json;
	}
	
	//	支付宝交易号
	public String getTradeNo() {
		 return result.getString("trade_no");

	}
	
	public String getOutTradeNo() {
		 return result.getString("out_trade_no");

	}
	
	//买家支付宝账号
	public String getBuyerLogonId() {
		 return result.getString("buyer_logon_id");

	}
	
	//支付状态
	public String getTradeStatus() {
		 return result.getString("trade_status");

	}
	
	//
	public String getTotalAmount() {
		 return result.getString("total_amount");

	}
	
	
	//实收金额，单位为元
	public String getReceiptAmount() {
		 return result.getString("receipt_amount");

	}
	
	//本次交易打款给卖家的时间
	public String getSendPayDate() {
		 return result.getString("send_pay_date");

	}
	
	//	买家在支付宝的用户id
	public String getBuyerUserId() {
		 return result.getString("buyer_user_id");

	}
	
	public JSONObject get() {
		return result;

	}
	
}
