class SparqlStatusChecker
  SUCCESS = 'success'
  FAILURE = 'failure'

  def initialize(urls, report)
    @urls = urls
    @report = report
  end

  def report
    puts 'Checking sparql availability...'
    @urls.each do |url|
      print "\nAnalyzing #{url}..."
      begin
        client = SPARQL::Client.new(url)
        ans = client.query('SELECT * WHERE {?s ?p ?o}')
        ans.present? ? add_code_report(SUCCESS, url) : add_code_report(FAILURE, url)
      rescue SPARQL::Client::ClientError
        add_code_report(FAILURE, url)
      end
      puts 'Done!'
    end
    @report
  end

  private

  def add_code_report(code, url)
    if @report[:url][url]
      @report[:url][url][:sparql] = code
    else
      url_status = { url: url, sparql: code }
      @report[:url][url] = url_status
    end
  end
end
