package com.lihongkun.rebatis.proxy;

import java.lang.reflect.Method;

import org.apache.ibatis.builder.annotation.MethodResolver;

public class RebatisMethodResolver extends MethodResolver {
	private final RebatisMapperAnnotationBuilder annotationBuilder;
	private Method method;

	public RebatisMethodResolver(RebatisMapperAnnotationBuilder annotationBuilder, Method method) {
		super(annotationBuilder, method);
		this.annotationBuilder = annotationBuilder;
		this.method = method;
	}

	public void resolve() {
		annotationBuilder.parseStatement(method);
	}
}
