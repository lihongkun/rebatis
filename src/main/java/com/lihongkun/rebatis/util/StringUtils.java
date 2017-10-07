package com.lihongkun.rebatis.util;


public class StringUtils {
	
	/**
	 * 驼峰命名风格的字符串转换为下划线的字符串
	 * @param camel		驼峰命名风格的字符串
	 * @return			下划线的字符串
	 */
	public static final String camelToUnderScore(String camel){
		return org.apache.commons.lang3.StringUtils.join(org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase(camel),"_").toLowerCase();
	}
	
}
