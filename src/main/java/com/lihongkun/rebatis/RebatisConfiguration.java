package com.lihongkun.rebatis;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.lihongkun.rebatis.interceptor.OffsetLimitInterceptor;
import com.lihongkun.rebatis.interceptor.ShowSqlInterceptor;
import com.lihongkun.rebatis.pagination.SqlMode;
import com.lihongkun.rebatis.proxy.RebatisMapperRegistry;

public class RebatisConfiguration extends Configuration {

	protected RebatisMapperRegistry rebatisMapperRegistry;
	
	public RebatisConfiguration(boolean enablePagination,boolean enableRegistStatement,boolean enableShowSql){
		this(enablePagination,enableRegistStatement,enableShowSql,SqlMode.DEFAULT);
	}
	
	public RebatisConfiguration(boolean enablePagination,boolean enableRegistStatement,boolean enableShowSql,SqlMode sqlMode){
		super();
		rebatisMapperRegistry = new RebatisMapperRegistry(this,enableRegistStatement);
		if(enableShowSql)
			this.addInterceptor(new ShowSqlInterceptor());
		if(enablePagination || enableRegistStatement){
			this.addInterceptor(new OffsetLimitInterceptor(sqlMode));
		}
	}

	public void addMappers(String packageName, Class<?> superType) {
		rebatisMapperRegistry.addMappers(packageName, superType);
	}

	public void addMappers(String packageName) {
		rebatisMapperRegistry.addMappers(packageName);
	}

	public <T> void addMapper(Class<T> type) {
		rebatisMapperRegistry.addMapper(type);
	}

	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
		return rebatisMapperRegistry.getMapper(type, sqlSession);
	}

	public boolean hasMapper(Class<?> type) {
		return rebatisMapperRegistry.hasMapper(type);
	}
}
