package com.lxr.commons.lang;

import java.util.Map;

public abstract class AbstractDataProvider implements DataProvider{
	
	
	public String getTemplateString(String key,Map<String, Object> data) {
		
		return getString(key);
	}

}
