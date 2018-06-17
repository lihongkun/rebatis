package com.lihongkun.rebatis.statement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SetSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.apache.ibatis.session.Configuration;

import com.lihongkun.rebatis.util.StringUtil;

/**
 * 通用SQL注册器
 * @author lihongkun
 */
public abstract class GenericMappedStatementRegistry implements MappedStatementRegistry {
	
	protected Configuration configuration;
	
	protected MapperBuilderAssistant assistant;
	
	protected Field idField;
	
	protected List<Field> columnFields;
	
	protected String tableName;
	
	protected String namespace;
	
	protected Class<?> entityClass;
	
	protected String databaseId;
	
	protected LanguageDriver languageDriver;
	
	public GenericMappedStatementRegistry(RegistryArgs args){
		this.configuration = args.getConfiguration();
		this.assistant = args.getAssistant();
		this.entityClass = args.getEntityClass();
		this.tableName = args.getTableName();
		this.namespace = args.getNamespace();
		this.idField = args.getIdField();
		this.columnFields = args.getColumnFields();
		this.databaseId = configuration.getDatabaseId();
		this.languageDriver = configuration.getDefaultScriptingLanguageInstance();
	}
	
	/**
	 * java Bean 属性名称转换成对应的DB字段名称
	 * @param field java bean 属性
	 * @return 对应的数据库字段名称
	 */
	protected String getColumnNameByField(Field field){
		return StringUtil.camelToUnderScore(field.getName());
	}
	
	/**
	 * mybatis 判断Bean属性是否为空表达式
	 * @param field  java bean 属性
	 * @return field 对应的 测试表达式
	 */
	protected String getTestByField(Field field) {
		return field.getName() + "!= null";
	}
	
	/**
	 * 生成 mybatis 的 where语句 片段
	 * <pre>
	 * &lt;where&gt;
	 * 		&lt;if test=" someField1 != null "&gt; and some_field1 = #{someField1} &lt;/if&gt;
	 * 		&lt;if test=" someField2 != null "&gt; and some_field2 = #{someField2} &lt;/if&gt;
	 * &lt;/where&gt;
	 * </pre>
	 * @return where子句
	 */
	protected WhereSqlNode getWhereStatement(){
		List<SqlNode> contents = new ArrayList<SqlNode>();
		contents.add(new IfSqlNode(new TextSqlNode(" AND "+ getColumnNameByField(idField) + " = #{"+idField.getName()+"}"), getTestByField(idField)));
		for(Field field : columnFields){
			contents.add(new IfSqlNode(new TextSqlNode(" AND "+ getColumnNameByField(field) + " = #{"+field.getName()+"}"), getTestByField(field)));
		}
		return new WhereSqlNode(configuration, new MixedSqlNode(contents));
	}
	
	/**
	 * 生成mybatis 的 更新语句片段
	 * <pre>
	 * &lt;set&gt;
	 * 	&lt;if test=" someField1 != null "&gt; some_field1 = #{someField1} ,&lt;/if&gt;
	 * 	&lt;if test=" someField2 != null "&gt; some_field2 = #{someField2} ,&lt;/if&gt;
	 * &lt;/set&gt;
	 * </pre>
	 * @return 更新子句
	 */
	protected SetSqlNode getUpdateSetStatement(){
		List<SqlNode> contents = new ArrayList<SqlNode>();
		for(Field field : columnFields){
			contents.add(new IfSqlNode(new TextSqlNode(getColumnNameByField(field)+" = #{"+field.getName()+"} , "),getTestByField(field)));
		}
		return new SetSqlNode(configuration, new MixedSqlNode(contents));
	} 
	
	/**
	 * 生成mybatis 插入语句声明字段片段
	 * <pre>
	 * [insert into table]
	 * (
	 *	&lt;if test=" someField1 != null "&gt;some_field1 &lt;/if&gt;,
	 * 	&lt;if test=" someField2 != null "&gt;some_field2 &lt;/if&gt;,
	 * 	&lt;if test=" someField3 != null "&gt;some_field3 &lt;/if&gt;
	 * )
	 * </pre>
	 * @return insert 声明片段
	 */
	protected TrimSqlNode getInsertColumnStatement(){
		List<SqlNode> contents = new ArrayList<SqlNode>();
		
		List<SqlNode> sqlNodes = new ArrayList<SqlNode>();
		sqlNodes.add(new TextSqlNode(getColumnNameByField(idField) + ","));
		contents.add(new IfSqlNode(new MixedSqlNode(sqlNodes), getTestByField(idField)));
		
		for(Field field : columnFields){
			sqlNodes = new ArrayList<SqlNode>();
			sqlNodes.add(new TextSqlNode(getColumnNameByField(field) + ","));
			contents.add(new IfSqlNode(new MixedSqlNode(sqlNodes), getTestByField(field)));
		}
		
		return new TrimSqlNode(configuration, new MixedSqlNode(contents), "(", null, ")", ",");
	}
	
	/**
	 * 生成mybatis 插入语句片段
	 * <pre>
	 * values (
	 * 	&lt;if test=" someField1 != null "&gt;#{someField1}&lt;/if&gt;,
	 * 	&lt;if test=" someField2 != null "&gt;#{someField2}&lt;/if&gt;,
	 * 	&lt;if test=" someField3 != null "&gt;#{someField3}&lt;/if&gt;
	 * )
	 * </pre>
	 * @return insert值片段
	 */
	public TrimSqlNode getInsertFieldsStatement(){
		List<SqlNode> contents = new ArrayList<SqlNode>();
		
		contents.add(new IfSqlNode(new TextSqlNode("#{" + idField.getName() + "},"), getTestByField(idField)));
		for(Field field : columnFields){
			contents.add(new IfSqlNode(new TextSqlNode("#{" + field.getName() + "},"), getTestByField(field)));
		}
		
		return new TrimSqlNode(configuration, new MixedSqlNode(contents), " VALUES (", null, ")", ",");
	}
	
	/**
	 * @return SQLID
	 */
	public abstract String getStatementId();
	
	/**
	 * @return SQL操作类型
	 */
	public abstract SqlCommandType getSqlCommandType();
	
	/**
	 * @return SQL源
	 */
	public abstract SqlSource getSqlSource();
	
	/**
	 * @return 主键生成策略
	 */
	public abstract KeyGenerator getKeyGenerator();
	
	/**
	 * @return 返回类型
	 */
	public abstract Class<?> getResultType();
	
	/**
	 * 注册SQL
	 */
	public void registerMappedStatement() {
		
		Integer fetchSize = null;
		String parameterMap = null;
		Class<?> parameterType = null;
		String resultMap = null;
		ResultSetType resultSetType = null;
		boolean flushCache = true;
		boolean useCache = false;
		boolean resultOrdered = false;
		String keyProperty = idField.getName();
		String keyColumn = getColumnNameByField(idField);

		assistant.addMappedStatement(getStatementId(), getSqlSource(), StatementType.PREPARED, getSqlCommandType(),
				fetchSize, configuration.getDefaultStatementTimeout(), parameterMap, parameterType, resultMap,
				getResultType(), resultSetType, flushCache, useCache, resultOrdered, getKeyGenerator(), keyProperty,
				keyColumn, databaseId, languageDriver);
	}
}
