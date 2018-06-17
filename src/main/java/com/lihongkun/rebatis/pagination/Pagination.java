package com.lihongkun.rebatis.pagination;

import java.io.Serializable;
import java.util.List;

public class Pagination<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	private boolean containTotal;
	
	private Long total;
	
	private List<T> items;

	public boolean isContainTotal() {
		return containTotal;
	}

	public void setContainTotal(boolean containTotal) {
		this.containTotal = containTotal;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
}
