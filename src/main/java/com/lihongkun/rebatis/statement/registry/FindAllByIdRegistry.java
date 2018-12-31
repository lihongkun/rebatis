package com.lihongkun.rebatis.statement.registry;

import java.lang.reflect.Field;
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
 * <p>根据ID列表查询记录</p>
 * <pre>
 * select [all fields] from [tableName] 
 * where [id] in  
 * &lt;foreach collection="list" item="item" index="index" open="(" close=")" seperate="," &gt;
 * 	#{item} 
 * &lt;/foreach&gt; 
 * </pre>
 * @author lihongkun
 */
public class FindAllByIdRegistry extends GenericMappedStatementRegistry {

	public FindAllByIdRegistry(RegistryArgs args) {
		super(args);
	}

	@Override
	public String getStatementId() {
		return namespace+".findAllById";
	}

	@Override
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.SELECT;
	}

	@Override
	public SqlSource getSqlSource() {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select ").append(getColumnNameByField(idField)).append(" ").append(idField.getName());
		for(Field field : columnFields){
			sql.append(" , ").append(getColumnNameByField(field)).append(" ").append(field.getName());
		}
		
		sql.append(" from ").append(tableName);
		
		List<SqlNode> contents = new ArrayList<>();
		contents.add(new TextSqlNode(sql.toString()));
		
		
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
		return entityClass;
	}
}
