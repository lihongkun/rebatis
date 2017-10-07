package com.lihongkun.rebatis.statement.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;

import com.lihongkun.rebatis.statement.GenericMappedStatementRegistry;
import com.lihongkun.rebatis.statement.RegistryArgs;

public class CountRegistry extends GenericMappedStatementRegistry {

	public CountRegistry(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace + ".count";
	}

	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	public SqlSource getSqlSource() {
		
		List<SqlNode> contents = new ArrayList<SqlNode>();
		contents.add(new TextSqlNode("select count(1) from " + tableName + " "));
		contents.add(getWhereStatement());
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}

	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}

	public Class<?> getResultType() {
		return Integer.class;
	}
}
