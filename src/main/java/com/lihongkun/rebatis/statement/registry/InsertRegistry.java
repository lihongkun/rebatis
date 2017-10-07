package com.lihongkun.rebatis.statement.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;

import com.lihongkun.rebatis.statement.RegistryArgs;

public class InsertRegistry extends GenericInsertRegistry {

	public InsertRegistry(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace + ".insert";
	}

	public SqlSource getSqlSource() {
		
		List<SqlNode> contents = new ArrayList<SqlNode>();
		contents.add(new TextSqlNode("insert into " + tableName + " "));
		contents.add(getInsertColumnStatement());
		contents.add(getInsertFieldsStatement());
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}
}
