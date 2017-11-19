package com.itba.domain.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

@Entity
@Table(name = "campaign")
public class Campaign extends PersistentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "endpoint")
	private String endpoint;

	@Column(name = "graphs")
	private String graphs;
	
	@Column(name = "params")
	private String params;

	@OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
	private Set<EvaluationSession> sessions;

	Campaign() {
	}

	public Campaign(String name, String endpoint, String graph, String params) {
		this.name = name;
		this.endpoint = endpoint;
		this.graphs = graph;
		this.params = params;
	}

	public boolean hasEvaluations() {
		for (EvaluationSession session: sessions) {
			if (!session.getEvaluatedResources().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public Set<EvaluationSession> getSessions() {
		return sessions;
	}
	
	public String getName() {
		return name;
	}

	public String getEndpoint() {
		return endpoint;
	}
	
	public String getGraphs() {
		return graphs;
	}
	
	public String getParams() {
		return params;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setGraphs(String graphs) {
		this.graphs = graphs;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String generateQueryURL(String sparqlQuery) {
		String retVal = "";
		retVal += endpoint;
		retVal += "?format=json&query=";
		try {
			retVal += URLEncoder.encode(sparqlQuery, "UTF-8").replace("#", "%23") + params;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	public String getQueryforResourceTriples(String resource) {
		return "select ?p ?o " + "where {<" + resource + "> ?p ?o} ORDER BY ?p";
	}

	public String getQueryForEndpointSize() {
		return " SELECT (count(?s) as ?c) FROM <"+graphs+"> WHERE { ?s ?p ?o }";
	}
	
	public String getQueryforRandomResource(int endpointSize) {
		int offset = new Random().nextInt(endpointSize);
		return " SELECT ?s ?p ?o " + " FROM <"+graphs+"> WHERE { ?s ?p ?o } LIMIT 1 OFFSET " + offset;
	}

	public String getQueryforRandomClassResource(String classURI, long maxRand) {
		int offset = new Random().nextInt((int) maxRand);
		return " SELECT ?s " + " WHERE { ?s rdf:type <" + classURI + "> } LIMIT 1 OFFSET " + offset;
	}

	public String getQueryforClassCount(String classURI) {
		return " SELECT count(?s) " + " WHERE { ?s rdf:type <" + classURI + "> }";
	}

	public String getQueryforSearchResultPage(String namepart, int offset, int limit) {
		return " SELECT ?s ?p ?o FROM <" + graphs + "> WHERE { ?s ?p ?o . " + " FILTER regex(str(?s), '" + namepart + "') }"
				+ " LIMIT " + limit + " OFFSET " + offset;
	}

	public String getQueryForLicenseChecking() {
		return "PREFIX cc: <http://creativecommons.org/ns#> PREFIX dc: <http://purl.org/dc/elements/1.1/> "
				+ "ASK {{ ?s cc:license ?licence } UNION { ?s dc:rights ?licence }}";
	}
}
