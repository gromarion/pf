package com.itba.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

@Entity
@Table(name = "endpoint_stats")
public class EndpointStats extends PersistentEntity {
	@Column(name = "endpoint_url")
	private String endpointUrl;
	
	@Column(name = "statuscode")
	private String statusCode;
	
	@Column(name = "timestamp")
	private long timestamp;
	
	EndpointStats() {}
	
	public EndpointStats(String endpointUrl, String statusCode, long timestamp) {
    	this.endpointUrl = endpointUrl;
    	this.statusCode = statusCode;
    	this.timestamp = timestamp;
    }
	
	public String getEndpointUrl() {
		return endpointUrl;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}
	
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
