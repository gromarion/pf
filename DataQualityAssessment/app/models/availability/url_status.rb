module Availability
  class UrlStatus
    attr_accessor :url, :http_code, :sparql_query
    def initialize(url)
      @url = url
    end
  end
end
