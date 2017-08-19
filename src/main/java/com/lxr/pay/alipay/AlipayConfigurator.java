package com.lxr.pay.alipay;

public class AlipayConfigurator {
	
	
	
	// 商户appid
	public  String appId = "2016080300159818";
	//商户门店编号
	public String storeId = "";
	//
	public String sellerId="fjncfe6410@sandbox.com";

	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public  String notifyUrl = "";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public  String returnUrl = "";
	// 返回格式		FORMAT	参数返回格式，只支持json	json（固定）
	public  String format = "json";
	// 请求和签名使用的字符编码格式，支持GBK和UTF-8	开发者根据实际工程编码配置
	public  String charset = "UTF-8";
	//应用私钥
	public  String appPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNff92Rdv4h3cu5f7JSVRj9mnxQwGEtaBXDn5pAhZ5d2hqdMmedxToACt6+cAeYvihdLOiEKFvjVCItdaKaHfLmxX6czVGCobYPtg3NWh4ND8OlnKJygI+xax1epiFnk5z1ws8f04faOArzjNhkhdfKpppKp5WzzuzPavlBPpmhuguRSBEETnBJ5wc9jSwWWex+pvLMoy77MfdzHuYhHtrqPlykUQMhGnNRoD6Muulszh4Cp002uRG07A33YefVYrFo66o6tPjaJU4GdB663c2Iuzduyeyw82+RhTlVxYUju9vMejZIfGt/Hf+q2dUfFnF8Doqoj2SL5O8Qedt9pSZAgMBAAECggEATx4TSOlLVS86f6jvzVIJtFPHbbbQFIsS+1sJaGJYetdANbbUPp3LvObLaVpaCSPCb1W4VHkELEJSs/8p1f8QbXNs2LyvH1kniaeqc0SUBBMC539M0Kt6kesqwg4pVx3sNG4cxFlTL9EO+K+2n2p3UymZahEAtP3wHgvwIIGmGsmkFmUzZhYUTjxgA2OmGUWWIget4UvBOonRk+GRzHc7+Yj27Z9nPEsmKKKcDSLhzI2FMW3NAHvAssxkS1Yg4IBZ/IDMugXSy5vGgCZTsxtKECmBnIIdV+sAjOk7XXJIuNDfkGcWcGWbqu5DxHZjfej28OZtOEcxUzHotSq1ico5uQKBgQC+hdqMCo4hJ0rwYtguf8cDWVoY0/jix8Ekkdifi+9TdjQmRX7tOZtCfOPIw/fS5oL02HeHXQFIhC+WLbWeZlhvzWcYSlTQsV6OMC3tDU9a8GYmpof9RUVfSlkmsbLsMOXQRycjzGbNgGVb91rGzsm439/eSnPH5qtcMwvxtQBqZwKBgQC+HnBLd0w6LB//bpbzgN5KFRqfi2xyWFpUcY5NihwFH1mwYgV3JGA+WsLWujaTzEGzztVOqc8CgMH6qVu62MW0dGjzyo3RyD4urr/dfoW733DlhpLtcRwuNmHtDSNjFLs9dzbNUxAzDdHMho1q2whWeTA2jYmAM6kEwYi1S9+o/wKBgHUJfwaWGVdubs+auNhfsB5zjjBATtzo/tH7MgpYiQ1JSKHWATGzOJgwZjFXiO6qofnM3ChwTrCAW0czDyWeE2ei01kNmGqW+tH38M51ZbEkimvl0xYXrpTp9Dgb4yDKrPI0M2v/TYuh+yHyonYMY8jI5hoEvHkpT00VIpo5qWazAoGACQ9ERKRbso31vGNVuCbU8OzOhvnK4iwJqep7YICLU0cqK/iP0BygpXgHvtnCzgo2u03I5mW7IqBmHf7CC0ZXLYE6mxt05KFJLUpQhEqKLui35YgjKkNwl1cqFXTB0e/j+ErdBq3FoNkwvTFQPd5QKGbAAGmAOua7UCm0zQbfkpsCgYEAmdiqxnWclXv2kmNEPInKnYdP8/86Hc9oB+tVTJY8+1CzYsMmYBDo+ZWQbdHRJvXL8Yc299goOTCbXpfwW1SSvq2KOgh0vXQesPfiAeT2cymkmD7gTJzfD9cs4P+VgzC40DB7RFNNo0OiisbycTpthZ7bQ2aH7eMvm6YbjTXZfdU=";

	//应用工钥
	public  String appPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjX3/dkXb+Id3LuX+yUlUY/Zp8UMBhLWgVw5+aQIWeXdoanTJnncU6AArevnAHmL4oXSzohChb41QiLXWimh3y5sV+nM1RgqG2D7YNzVoeDQ/DpZyicoCPsWsdXqYhZ5Oc9cLPH9OH2jgK84zYZIXXyqaaSqeVs87sz2r5QT6ZoboLkUgRBE5wSecHPY0sFlnsfqbyzKMu+zH3cx7mIR7a6j5cpFEDIRpzUaA+jLrpbM4eAqdNNrkRtOwN92Hn1WKxaOuqOrT42iVOBnQeut3NiLs3bsnssPNvkYU5VcWFI7vbzHo2SHxrfx3/qtnVHxZxfA6KqI9ki+TvEHnbfaUmQIDAQAB";

	// 支付宝公钥
	public  String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2wF+na+fGS5aWySFSnIVCulpPmLgXMa5FH52X1EI1Cub2rUuWX2YnyyWOyLcXmnm9l3vA1b74eDBsi+5TM59aWtHTCafsbxS4+URUsZYbQhrysCCc2ZMR2fMqINTU2bUsJl6qidSSNbFVH84IpElZyxOZUDK4Lt4EucmjXJw6JVknbwb0F9HrX+tnOramhF8ffej4EmJnwK8dKd8DaQf76Cp1EHfhPzW+YyRhCWl6D5janZ4bsfSbqRB2bLY4pXecKAXXoXvrjQ/xGMA1yiz9z0iFsCIDK5MSoy8nUDq1tiDPXepRMeAD2Z31QI5b7Cva2Qx/Iis8oVV2PlOX5+eaQIDAQAB";

	//商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	public  String signType = "RSA2";
	
}
