package com.lihongkun.rebatis.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 不进行序列化的字段
 * 
 * @author lihongkun
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Transient {

}
