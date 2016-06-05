class SparqlStatusChecker
  SUCCESS = 'success'
  FAILURE = 'failure'

  def initialize(urls)
    @urls = urls
    @report = {}
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
    @report[SUCCESS]
  end

  private

  def add_code_report(code, url)
    if @report[code]
      @report[code] << url
    else
      @report[code] = [url]
    end
  end
end
