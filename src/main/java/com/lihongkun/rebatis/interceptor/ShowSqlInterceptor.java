package com.lihongkun.rebatis.interceptor;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lihongkun.rebatis.util.StringUtil;

/**
 * @author lihongkun
 */
@Intercepts({
	@Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class }),
	@Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class,RowBounds.class, ResultHandler.class })
})
public class ShowSqlInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShowSqlInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		String sqlId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		Object returnValue;
		long start = System.currentTimeMillis();
		returnValue = invocation.proceed();
		long time = (System.currentTimeMillis() - start);
		String sql = getSql(configuration, boundSql, sqlId, time);
		LOGGER.info(sql);
		return returnValue;
	}

	private static String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time) {
		String sql = showSql(configuration, boundSql);
		return sqlId +
				"[" +
				sql +
				"][" +
				time +
				"ms]";
	}

	private static String getParameterValue(Object obj) {
		String value;
		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new java.util.Date()) + "'";
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}

		}
		return value;
	}

	private static String showSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = StringUtils.replaceAll(boundSql.getSql(), "[\\s]+", " ");
		
		List<Object> params = new ArrayList<>();
		
		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				params.add(getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						params.add(getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						params.add(getParameterValue(obj));
					}
				}
			}
		}
		
		if(params.size() > 0){
			sql = StringUtil.replace(sql, "?", params.toArray());
		}
		
		return sql;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

}
