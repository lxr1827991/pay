package com.lxr.commons.utils;

import java.math.BigDecimal;

public class TypeConverUtil {

	
	public static String $Str(Object obj) {
		

        if(obj == null || obj.toString().toLowerCase().equals("null"))
            return "";
        else
            return obj.toString().trim();

	}
	
	public static BigDecimal $BigDecimal(Object obj) {
		if(obj == null || "".equals(obj.toString()) || "null".equals(obj.toString()))
            return new BigDecimal(0);
    	else 
    		return new BigDecimal(obj.toString());


	}
	
	
	public static int $int(Object fieldName) {
		if(fieldName == null || "".equals(fieldName.toString()))
            return 0;
        else
            return Integer.valueOf(fieldName.toString()).intValue();

	}
	
}
