package com.lihongkun.rebatis.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ReflectionUtils {

	/**
	 * 查找类中,拥有某个注解的字段,如果不存在返回空
	 * @param clazz			类的Class
	 * @param annoClazz		注解Class
	 * @return				字段Field
	 */
	public static Field findFieldWithAnnoation(Class<?> clazz,Class<?> annoClazz) {
		for (Field field : clazz.getDeclaredFields()) {
			Annotation[] annos = field.getDeclaredAnnotations();
			for(Annotation anno : annos){
				if(annoClazz.isAssignableFrom(anno.getClass())){
					return field;
				}
			}
		}
		
		return null;
	}
}
