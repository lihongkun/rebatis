package com.lihongkun.rebatis.statement.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;

import com.lihongkun.rebatis.statement.GenericMappedStatementRegistry;
import com.lihongkun.rebatis.statement.RegistryArgs;

public class DeleteAllByIdRegistry extends GenericMappedStatementRegistry {

	public DeleteAllByIdRegistry(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace+".deleteAllById";
	}

	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}
	
	public SqlSource getSqlSource() {
		
		List<SqlNode> contents = new ArrayList<>();
		contents.add(new TextSqlNode(" delete from "+tableName));
		
		List<SqlNode> whereStatment = new ArrayList<>();
		whereStatment.add(new TextSqlNode(" where id in "));
		whereStatment.add(new ForEachSqlNode(configuration, new TextSqlNode("#{item}"), "list", "index", "item", "(", ")", ","));
		
		contents.add(new IfSqlNode(new MixedSqlNode(whereStatment), "list != null "));
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}
	
	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}

	public Class<?> getResultType() {
		return Integer.class;
	}
}
