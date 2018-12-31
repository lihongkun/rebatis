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

/**
 * <p>根据记录ID批量删除 </p>
 * <pre>
 * delete from [tableName] 
 * where [id] in  
 * &lt;foreach collection="list" item="item" index="index" open="(" close=")" seperate=","&gt;
 * 	#{item} 
 * &lt;/foreach&gt;
 * </pre>
 * @author lihongkun
 */
public class DeleteAllByIdRegistry extends GenericMappedStatementRegistry {

	public DeleteAllByIdRegistry(RegistryArgs args) {
		super(args);
	}

	@Override
	public String getStatementId() {
		return namespace+".deleteAllById";
	}

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.DELETE;
	}
	
	@Override
	public SqlSource getSqlSource() {
		
		List<SqlNode> contents = new ArrayList<>();
		contents.add(new TextSqlNode(" delete from "+tableName));
		
		List<SqlNode> whereStatment = new ArrayList<>();
		whereStatment.add(new TextSqlNode(" where "+getColumnNameByField(idField)+" in "));
		whereStatment.add(new ForEachSqlNode(configuration, new TextSqlNode("#{item}"), "list", "index", "item", "(", ")", ","));
		
		contents.add(new IfSqlNode(new MixedSqlNode(whereStatment), "list != null "));
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}
	
	@Override
	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}

	@Override
    public Class<?> getResultType() {
		return Integer.class;
	}
}
