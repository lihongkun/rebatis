package com.lihongkun.rebatis.pagination;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.lihongkun.rebatis.proxy.RebatisMapperMethod.SqlCommand;

/**
 * 分页帮助类
 * @author lihongkun
 */
public class PageHelper {

	/**
	 * 提取分页参数
	 * @param args	全部参数
	 * @return		分页参数
	 */
	public static Paginator getPaginator(Object[] args){
		Paginator paginator = null;
		
		//find first Paginator instance for pagination
		for(Object arg:args){
			if(arg instanceof Paginator){
				paginator = (Paginator)arg;
				break;
			}
		}
		
		if(paginator == null){
			throw new BindingException("paginator no found on args");
		}
		
		return paginator;
	}
	
	/**
	 * 查询数据总数
	 * @param sqlSession
	 * @param command
	 * @param param
	 * @return
	 */
	public static Long getTotalCnt(SqlSession sqlSession,SqlCommand command,Object param){
		Long total = 0L;
		Configuration configuration = sqlSession.getConfiguration();
		try {
			PreparedStatement countStmt = getCountStatement(configuration, command, param);
			
			ResultSet rs = countStmt.executeQuery();
			if(rs.next())
				total = rs.getLong(1);
			
		} catch (SQLException e) {
			throw new BindingException(e);
		}
		return total;
	}
	
	private static PreparedStatement getCountStatement(Configuration configuration,SqlCommand command,Object param) throws SQLException{
		MappedStatement mappedStatement = configuration.getMappedStatement(command.getName(), false);
		BoundSql boundSql = mappedStatement.getBoundSql(param);
		
		String countSql = "select count(1) from ("+boundSql.getSql()+") count_tmp";
		Connection connection = configuration.getEnvironment().getDataSource().getConnection();
		PreparedStatement countStmt = connection.prepareStatement(countSql);
		
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings == null) {
            return countStmt;
        }
        
        MetaObject metaObject = param == null ? null : configuration.newMetaObject(param);
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            if (parameterMapping.getMode() == ParameterMode.OUT) {
                continue;
            }
            
            String propertyName = parameterMapping.getProperty();
            
            Object value = getPropertyValue(param, boundSql, metaObject, typeHandlerRegistry, propertyName);
            TypeHandler typeHandler = getTypeHandler(mappedStatement, parameterMapping, propertyName);
            JdbcType jdbcType = getJdbcType(configuration, parameterMapping, value);
            
            typeHandler.setParameter(countStmt, i + 1, value, jdbcType);
        }
        
        return countStmt;
	}

	private static JdbcType getJdbcType(Configuration configuration, ParameterMapping parameterMapping, Object value) {
		JdbcType jdbcType = parameterMapping.getJdbcType();
		if (value == null && jdbcType == null) 
			jdbcType = configuration.getJdbcTypeForNull();
		return jdbcType;
	}

	private static TypeHandler getTypeHandler(MappedStatement mappedStatement, ParameterMapping parameterMapping, String propertyName) {
		TypeHandler typeHandler = parameterMapping.getTypeHandler();
		if (typeHandler == null) {
		    throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
		}
		return typeHandler;
	}

	private static Object getPropertyValue(Object param, BoundSql boundSql, MetaObject metaObject,TypeHandlerRegistry typeHandlerRegistry, String propertyName) {
		if (boundSql.hasAdditionalParameter(propertyName)) {
		    return boundSql.getAdditionalParameter(propertyName);
		} 
		else if (param == null || typeHandlerRegistry.hasTypeHandler(param.getClass())) {
		    return param;
		} 
		else {
		    return metaObject == null ? null : metaObject.getValue(propertyName);
		}
	}
	
}
