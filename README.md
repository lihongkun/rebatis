## 功能简介
- 类似JPA(Hibernate),使用实体注解.
- 实现通用的Mapper接口及其SQL注册,无代码生成.
- 对Mybatis使用无任何侵入,Mybatis原有的使用方式可继续使用.

## 开始使用
- 实体Bean 增加注解    
自定义注解Table对应了数据库的表名
```java
@Table("t_sample")
public class Sample {
	@Id(KeyGeneratorType.AUTO)
	private Long id;
	private String code;
	private String name;
	...
}
```

- Mapper 编写   
BaseMapper自定义了通用的数据库方法
```java
public interface SampleMapper extends BaseMapper<Sample, Long> {
	
}
```

- SqlSessionFactoryBean 等相关Bean 注册 
```java
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages="com.lihongkun.rebatis.sample.mapper",factoryBean=RebatisMapperFactoryBean.class)
public class MybatisConfiguration {
    
	@Autowired
	private DataSource dataSource;
	
	@Bean
	@ConditionalOnMissingBean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory() throws Exception {

		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);

		return sessionFactory.getObject();
	}

}
```
## 测试用例

源码目录下的RebatisTest可以直接运行.使用了内存数据库h2

## 原理

主要扩展了MapperFactoryBean的RebatisMapperFactoryBean在执行原有的扫描Mapper接口注册到Mybatis之前,进行对应的MapperStatement的注册达到了不用自己手写SQL的目的.