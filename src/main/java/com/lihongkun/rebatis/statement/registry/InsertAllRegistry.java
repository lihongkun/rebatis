package com.lihongkun.rebatis.statement.registry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;

import com.lihongkun.rebatis.statement.RegistryArgs;

public class InsertAllRegistry extends GenericInsertRegistry {

	public InsertAllRegistry(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace + ".insertAll";
	}

	public SqlSource getSqlSource() {
		List<SqlNode> contents = new ArrayList<SqlNode>();
		contents.add(new TextSqlNode("insert into " + tableName + " "));
		contents.add(getInsertColumnStatement());
		contents.add(new TextSqlNode(" values "));
		
		List<SqlNode> eachSqlNodes = new ArrayList<SqlNode>();
		eachSqlNodes.add(new IfSqlNode(new TextSqlNode("#{item." + idField.getName() + "},"), "item."+idField.getName()+ " != null"));
		for(Field field : columnFields){
			eachSqlNodes.add(new IfSqlNode(new TextSqlNode("#{item." + field.getName() + "},"), "item."+field.getName()+ " != null"));
		}
		SqlNode eachSqlNode = new TrimSqlNode(configuration, new MixedSqlNode(eachSqlNodes), "(", null, ")", ",");
		
		contents.add(new ForEachSqlNode(configuration, eachSqlNode,"entities", "index", "item", StringUtils.EMPTY, StringUtils.EMPTY, ","));
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}
}
