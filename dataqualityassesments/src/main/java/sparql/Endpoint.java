package sparql;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Random;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Endpoint implements Serializable {
	private static final long serialVersionUID = 1L;
	private long endpointID = 0;
    private String endpoint;
    private List<String> graphs = new ArrayList<String>();
    private String name;

    public Endpoint() {
        endpoint = "";
        name="";
        graphs = Arrays.asList();
    }

    public Endpoint(long id, String endpoint, String graphs, String name) {
        this.endpointID = id;
        this.endpoint = endpoint;
        String[] g = graphs.split(";");
        for (int i = 0; i < g.length; i++)
            this.graphs.add(g[i]);
        this.name = name;
    }

    public long getID(){
        return endpointID;
    }

    public String getName() {
        return name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public List<String> getGraphs() {
        return graphs;
    }

    public String getDefaultGraph() {
        return graphs.get(0);
    }

    public String generateQueryURL(String sparqlQuery) {
        String retVal = "";
        retVal += endpoint;
        //retVal += "?default-graph-uri=" + URL.encode(graphs.get(0));
        retVal += "?format=json&query=";
        retVal += URL.encode(sparqlQuery).replace("#", "%23");
        return retVal;
    }

    public String getQueryforResourceTriples(String resource) {
        String from = "";
        for (String g : graphs)
            from += " FROM <" + g + "> ";
        return "select ?p ?o " +
                from +
                "where {<" +
                resource +
                "> ?p ?o} ORDER BY ?p";
    }

    public String getQueryforRandomResource() {

        int offset = Random.nextInt(760129);
        String from = "";
        for (String g : graphs)
            from += " FROM <" + g + "> ";
        return " SELECT ?s " + from +
                " WHERE { ?s foaf:isPrimaryTopicOf ?o } LIMIT 1 OFFSET " + offset;
    }

    public String getQueryforRandomClassResource(String classURI, long maxRand) {

        int offset = Random.nextInt((int) maxRand);
        String from = "";
        for (String g : graphs)
            from += " FROM <" + g + "> ";
        return " SELECT ?s " + from +
                " WHERE { ?s rdf:type <" + classURI + "> } LIMIT 1 OFFSET " + offset;
    }

    public String getQueryforClassCount(String classURI) {

        String from = "";
        for (String g : graphs)
            from += " FROM <" + g + "> ";
        return " SELECT count(?s) " + from +
                " WHERE { ?s rdf:type <" + classURI + "> }";
    }

    public String getQueryforAutocomplete(String namepart) {
        String from = "";
        for (String g : graphs)
            from += " FROM <" + g + "> ";
        return " SELECT ?s " + from +
                " WHERE { ?s foaf:isPrimaryTopicOf ?o . " +
                " FILTER regex(str(?s), '" + namepart + "', 'i'). } LIMIT 10";
    }
}
