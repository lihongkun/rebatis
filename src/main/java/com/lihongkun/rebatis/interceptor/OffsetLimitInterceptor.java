package com.lihongkun.rebatis.interceptor;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.lihongkun.rebatis.pagination.RebatisRowBounds;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }) })
public class OffsetLimitInterceptor implements Interceptor {
	private static int MAPPED_STATEMENT_INDEX = 0;
	private static int PARAMETER_INDEX = 1;
	private static int ROWBOUNDS_INDEX = 2;
	private static String LIMIT_CLAUSE = " limit %s,%s";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		final Object[] queryArgs = invocation.getArgs();
		final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		final Object parameter = queryArgs[PARAMETER_INDEX];
		final Object rowBounds = queryArgs[ROWBOUNDS_INDEX];

		if(!(rowBounds instanceof RebatisRowBounds)){
			//不是 RebatisRowBounds 直接返回,兼容有用到其他分页插件的应用
			return invocation.proceed();
		}
		
		RebatisRowBounds rebatisRowBounds = (RebatisRowBounds)rowBounds;
		
		if (rebatisRowBounds.getOffset() == RowBounds.NO_ROW_OFFSET && rebatisRowBounds.getLimit() == RowBounds.NO_ROW_LIMIT) {
			return invocation.proceed();
		}

		final BoundSql boundSql = ms.getBoundSql(parameter);

		String sql = boundSql.getSql() + String.format(LIMIT_CLAUSE, rebatisRowBounds.getOffset(),rebatisRowBounds.getLimit());
		BoundSql paginationBoundSql = paginationBoundSql(ms, boundSql, sql, boundSql.getParameterMappings(), parameter);
		
		MappedStatement paginationMappedStatement = paginationMappedStatement(ms, new BoundSqlSqlSource(paginationBoundSql));

		queryArgs[MAPPED_STATEMENT_INDEX] = paginationMappedStatement;
		queryArgs[ROWBOUNDS_INDEX] = new RowBounds();
		
		return invocation.proceed();
	}

	private BoundSql paginationBoundSql(MappedStatement ms, BoundSql boundSql, String sql,
			List<ParameterMapping> parameterMappings, Object parameter) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, parameterMappings, parameter);
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			}
		}
		return newBoundSql;
	}

	// see: MapperBuilderAssistant
	private MappedStatement paginationMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
			StringBuffer keyProperties = new StringBuffer();
			for (String keyProperty : ms.getKeyProperties()) {
				keyProperties.append(keyProperty).append(",");
			}
			keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
			builder.keyProperty(keyProperties.toString());
		}

		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}
	
	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;
		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
