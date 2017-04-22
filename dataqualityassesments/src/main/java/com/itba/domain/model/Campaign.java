package com.itba.domain.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

import lombok.Getter;

@Entity
@Table(name = "campaign")
public class Campaign extends PersistentEntity {

    @Column(name = "name")
    private @Getter String name;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "graphs")
    private String graphs;

    @OneToMany(mappedBy = "campaign")
    private @Getter Set<EvaluationSession> sessions;

    Campaign() {}
    
    public Campaign(String name, String endpoint) {
    	this.name = name;
    	this.endpoint = endpoint;
    }
    
    public String generateQueryURL(String sparqlQuery) {
        String retVal = "";
        retVal += endpoint;
        retVal += "?format=json&query=";
        try {
            retVal += URLEncoder.encode(sparqlQuery, "UTF-8").replace("#", "%23");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public String getQueryforResourceTriples(String resource) {
        String from = "";
        for (String g : graphs.split(";"))
            from += " FROM <" + g + "> ";
        return "select ?p ?o " +
                from +
                "where {<" +
                resource +
                "> ?p ?o} ORDER BY ?p";
    }

    public String getQueryforRandomResource() {

        int offset = new Random().nextInt(760129);
        String from = "";
        for (String g : graphs.split(";"))
            from += " FROM <" + g + "> ";
        return " SELECT ?s " + from +
                " WHERE { ?s foaf:isPrimaryTopicOf ?o } LIMIT 1 OFFSET " + offset;
    }

    public String getQueryforRandomClassResource(String classURI, long maxRand) {

        int offset = new Random().nextInt((int) maxRand);
        String from = "";
        for (String g : graphs.split(";"))
            from += " FROM <" + g + "> ";
        return " SELECT ?s " + from +
                " WHERE { ?s rdf:type <" + classURI + "> } LIMIT 1 OFFSET " + offset;
    }

    public String getQueryforClassCount(String classURI) {

        String from = "";
        for (String g : graphs.split(";"))
            from += " FROM <" + g + "> ";
        return " SELECT count(?s) " + from +
                " WHERE { ?s rdf:type <" + classURI + "> }";
    }

    public String getQueryforSearchResultPage(String namepart, int offset, int limit) {
        String from = "";
        for (String g : graphs.split(";"))
            from += " FROM <" + g + "> ";
        return " SELECT ?s " + from +
                " WHERE { ?s foaf:isPrimaryTopicOf ?o . " +
                " FILTER regex(str(?s), '" + namepart + "', 'i'). }" +
                " LIMIT " + limit +
                " OFFSET " + offset;
    }
}
