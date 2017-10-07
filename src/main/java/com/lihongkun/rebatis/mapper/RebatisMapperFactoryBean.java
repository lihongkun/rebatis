package com.lihongkun.rebatis.mapper;

import static org.springframework.util.Assert.notNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;

import com.lihongkun.rebatis.statement.MappedStatementRegistryComposite;

public class RebatisMapperFactoryBean<T> extends org.mybatis.spring.mapper.MapperFactoryBean<T> {

	public RebatisMapperFactoryBean() {
		super();
	}

	public RebatisMapperFactoryBean(Class<T> mapperInterface) {
		super(mapperInterface);
	}

	protected void checkDaoConfig() {

		notNull(getMapperInterface(), "Property 'mapperInterface' is required");

		Configuration configuration = getSqlSession().getConfiguration();
		if (!configuration.hasMapper(this.getMapperInterface())) {
			try {
				configuration.addMapper(this.getMapperInterface());
			} catch (Exception e) {
				logger.error("Error while adding the mapper '" + this.getMapperInterface() + "' to configuration.", e);
				throw new IllegalArgumentException(e);
			} finally {
				ErrorContext.instance().reset();
			}
		}

		String interfaceName = getMapperInterface().getName();
		if (!configuration.hasStatement(interfaceName)) {
			new MappedStatementRegistryComposite(configuration, interfaceName, getEntityClass())
					.registerMappedStatement();
		}
	}

	private Class<?> getEntityClass() {
		Type[] types = getMapperInterface().getGenericInterfaces();
		ParameterizedType type = (ParameterizedType) types[0];
		Type[] actualTypes = type.getActualTypeArguments();
		return (Class<?>) actualTypes[0];
	}

}
