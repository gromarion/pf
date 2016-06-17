class UrlStatusChecker

  OK = %w[200 201 202 203 204 205 206 207 208 226]
  REDIRECTION = %w[300 301 302 303 304 305 306 307 308]
  OK_CODES = OK + REDIRECTION

  def initialize(urls, report)
    @urls = urls
    @report = report
  end

  def report
    puts 'Checking url availability...'
    if @urls.nil?
      puts 'No URLs to analyze'
      return
    end
    @urls.each do |url|
      print "\nAnalyzing #{url}..."
      code = fetch_code(url)
      add_code_report(code, url)
      puts 'Done!'
    end    
    @report
  end

  private

  def fetch_code(url)
    uri = URI.parse(url)
    http = Net::HTTP.new(uri.host, uri.port)
    request = Net::HTTP::Get.new(uri.request_uri)
    res = http.request(request)

    res.code
  end

  def add_code_report(code, url)
    if @report[url]
      @report[url][:http_code] = code
    else
      url_status = { url: url, http_code: code }
      @report[url] = url_status
    end
  end
end
