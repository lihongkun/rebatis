package com.lihongkun.rebatis;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.lihongkun.rebatis.interceptor.OffsetLimitInterceptor;
import com.lihongkun.rebatis.interceptor.ShowSqlInterceptor;
import com.lihongkun.rebatis.pagination.SqlMode;
import com.lihongkun.rebatis.proxy.RebatisMapperRegistry;

/**
 * Rebatis 配置类
 * @author lihongkun
 */
public class RebatisConfiguration extends Configuration {

	protected RebatisMapperRegistry rebatisMapperRegistry;
	
	/**
	 * 配置构造函数
	 * @param enablePagination			是否启用分页功能
	 * @param enableRegistStatement		是否注册基础SQL
	 * @param enableShowSql				是否打印SQL日志
	 */
	public RebatisConfiguration(boolean enablePagination,boolean enableRegistStatement,boolean enableShowSql){
		this(enablePagination,enableRegistStatement,enableShowSql,SqlMode.DEFAULT);
	}
	
	/**
	 * 配置构造函数
	 * @param enablePagination			是否启用分页功能
	 * @param enableRegistStatement		是否注册基础SQL
	 * @param enableShowSql				是否打印SQL日志
	 * @param sqlMode					SQL服务类型,区分分页SQL,默认MySQL,支持Oracle,PostgreSQL  {@link SqlMode}
	 */
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
