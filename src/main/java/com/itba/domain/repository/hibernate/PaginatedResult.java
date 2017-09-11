package com.itba.domain.repository.hibernate;

import java.util.List;

public class PaginatedResult<T> {

	private List<T> result;
	private int page;
	private long totalResultsAmount;
	private int limit;
	
	public PaginatedResult(List<T> result, int page, long totalresultsAmount, int limit) {
		this.result = result;
		this.page = page;
		this.totalResultsAmount = totalresultsAmount;
		this.limit = limit;
	}
	
	public boolean hasNextPage() {
		return page * limit + result.size() < totalResultsAmount;
	}
	
	public List<T> getResult() {
		return result;
	}
	
	public void setResult(List<T> result) {
		this.result = result;
	}
}
