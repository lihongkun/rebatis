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

/**
 * 根据条件删除
 * 
 * delete from [tableName]
 * <where>
 * 		<if test="a != null"> and a = #{a}</if>
 * 		<if test="b != null"> and b = #{b}</if>
 * </where>
 * 
 * 
 * @author lihongkun
 */
public class DeleteRegistry extends GenericMappedStatementRegistry {

	public DeleteRegistry(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace + ".delete";
	}

	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}

	public SqlSource getSqlSource() {
		
		List<SqlNode> contents = new ArrayList<>();
		contents.add(new TextSqlNode("delete from "+ tableName));
		contents.add(getWhereStatement());
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}

	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}

	public Class<?> getResultType() {
		return int.class;
	}
}
