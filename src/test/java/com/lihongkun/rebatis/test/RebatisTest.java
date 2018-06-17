package com.lihongkun.rebatis.test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.lihongkun.rebatis.pagination.Pagination;
import com.lihongkun.rebatis.pagination.Paginator;
import com.lihongkun.rebatis.test.domain.Category;
import com.lihongkun.rebatis.test.mapper.CategoryMapper;

public class RebatisTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(RebatisTest.class);
	
	private static ApplicationContext context;
	
	private static CategoryMapper categoryMapper;
	
	@BeforeClass
	public static void setUp() throws Exception {
		context = new AnnotationConfigApplicationContext(RebatisConfs.class);
		categoryMapper = context.getBean(CategoryMapper.class);
	}
	
	@Before
	public void emptyLine(){
		System.out.println("\n");
	}
	
	@Test
	public void testInsert(){
		Category cat = insert();
		LOGGER.info("testInsert , succ {}",cat);
	}

	@Test
	public void  testExistsById(){
		boolean b = categoryMapper.existsById(3L);
		LOGGER.info("testExistsById , result {}",b);
	}
	
	@Test
	public void  testFindById(){
		Category cat =categoryMapper.findById(3L);
		LOGGER.info("testFindById : {}",cat);
	}
	
	@Test
	public void testFindAll(){
		Category category = new Category();
		category.setType(1);
		
		Pagination<Category> catList = categoryMapper.findAll(category, new Paginator(0, 2,true));
		LOGGER.info("testFindAll");
		for(Category cat : catList.getItems()){
			LOGGER.info("{}",cat);
		}
	}

	@Test
	public void testFindAllById(){
		List<Category> catList = categoryMapper.findAllById(Arrays.asList(1L,2L,3L,4L));
		LOGGER.info("testFindAllById");
		for(Category cat : catList){
			LOGGER.info("{}",cat);
		}
	}

	@Test
	public void testCount(){
		long cnt = categoryMapper.count(new Category());
		LOGGER.info("testCount , cnt {}",cnt);
	}

	@Test
	public void  testDeleteById(){
		Category cat = insert();
		int i = categoryMapper.deleteById(cat.getId());
		LOGGER.info("testDeleteById , succ {}",i);
	}

	@Test
	public void  testDelete(){
		Category cat = insert();
		int i = categoryMapper.delete(cat);
		LOGGER.info("testDelete , succ {}",i);
	}

	@Test
	public void  testDeleteAllById(){
		Category cat = insert();
		int i = categoryMapper.deleteAllById(Collections.singletonList(cat.getId()));
		LOGGER.info("testDeleteAllById , succ {}",i);
	}
	
	@Test
	public void  testUpdateById(){
		Category cat = categoryMapper.findById(1L);
		
		LOGGER.info("testUpdateById before : {}",cat);
		
		cat.setCode("design ideas");
		categoryMapper.updateById(cat);
		
		cat = categoryMapper.findById(1L);
		
		LOGGER.info("testUpdateById after : {}",cat);
		
	}

	private Category insert(){
		long unixtime = Calendar.getInstance().getTime().getTime();
		
		Category cat = new Category();
		cat.setCode("framework");
		cat.setName("framework");
		cat.setCover(StringUtils.EMPTY);
		cat.setType(NumberUtils.INTEGER_ONE);
		cat.setCreatedBy(NumberUtils.LONG_ONE);
		cat.setUpdatedBy(NumberUtils.LONG_ONE);
		cat.setCreatedOn(unixtime);
		cat.setUpdatedOn(unixtime);
		
		categoryMapper.insert(cat);
		
		return cat;
	}
}
