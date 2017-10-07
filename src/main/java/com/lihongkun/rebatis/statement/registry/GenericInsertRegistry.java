package com.lihongkun.rebatis.statement.registry;

import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.SqlCommandType;

import com.lihongkun.rebatis.annotation.Id;
import com.lihongkun.rebatis.keygen.KeyGeneratorType;
import com.lihongkun.rebatis.statement.GenericMappedStatementRegistry;
import com.lihongkun.rebatis.statement.RegistryArgs;

public abstract class GenericInsertRegistry extends GenericMappedStatementRegistry {

	public GenericInsertRegistry(RegistryArgs args) {
		super(args);
	}
	
	public KeyGenerator getKeyGenerator() {
		//默认使用SelectKey
		String keyStatementId = getStatementId() + SelectKeyGenerator.SELECT_KEY_SUFFIX;
		if (configuration.hasKeyGenerator(keyStatementId)) {
			return configuration.getKeyGenerator(keyStatementId);
		}
		
		//不存在SelectKey 则使用ID注解规则
		Id idAnnotation = idField.getAnnotation(Id.class);
		if(idAnnotation.value() == KeyGeneratorType.AUTO){
			return new Jdbc3KeyGenerator();
		}
		else{
			return new NoKeyGenerator();
		}
	}

	public Class<?> getResultType() {
		return int.class;
	}
	
	public SqlCommandType getSqlCommandType() {
		return SqlCommandType.INSERT;
	}

}
