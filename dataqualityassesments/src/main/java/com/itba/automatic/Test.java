package com.itba.automatic;

public class Test {
	public static void main(String[] args) {
		System.out.println(SparqlSatatusChecker.check("http://live.dbpedia.org/sparql", "http://dbpedia.org", "live.dbpedia.org/sparql"));
		Latency.check("http://live.dbpedia.org/sparql", 80, 10);
	}
}
