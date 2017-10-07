package com.lihongkun.rebatis.statement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.lihongkun.rebatis.annotation.Id;
import com.lihongkun.rebatis.annotation.Table;
import com.lihongkun.rebatis.annotation.Transient;
import com.lihongkun.rebatis.statement.registry.CountRegistry;
import com.lihongkun.rebatis.statement.registry.DeleteAllByIdRegistry;
import com.lihongkun.rebatis.statement.registry.DeleteByIdRegistry;
import com.lihongkun.rebatis.statement.registry.DeleteRegistry;
import com.lihongkun.rebatis.statement.registry.ExistsByIdRegistry;
import com.lihongkun.rebatis.statement.registry.FindAllByIdRegistry;
import com.lihongkun.rebatis.statement.registry.FindAllRegistry;
import com.lihongkun.rebatis.statement.registry.FindByIdRegistry;
import com.lihongkun.rebatis.statement.registry.InsertAllRegistry;
import com.lihongkun.rebatis.statement.registry.InsertRegistry;
import com.lihongkun.rebatis.statement.registry.UpdateByIdRegisty;
import com.lihongkun.rebatis.util.StringUtils;

/**
 * SQL注册器聚合
 * @author lihongkun
 */
public class MappedStatementRegistryComposite implements MappedStatementRegistry {
	
	private Configuration configuration;
	
	private MapperBuilderAssistant assistant;
	
	private String namespace;
	
	private String tableName;
	
	private Class<?> entityClass;
	
	private Field idField;
	
	private List<Field> columnFields = new ArrayList<Field>();
	
	private List<MappedStatementRegistry> mappedStatementRegistrys;
	
	public MappedStatementRegistryComposite(Configuration configuration,String namespace,Class<?> entityClass){
		this.configuration = configuration;
		this.entityClass = entityClass;
		this.namespace = namespace;
		
		Table table = entityClass.getAnnotation(Table.class);
		
		String resource = namespace.replace(".", "/")+".java";
		
		assistant = new MapperBuilderAssistant(configuration, resource);
		assistant.setCurrentNamespace(namespace);
		
		if(org.apache.commons.lang3.StringUtils.isEmpty(table.value())){
			this.tableName = StringUtils.camelToUnderScore(entityClass.getSimpleName());
		}else {
			this.tableName = table.value();
		}
		
		this.idField = com.lihongkun.rebatis.util.ReflectionUtils.findFieldWithAnnoation(entityClass, Id.class);
		
		ReflectionUtils.doWithFields(entityClass, new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				columnFields.add(field);
			}
		},new FieldFilter() {
			public boolean matches(Field field) {
				
				if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
					return false;
				}
				
				for(Annotation annotation : field.getAnnotations()){
					if(Transient.class.isAssignableFrom(annotation.getClass()) 
							|| Id.class.isAssignableFrom(annotation.getClass())){
						return false;
					}
				}
				
				return true;
			}
		});
		
		mappedStatementRegistrys = getDefaultMappedStatementRegistrys();
	}
	
	private List<MappedStatementRegistry> getDefaultMappedStatementRegistrys(){
		
		RegistryArgs registryArgs = buildRegistryArgs();
		
		List<MappedStatementRegistry> mappedStatementRegistrys = new ArrayList<>();
		
		mappedStatementRegistrys.add(new CountRegistry(registryArgs));
		mappedStatementRegistrys.add(new DeleteAllByIdRegistry(registryArgs));
		mappedStatementRegistrys.add(new DeleteByIdRegistry(registryArgs));
		mappedStatementRegistrys.add(new DeleteRegistry(registryArgs));
		mappedStatementRegistrys.add(new ExistsByIdRegistry(registryArgs));
		mappedStatementRegistrys.add(new FindAllByIdRegistry(registryArgs));
		mappedStatementRegistrys.add(new FindAllRegistry(registryArgs));
		mappedStatementRegistrys.add(new FindByIdRegistry(registryArgs));
		mappedStatementRegistrys.add(new InsertAllRegistry(registryArgs));
		mappedStatementRegistrys.add(new InsertRegistry(registryArgs));
		mappedStatementRegistrys.add(new UpdateByIdRegisty(registryArgs));
		
		return mappedStatementRegistrys;
	}
	
	private RegistryArgs buildRegistryArgs(){
		RegistryArgs args = new RegistryArgs();
		
		args.setAssistant(assistant);
		args.setColumnFields(columnFields);
		args.setConfiguration(configuration);
		args.setEntityClass(entityClass);
		args.setIdField(idField);
		args.setNamespace(namespace);
		args.setTableName(tableName);
		
		return args;
	}

	public void registerMappedStatement() {
		for(MappedStatementRegistry resistry:mappedStatementRegistrys){
			resistry.registerMappedStatement();
		}
	}
	
}
