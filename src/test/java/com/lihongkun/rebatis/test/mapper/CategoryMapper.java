package com.lihongkun.rebatis.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lihongkun.rebatis.mapper.BaseMapper;
import com.lihongkun.rebatis.pagination.Pagination;
import com.lihongkun.rebatis.pagination.Paginator;
import com.lihongkun.rebatis.test.domain.Category;

public interface CategoryMapper extends BaseMapper<Category, Long>{

	@Select("select id,code,name,type,cover,created_on createdOn,created_by createdBy,updated_on updatedOn,updated_by updatedBy  from t_category where type = #{type}")
	public List<Category> findByType(@Param("type") Integer type);
	
	@Select("select id,code,name,type,cover,created_on createdOn,created_by createdBy,updated_on updatedOn,updated_by updatedBy  from t_category where type = #{type}")
	public Pagination<Category> findByTypePage(@Param("type") Integer type,Paginator paginator);
	
	@Select("select id,code,name,type,cover,created_on createdOn,created_by createdBy,updated_on updatedOn,updated_by updatedBy  from t_category where type = #{type}")
	public Pagination<Category> findAll2(Category category,Paginator paginator);
}
