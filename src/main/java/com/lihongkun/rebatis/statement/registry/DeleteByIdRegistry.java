package com.lihongkun.rebatis.statement.registry;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;

import com.lihongkun.rebatis.statement.GenericMappedStatementRegistry;
import com.lihongkun.rebatis.statement.RegistryArgs;

/**
 * 
 * <p>根据记录ID删除单条记录</p>
 * <pre>
 * delete from [tableName] 
 * where [id] = #{id}
 * </pre>
 * @author lihongkun
 */
public class DeleteByIdRegistry extends GenericMappedStatementRegistry {

	public DeleteByIdRegistry(RegistryArgs args) {
		super(args);
	}

	@Override
	public String getStatementId() {
		return namespace + ".deleteById";
	}

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	@Override
	public SqlSource getSqlSource() {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" delete from ").append(tableName);
		sql.append(" where ").append(getColumnNameByField(idField)).append(" = #{id}");
		
		return new DynamicSqlSource(configuration, new TextSqlNode(sql.toString()));
	}

	@Override
	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}
	
	@Override
    public Class<?> getResultType() {
		return int.class;
	}
}
