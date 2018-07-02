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
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.lihongkun.rebatis.proxy.RebatisMapperMethod.SqlCommand;
import com.lihongkun.rebatis.util.StringUtil;

/**
 * 分页帮助类
 * @author lihongkun
 */
public class PageHelper {

	private static String MYSQL_PAGE_SQL = "{} limit {},{}";
	
	private static String ORACLE_PAGE_SQL = "select * from ( select row_.*, rownum rownum_ from ({}) row_ ) where rownum_ <= {} and rownum_ > {} ) where rownum <= {}";
	
	private static String POSTGRE_PAGE_SQL = "{} limit {} offset {}";
	
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
	 * @param sqlSession		Mybatis SqlSession
	 * @param command			Mybatis SqlCommand {@link SqlCommand}
	 * @param param				Mybatis MappedStatement parameterObject
	 * @return					对应SQL查询出的数据总数
	 */
	public static Long getTotalCnt(SqlSession sqlSession,SqlCommand command,Object param){
		Long total = 0L;
		Configuration configuration = sqlSession.getConfiguration();
		Connection connection = null;
		PreparedStatement countStmt = null;
		try {
			connection = configuration.getEnvironment().getDataSource().getConnection();
			countStmt = getCountStatement(configuration,connection, command, param);
			
			ResultSet rs = countStmt.executeQuery();
			if(rs.next())
				total = rs.getLong(1);
			
		}
		catch (SQLException e) {
			throw new BindingException(e);
		}
		finally {
			closeQuietly(countStmt);
			closeQuietly(connection);
		}
		
		return total;
	}
	
	private static PreparedStatement getCountStatement(Configuration configuration,Connection connection,SqlCommand command,Object param) throws SQLException{
		MappedStatement mappedStatement = configuration.getMappedStatement(command.getName(), false);
		BoundSql boundSql = mappedStatement.getBoundSql(param);
		
		String countSql = "select count(1) from ("+boundSql.getSql()+") count_tmp";
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
	
	public static String getPageSql(String sql,RowBounds rowBounds,SqlMode sqlMode){
		
		if(sqlMode == SqlMode.DEFAULT || sqlMode == SqlMode.MYSQL){
			return StringUtil.replace(MYSQL_PAGE_SQL, "{}", new Object[]{sql,rowBounds.getOffset(),rowBounds.getLimit()});
		}
		else if(sqlMode == SqlMode.ORACLE){
			return StringUtil.replace(ORACLE_PAGE_SQL, "{}", new Object[]{sql,rowBounds.getLimit(),rowBounds.getOffset()});
		}
		else if(sqlMode == SqlMode.POSTGRE){
			return StringUtil.replace(POSTGRE_PAGE_SQL, "{}", new Object[]{sql,rowBounds.getLimit()+rowBounds.getOffset(),rowBounds.getOffset(),rowBounds.getLimit()});
		}
		else {
			throw new BindingException("only mysql,oracle,postgre be supported !");
		}
	}
	
	private static final void closeQuietly(AutoCloseable closeable){
		try{
			if(closeable != null){
				closeable.close();
			}
		}
		catch (Exception e) {
		}
	}
}
