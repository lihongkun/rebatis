package com.lihongkun.rebatis.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;

public class RebatisMapperProxyFactory<T> {

	private final Class<T> mapperInterface;
	private final Map<Method, RebatisMapperMethod> methodCache = new ConcurrentHashMap<Method, RebatisMapperMethod>();

	public RebatisMapperProxyFactory(Class<T> mapperInterface) {
	    this.mapperInterface = mapperInterface;
	}

	public Class<T> getMapperInterface() {
		return mapperInterface;
	}

	public Map<Method, RebatisMapperMethod> getMethodCache() {
		return methodCache;
	}

	@SuppressWarnings("unchecked")
	protected T newInstance(RebatisMapperProxy<T> mapperProxy) {
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface },
				mapperProxy);
	}

	public T newInstance(SqlSession sqlSession) {
		final RebatisMapperProxy<T> mapperProxy = new RebatisMapperProxy<T>(sqlSession, mapperInterface, methodCache);
		return newInstance(mapperProxy);
	}

}
