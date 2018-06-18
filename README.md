## 功能简介

提供通用SQL注册、分页、SQL打印功能,可以单独使用.如果使用通用SQL注册,将同时开启分页功能.

#### 通用SQL注册

基础的SQL不用再手工写,增加工程效率

- 类似JPA(Hibernate),使用实体注解.
- 实现通用的Mapper接口及其SQL注册,无代码生成.
- 对Mybatis使用无任何侵入,Mybatis原有的使用方式可继续使用.

#### 分页功能

Mybatis的分页参数RowBounds存在序列化问题,业界里面的分页插件在使用的时候通常需要自己手动进行转换,且返回值在一些序列化中会丢失.

- 分页参数不再继承RowBounds,采用自己写的可序列化的Paginator.
- 重写Configuration注册Mapper相关的类,增加分页的功能


#### SQL打印功能

- 提供通用的SQL打印Inteceptor.


## 开始使用
- 实体Bean 增加注解    
自定义注解Table对应了数据库的表名
```java
@Table("t_category")
public class Category {

	@Id(KeyGeneratorType.AUTO)
	private Long id;
	
	private String code;
	
	private String name;
	
	private Integer type;
	
	private String cover;
	...
}
```

- Mapper 编写   
BaseMapper自定义了通用的数据库方法
```java
public interface CategoryMapper extends BaseMapper<Category, Long>{

	@Select("select id,code,name,type,cover,created_on createdOn,created_by createdBy,updated_on updatedOn,updated_by updatedBy  from t_category where type = #{type}")
	public List<Category> findByType(@Param("type") Integer type);
	
	@Select("select id,code,name,type,cover,created_on createdOn,created_by createdBy,updated_on updatedOn,updated_by updatedBy  from t_category where type = #{type}")
	public Pagination<Category> findByTypePage(@Param("type") Integer type,Paginator paginator);
	
	@Select("select id,code,name,type,cover,created_on createdOn,created_by createdBy,updated_on updatedOn,updated_by updatedBy  from t_category where type = #{type}")
	public Pagination<Category> findAll2(Category category,Paginator paginator);
}
```

- SqlSessionFactoryBean 等相关Bean 注册 
```java
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages="com.lihongkun.rebatis.sample.mapper")
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
		// 只需要注入 RebatisConfiguration 并选择启用的功能
		sqlSessionFactory.setConfiguration(new RebatisConfiguration(true, true, true));

		return sessionFactory.getObject();
	}

}
```
## 测试用例

测试使用了内存数据库h2,源码目录下的RebatisTest和PaginationTest可以直接运行.

其中RebatisTest 演示了BaseMapper的方法,PaginationTest演示了单独使用分页功能的几个方法.

## 原理

主要扩展了Configuration,在注册Mapper的时候同时对BaseMapper中已定义的接口进行对应的MapperStatement的注册达到了不用自己手写SQL的目的.

## 联系方式

QQ群：477318923

公众号 

![image](http://www.lihongkun.com/qrcode_for_gh_7e64b0d0bc1e_258.jpg)