class AvailabilityPipeline
  def initialize(urls, criteria)
    @urls = urls
    @criteria = criteria
  end

  def perform
    remaining_urls = @urls
    @criteria.each { |criteria| remaining_urls = criteria.constantize.new(remaining_urls).report }
    remaining_urls
  end
end
