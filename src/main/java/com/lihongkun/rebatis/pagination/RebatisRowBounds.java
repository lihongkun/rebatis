package com.lihongkun.rebatis.pagination;

import org.apache.ibatis.session.RowBounds;

public class RebatisRowBounds extends RowBounds{

	private int offset;
	
	private int limit;
	
	public RebatisRowBounds(int offset,int limit){
		this.offset = offset;
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}
	
	public int getLimit() {
		return limit;
	}
}
