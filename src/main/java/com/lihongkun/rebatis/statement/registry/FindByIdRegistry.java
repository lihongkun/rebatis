package com.lihongkun.rebatis.statement.registry;

import java.lang.reflect.Field;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;

import com.lihongkun.rebatis.statement.GenericMappedStatementRegistry;
import com.lihongkun.rebatis.statement.RegistryArgs;

/**
 * 根据ID查询
 * 
 * select [all fields] from [tableName]
 * where [id] = #{id}
 * 
 * @author lihongkun
 */
public class FindByIdRegistry extends GenericMappedStatementRegistry {

	public FindByIdRegistry(RegistryArgs args) {
		super(args);
	}


	public String getStatementId() {
		return namespace+".findById";
	}

	
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}
	
	public SqlSource getSqlSource() {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select ").append(getColumnNameByField(idField)).append(" ").append(idField.getName());
		for(Field field : columnFields){
			sql.append(" , ").append(getColumnNameByField(field)).append(" ").append(field.getName());
		}
		
		sql.append(" from ").append(tableName);
		sql.append(" where ").append(getColumnNameByField(idField)).append(" = #{id}");
		
		return new DynamicSqlSource(configuration, new TextSqlNode(sql.toString()));
	}
	
	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}
	
	public Class<?> getResultType() {
		return entityClass;
	}
}
