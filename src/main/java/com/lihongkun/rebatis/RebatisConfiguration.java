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
	 * @param enableRegisterStatement		是否注册基础SQL
	 * @param enableShowSql				是否打印SQL日志
	 */
	public RebatisConfiguration(boolean enablePagination,boolean enableRegisterStatement,boolean enableShowSql){
		this(enablePagination,enableRegisterStatement,enableShowSql,SqlMode.DEFAULT);
	}
	
	/**
	 * 配置构造函数
	 * @param enablePagination			是否启用分页功能
	 * @param enableRegisterStatement		是否注册基础SQL
	 * @param enableShowSql				是否打印SQL日志
	 * @param sqlMode					SQL服务类型,区分分页SQL,默认MySQL,支持Oracle,PostgreSQL  {@link SqlMode}
	 */
	public RebatisConfiguration(boolean enablePagination,boolean enableRegisterStatement,boolean enableShowSql,SqlMode sqlMode){
		super();
		rebatisMapperRegistry = new RebatisMapperRegistry(this,enableRegisterStatement);
		if(enableShowSql){
			this.addInterceptor(new ShowSqlInterceptor());
		}
		if(enablePagination || enableRegisterStatement){
			this.addInterceptor(new OffsetLimitInterceptor(sqlMode));
		}
	}

	@Override
	public void addMappers(String packageName, Class<?> superType) {
		rebatisMapperRegistry.addMappers(packageName, superType);
	}

	@Override
	public void addMappers(String packageName) {
		rebatisMapperRegistry.addMappers(packageName);
	}

	@Override
	public <T> void addMapper(Class<T> type) {
		rebatisMapperRegistry.addMapper(type);
	}

	@Override
	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
		return rebatisMapperRegistry.getMapper(type, sqlSession);
	}

	@Override
	public boolean hasMapper(Class<?> type) {
		return rebatisMapperRegistry.hasMapper(type);
	}
}
