package com.lihongkun.rebatis.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	/**
	 * 驼峰命名风格的字符串转换为下划线的字符串
	 * 
	 * @param camel 驼峰命名风格的字符串
	 * @return 下划线的字符串
	 */
	public static final String camelToUnderScore(String camel) {
		return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(camel), "_").toLowerCase();
	}

	public static String replace(String text, String repl, Object[] with) {
		if (StringUtils.isEmpty(text) || StringUtils.isEmpty(repl) || (with == null) || (with.length == 0)) {
			return text;
		}

		if (text.indexOf(repl) == -1) {
			return text;
		}

		StringBuffer resultText = new StringBuffer();

		String[] splitText = StringUtils.split(text, repl);

		if (text.endsWith(repl)) {
			splitText = (String[]) ArrayUtils.addAll(splitText, new String[] {""});
		}

		for (int i = 0; i < splitText.length; ++i) {
			resultText.append(splitText[i]);
			if (i + 1 != splitText.length) {
				if (with.length > i) {
					resultText.append(with[i]);
				} else {
					resultText.append(repl);
				}
			}
		}

		return resultText.toString();
	}
}
