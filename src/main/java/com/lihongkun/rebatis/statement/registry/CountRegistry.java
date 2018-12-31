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
 * count 语句
 * 
 * <pre>
 * select count(1) from [tableName] 
 * &lt;where&gt;
 * 	&lt;if test="a != null "&gt; and a = #{a} &lt;/if&gt; 
 * 	&lt;if test="b != null "&gt; and b = #{b} &lt;/if&gt; 
 * &lt;/where&gt;
 * </pre>
 * 
 * @author lihongkun
 */
public class CountRegistry extends GenericMappedStatementRegistry {

	public CountRegistry(RegistryArgs args) {
		super(args);
	}

	@Override
	public String getStatementId() {
		return namespace + ".count";
	}

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public SqlSource getSqlSource() {
		
		List<SqlNode> contents = new ArrayList<SqlNode>();
		contents.add(new TextSqlNode("select count(1) from " + tableName + " "));
		contents.add(getWhereStatement());
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}

	@Override
	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}

	@Override
    public Class<?> getResultType() {
		return Long.class;
	}
}
