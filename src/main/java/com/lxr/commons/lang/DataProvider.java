package com.lxr.commons.lang;

import java.util.Map;

public interface DataProvider {

	String getString(String key);
	
	String getTemplateString(String key,Map<String, Object> data);
	
}
