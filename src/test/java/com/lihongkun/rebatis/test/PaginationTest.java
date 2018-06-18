package com.lihongkun.rebatis.test;

import java.util.List;

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

public class PaginationTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaginationTest.class);
	
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
	public void findByType(){
		List<Category> catList = categoryMapper.findByType(1);
		for(Category cat : catList){
			LOGGER.info("{}",cat);
		}
		
		Pagination<Category> pageList = categoryMapper.findByTypePage(1,new Paginator(0, 2));
		for(Category cat : pageList.getItems()){
			LOGGER.info("{}",cat);
		}
		
		Category category = new Category();
		category.setType(1);
		pageList = categoryMapper.findAll2(category,new Paginator(0, 2));
		for(Category cat : pageList.getItems()){
			LOGGER.info("{}",cat);
		}
		
	}

	
}
