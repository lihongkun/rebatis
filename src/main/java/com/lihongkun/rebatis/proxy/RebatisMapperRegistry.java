package com.lihongkun.rebatis.proxy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.lihongkun.rebatis.statement.MappedStatementRegistryComposite;

/**
 * @author lihongkun
 */
public class RebatisMapperRegistry {

	private final Configuration config;
	private final Map<Class<?>, RebatisMapperProxyFactory<?>> knownMappers;

	private boolean enableRegisterStatement;
	
	public RebatisMapperRegistry(Configuration config,boolean enableRegisterStatement) {
	    this.config = config;
	    this.enableRegisterStatement = enableRegisterStatement;
	    this.knownMappers = new HashMap<>();
	  }

	@SuppressWarnings("unchecked")
	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
		final RebatisMapperProxyFactory<T> mapperProxyFactory = (RebatisMapperProxyFactory<T>) knownMappers.get(type);
		if (mapperProxyFactory == null) {
			throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
		}
		try {
			return mapperProxyFactory.newInstance(sqlSession);
		} catch (Exception e) {
			throw new BindingException("Error getting mapper instance. Cause: " + e, e);
		}
	}

	public <T> boolean hasMapper(Class<T> type) {
		return knownMappers.containsKey(type);
	}

	public <T> void addMapper(Class<T> type) {
		if (type.isInterface()) {
			if (hasMapper(type)) {
				throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
			}
			boolean loadCompleted = false;
			try {
				knownMappers.put(type, new RebatisMapperProxyFactory<>(type));
				// It's important that the type is added before the parser is
				// run
				// otherwise the binding may automatically be attempted by the
				// mapper parser. If the type is already known, it won't try.
				MapperAnnotationBuilder parser = new RebatisMapperAnnotationBuilder(config, type);
				parser.parse();
				loadCompleted = true;
				
				// 注册基础SQL
				if(this.enableRegisterStatement){
					new MappedStatementRegistryComposite(this.config, type.getName(), getEntityClass(type)).registerMappedStatement();
				}
				
			} finally {
				if (!loadCompleted) {
					knownMappers.remove(type);
				}
			}
		}
	}

	public Collection<Class<?>> getMappers() {
		return Collections.unmodifiableCollection(knownMappers.keySet());
	}

	public void addMappers(String packageName, Class<?> superType) {
		ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
		resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
		Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
		for (Class<?> mapperClass : mapperSet) {
			addMapper(mapperClass);
		}
	}

	public void addMappers(String packageName) {
		addMappers(packageName, Object.class);
	}
	
	private Class<?> getEntityClass(Class<?> mapperInterface) {
		Type[] types = mapperInterface.getGenericInterfaces();
		ParameterizedType type = (ParameterizedType) types[0];
		Type[] actualTypes = type.getActualTypeArguments();
		return (Class<?>) actualTypes[0];
	}
}
