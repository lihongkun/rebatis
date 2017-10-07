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

import com.lihongkun.rebatis.util.StringUtils;

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
	
	protected String getColumnNameByField(Field field){
		return StringUtils.camelToUnderScore(field.getName());
	}
	
	protected String getTestByField(Field field) {
		return field.getName() + "!= null";
	}
	
	protected WhereSqlNode getWhereStatement(){
		List<SqlNode> contents = new ArrayList<SqlNode>();
		contents.add(new IfSqlNode(new TextSqlNode(" AND "+ getColumnNameByField(idField) + " = #{"+idField.getName()+"}"), getTestByField(idField)));
		for(Field field : columnFields){
			contents.add(new IfSqlNode(new TextSqlNode(" AND "+ getColumnNameByField(field) + " = #{"+field.getName()+"}"), getTestByField(field)));
		}
		return new WhereSqlNode(configuration, new MixedSqlNode(contents));
	}
	
	protected SetSqlNode getUpdateSetStatement(){
		List<SqlNode> contents = new ArrayList<SqlNode>();
		for(Field field : columnFields){
			contents.add(new IfSqlNode(new TextSqlNode(getColumnNameByField(field)+" = #{"+field.getName()+"} , "),getTestByField(field)));
		}
		return new SetSqlNode(configuration, new MixedSqlNode(contents));
	} 
	
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
	
	public TrimSqlNode getInsertFieldsStatement(){
		List<SqlNode> contents = new ArrayList<SqlNode>();
		
		contents.add(new IfSqlNode(new TextSqlNode("#{" + idField.getName() + "},"), getTestByField(idField)));
		for(Field field : columnFields){
			contents.add(new IfSqlNode(new TextSqlNode("#{" + field.getName() + "},"), getTestByField(field)));
		}
		
		return new TrimSqlNode(configuration, new MixedSqlNode(contents), " VALUES (", null, ")", ",");
	}
	
	/**
	 * SQLID
	 */
	public abstract String getStatementId();
	
	/**
	 * SQL操作类型
	 */
	public abstract SqlCommandType getSqlCommandType();
	
	/**
	 * SQL源
	 */
	public abstract SqlSource getSqlSource();
	
	/**
	 * 主键生成策略
	 */
	public abstract KeyGenerator getKeyGenerator();
	
	/**
	 * 返回类型
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