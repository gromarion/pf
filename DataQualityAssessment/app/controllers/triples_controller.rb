class TriplesController < ApplicationController
  def index
    @triples = sparql_client.query('SELECT ?s WHERE {?s ?p ?o} GROUP BY ?s', SPARQL::Client::ACCEPT_XML)
  end

  private

  def sparql_client
    @client ||= SPARQL::Client.new('http://localhost:8890/sparql')
  end
end
