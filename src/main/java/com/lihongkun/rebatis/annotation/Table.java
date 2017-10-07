package com.lihongkun.rebatis.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

/**
 * Mybatis Mapper Entity
 * MybatisMapperSqlSessionFactoryBean 启动的时候扫描此注解的实体
 * 进行MappedStatement的注册
 * 
 * @author lihongkun
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Table {
	
	/**
	 * 表名,如无的话为实体类名称下划线规则
	 * @return 映射的表名
	 */
	String value() default StringUtils.EMPTY;

}
