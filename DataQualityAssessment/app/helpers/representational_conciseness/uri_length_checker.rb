module RepresentationalConciseness
  class UriLengthChecker
    MAX_URL_LENGTH = 2048

    def initialize(urls, report)
      @urls = urls
      @report = report
    end

    def report
      puts "URI LENGTH-QUERY PARAMS CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "========================================================================"
      @urls.each do |url|
        print "\nAnalyzing #{url}..."
        add_url_report(url, :query_params) if url.include?("?")
        add_url_report(url, :too_long) if url.length > MAX_URL_LENGTH
        puts "Done!"
      end
      @report
    end

    private

    def add_url_report(url, key)
      if @report[:url][url]
        @report[:url][url][key] = true
      else
        url_status = { url: url }
        url_status[key] = true
        @report[:url][url] = url_status
      end
    end
  end
end
