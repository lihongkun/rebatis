package com.lihongkun.rebatis.statement.registry;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;

import com.lihongkun.rebatis.statement.RegistryArgs;

/**
 * 新增语句
 * 
 * insert into [tableName]
 * (
 * 		<if test=" someField1 != null">some_field1</if>,
 * 		<if test=" someField2 != null">some_field2</if>,
 * 		<if test=" someField3 != null">some_field3</if>
 * )
 * values (
 * 		<if test=" someField1 != null">#{someField1}</if>,
 * 		<if test=" someField2 != null">#{someField2}</if>,
 * 		<if test=" someField3 != null">#{someField3}</if>
 * )
 * 
 * @author lihongkun
 */
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
