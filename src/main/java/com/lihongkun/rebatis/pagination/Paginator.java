package com.lihongkun.rebatis.pagination;

import java.io.Serializable;

public class Paginator implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int offset;
	
	private int limit;
	
	private boolean containTotal;
	
	public Paginator(){
	}
	
	public Paginator(int offset,int limit){
		this(offset, limit, false);
	}
	
	public Paginator(int offset,int limit,boolean containTotal){
		this.offset = offset;
		this.limit = limit;
		this.containTotal = containTotal;
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isContainTotal() {
		return containTotal;
	}

	public void setContainTotal(boolean containTotal) {
		this.containTotal = containTotal;
	}
}
