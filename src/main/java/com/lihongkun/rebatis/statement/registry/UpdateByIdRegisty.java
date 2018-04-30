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
 * <p>根据ID更新</p>
 * <pre>
 * update [tableName] 
 * &lt;set&gt; 
 * 		&lt;if test=" someField1 != null"&gt; some_field1 = #{someField1} ,&lt;/if&gt; 
 * 		&lt;if test=" someField2 != null"&gt; some_field2 = #{someField2} ,&lt;/if&gt; 
 * &lt;/set&gt; 
 * where [id] = #{id}
 * </pre>
 * @author lihongkun
 */
public class UpdateByIdRegisty extends GenericMappedStatementRegistry {

	public UpdateByIdRegisty(RegistryArgs args) {
		super(args);
	}

	public String getStatementId() {
		return namespace + ".updateById";
	}

	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

	public SqlSource getSqlSource() {
		
		List<SqlNode> contents = new ArrayList<>();
		
		contents.add(new TextSqlNode(" update "+tableName));
		contents.add(getUpdateSetStatement());
		contents.add(new TextSqlNode(" where "+getColumnNameByField(idField)+" = #{"+idField.getName()+"}"));
		
		return new DynamicSqlSource(configuration, new MixedSqlNode(contents));
	}

	public KeyGenerator getKeyGenerator() {
		return new NoKeyGenerator();
	}

	public Class<?> getResultType() {
		return int.class;
	}
}
