class AvailabilityPipeline
  def initialize(urls, criteria)
    @urls = urls
    @criteria = criteria
  end

  def perform
    report = {}
    @criteria.each { |criteria| report = criteria.constantize.new(@urls, report).report }
    report
  end
end
