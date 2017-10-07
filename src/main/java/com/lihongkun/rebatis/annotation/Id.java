package com.lihongkun.rebatis.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.lihongkun.rebatis.keygen.KeyGeneratorType;

/**
 * 标注主键字段,设置主键生成规则
 * @author lihongkun
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Id {
	
	/**
	 * 设置主键生成策略
	 * @return 主键生成策略,见枚举值
	 */
	KeyGeneratorType value() default KeyGeneratorType.NONE;
}
