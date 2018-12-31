package com.lihongkun.rebatis.statement;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;


/**
 * Statement registry  arguments
 * @author lihongkun
 */
public class RegistryArgs {

	/**
	 * Mybatis Configuration
	 */
	private Configuration configuration;

	/**
	 * Mybatis Mapper registry assistant
	 */
	private MapperBuilderAssistant assistant;

	/**
	 * Mapped entity
	 */
	private Class<?> entityClass;

	/**
	 * Mapped entity's ID column field
	 */
	private Field idField;

	/**
	 * Mapped entity's other column field except ID column
	 */
	private List<Field> columnFields;

	/**
	 * Mapped entity's database table name
	 */
	private String tableName;

	/**
	 * Mybatis mapper's namespace
	 */
	private String namespace;
	
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public MapperBuilderAssistant getAssistant() {
		return assistant;
	}
	public void setAssistant(MapperBuilderAssistant assistant) {
		this.assistant = assistant;
	}
	public Class<?> getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	public Field getIdField() {
		return idField;
	}
	public void setIdField(Field idField) {
		this.idField = idField;
	}
	public List<Field> getColumnFields() {
		return columnFields;
	}
	public void setColumnFields(List<Field> columnFields) {
		this.columnFields = columnFields;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
}
