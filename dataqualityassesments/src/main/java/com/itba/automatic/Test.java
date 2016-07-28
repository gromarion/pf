package com.itba.automatic;

public class Test {
	public static void main(String[] args) {
		Latency.check("http://live.dbpedia.org/sparql", 80, 10);
	}
}
