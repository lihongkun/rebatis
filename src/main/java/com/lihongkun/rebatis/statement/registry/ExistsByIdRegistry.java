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
 * <p>根据ID判断记录是否存在</p>
 * <pre>
 * select count(1) from [tableName] where [id] = #{id}
 * </pre>
 * @author lihongkun
 */
public class ExistsByIdRegistry extends GenericMappedStatementRegistry {

	public ExistsByIdRegistry(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace+".existsById";
	}

	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	public SqlSource getSqlSource() {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select count(1) from ").append(tableName).append(" where ").append(getColumnNameByField(idField));
		sql.append(" = #{").append(idField.getName()).append("}");
		
		return new DynamicSqlSource(configuration, new TextSqlNode(sql.toString()));
	}

	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}

	public Class<?> getResultType() {
		return Boolean.class;
	}
}
