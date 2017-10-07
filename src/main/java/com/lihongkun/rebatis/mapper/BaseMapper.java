package com.lihongkun.rebatis.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

/**
 * 通用Mapper接口,定义额基础的数据库操作.
 * 
 * @author lihongkun
 */
public interface BaseMapper<T,ID>  {
	
	/**
	 * 新增
	 * @param entity	新增的实体类
	 * @return			成功数量
	 */
	<S extends T> int insert(S entity);

	/**
	 * 新增全部
	 * @param entities	新增的实体类列表
	 * @return			成功数量
	 */
	<S extends T> int insertAll(List<S> entities);

	/**
	 * 根据主键判断是否存在
	 * @param id		主键值
	 * @return			true - 存在,false - 不存在
	 */
	boolean existsById(ID id);
	
	/**
	 * 根据ID查询
	 * @param id		主键值
	 * @return			查询结果实体类
	 */
	T findById(ID id);
	
	/**
	 * 查询
	 * @param entity	查询条件		
	 * @return			查询结果实体类列表
	 */
	List<T> findAll(T entity,RowBounds rowBounds);

	/**
	 * 根据id列表查询
	 * 
	 * @param ids		主键值列表
	 * @return			查询结果实体类列表
	 */
	List<T> findAllById(List<ID> ids);

	/**
	 * 计数
	 * @param entity	实体类条件
	 * @return 			the number of entities
	 */
	long count(T entity);

	/**
	 * 删除指定ID
	 * @param id 		删除的对象主键值
	 * @return 			删除的数量
	 */
	int deleteById(ID id);

	/**
	 * 根据条件删除
	 * @param entity	删除的条件,属性不为空则加入条件中
	 * @return 			删除的数量
	 */
	int delete(T entity);

	/**
	 * 根据ID列表删除
	 * @param ids		待删除对象的主键值列表
	 * @return 			删除的数量
	 */
	int deleteAllById(List<ID> ids);
	
	/**
	 * 根据ID更新
	 * @param entity	实体类,其中ID为更新条件,其他字段为更新值
	 * @return 			更新的数量
	 */
	int updateById(T entity);
	
}
