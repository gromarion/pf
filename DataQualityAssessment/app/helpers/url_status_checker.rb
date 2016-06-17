class UrlStatusChecker

  OK = %w[200 201 202 203 204 205 206 207 208 226]
  REDIRECTION = %w[300 301 302 303 304 305 306 307 308]
  OK_CODES = OK + REDIRECTION

  SUCCESS = "success"
  FAILURE = "failure"

  def initialize(urls)
    @urls = urls
    @report = {}
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
    @report[SUCCESS]
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
    if OK_CODES.include?(code)
      add_value(SUCCESS, url)
    else
      add_value(FAILURE, url)
    end
  end

  def add_value(key, url)
    if @report[key]
      @report[key] << url
    else
      @report[key] = [url]
    end
  end
end
