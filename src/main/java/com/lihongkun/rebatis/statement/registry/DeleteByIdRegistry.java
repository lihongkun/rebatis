package com.lihongkun.rebatis.statement.registry;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;

import com.lihongkun.rebatis.statement.GenericMappedStatementRegistry;
import com.lihongkun.rebatis.statement.RegistryArgs;

public class DeleteByIdRegistry extends GenericMappedStatementRegistry {

	public DeleteByIdRegistry(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace + ".deleteById";
	}

	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	public SqlSource getSqlSource() {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" delete from ").append(tableName);
		sql.append(" where ").append(getColumnNameByField(idField)).append(" = #{id}");
		
		return new DynamicSqlSource(configuration, new TextSqlNode(sql.toString()));
	}

	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}
	
	public Class<?> getResultType() {
		return int.class;
	}
}
