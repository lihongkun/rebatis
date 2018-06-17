package com.lihongkun.rebatis.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;

public class RebatisMapperProxy<T> implements InvocationHandler, Serializable {

	private static final long serialVersionUID = -9143566202465236300L;
	private final SqlSession sqlSession;
	private final Class<T> mapperInterface;
	private final Map<Method, RebatisMapperMethod> methodCache;

	public RebatisMapperProxy(SqlSession sqlSession, Class<T> mapperInterface,
			Map<Method, RebatisMapperMethod> methodCache) {
		this.sqlSession = sqlSession;
		this.mapperInterface = mapperInterface;
		this.methodCache = methodCache;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			try {
				return method.invoke(this, args);
			} catch (Throwable t) {
				throw ExceptionUtil.unwrapThrowable(t);
			}
		}
		final RebatisMapperMethod mapperMethod = cachedPaginationMapperMethod(method);
		return mapperMethod.execute(sqlSession, args);
	}

	private RebatisMapperMethod cachedPaginationMapperMethod(Method method) {
		RebatisMapperMethod mapperMethod = methodCache.get(method);
		if (mapperMethod == null) {
			mapperMethod = new RebatisMapperMethod(mapperInterface, method, sqlSession.getConfiguration());
			methodCache.put(method, mapperMethod);
		}
		return mapperMethod;
	}

}
