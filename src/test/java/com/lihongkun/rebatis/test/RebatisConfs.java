package com.lihongkun.rebatis.test;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.lihongkun.rebatis.interceptor.ShowSqlInterceptor;
import com.lihongkun.rebatis.mapper.RebatisMapperFactoryBean;

@Configuration
@EnableTransactionManagement
@MapperScan(value="com.lihongkun.rebatis.test.mapper",factoryBean=RebatisMapperFactoryBean.class)
public class RebatisConfs {

	@Bean(initMethod="init")
	public DataSource dataSource(){
		
		DruidDataSource dataSource = new DruidDataSource();
		
		dataSource.setUrl("jdbc:h2:mem:test;MODE=MYSQL;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:/h2_init.sql'");
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		
		return dataSource;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception{
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource());
		sqlSessionFactory.setPlugins(new Interceptor[]{new ShowSqlInterceptor()});
		
		return sqlSessionFactory.getObject();
	}
	
}
