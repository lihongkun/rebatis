package com.lihongkun.rebatis.statement.registry;

import java.lang.reflect.Field;
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

/**
 * 
 * 根据条件查询列表
 * <pre>
 * select [all fields] from [tableName]
 * &lt;where&gt;
 * 	&lt;if test="a != null"&gt; and a = #{a} &lt;if&gt;
 * 	&lt;if test="b != null"&gt; and b = #{b} &lt;if&gt;
 * &lt;/where&gt;
 * </pre>
 * 
 * @author lihongkun
 */
public class FindAllRegistry extends GenericMappedStatementRegistry {

	public FindAllRegistry(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace+".findAll";
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
		
		List<SqlNode> contents = new ArrayList<>();
		contents.add(new TextSqlNode(sql.toString()));
		contents.add(getWhereStatement());
		
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}
	
	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}
	
	public Class<?> getResultType() {
		return entityClass;
	}
}
