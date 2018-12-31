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
 * <pre>
 * select [all fields] from [tableName]
 * where [id] = #{id}
 * </pre>
 * @author lihongkun
 */
public class FindByIdRegistry extends GenericMappedStatementRegistry {

	public FindByIdRegistry(RegistryArgs args) {
		super(args);
	}


	@Override
	public String getStatementId() {
		return namespace+".findById";
	}

	
	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}
	
	@Override
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
	
	@Override
	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}
	
	@Override
    public Class<?> getResultType() {
		return entityClass;
	}
}
