class Yah
  def initialize(ontology_path, criteria)
    @rdf = RDF::RDFXML::Reader.open(ontology_path)
    @criteria = criteria
    @urls = []
    @report = {}
    @availability_report, @availability_report, @schema_completeness_checker = nil
  end

  def report
    @report.merge!(Versatility::LanguagesChecker.new(@rdf, @versatility_report).report)
    @report.merge!(Completeness::SchemaCompletenessChecker.new(@rdf).report)
    @rdf.each do |triple|
      add_url(triple.subject) if triple.subject.is_a?(RDF::URI)
      add_url(triple.object) if triple.object.is_a?(RDF::URI)
    end
    @report.merge!(AvailabilityPipeline.new(@urls, @criteria).perform)
  end

  private

  def add_url(statement)
    url = statement.value
    @urls << url unless @urls.include?(url)
  end
end
