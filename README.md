## 功能简介
- 类似JPA(Hibernate),使用实体注解,自动注册SQL.
- 实现通用的Mapper接口和SQL注册,无代码生成.
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
