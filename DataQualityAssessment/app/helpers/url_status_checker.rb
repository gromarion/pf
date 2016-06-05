class UrlStatusChecker
  def initialize(urls)
    @urls = urls
    @report = {}
  end

  def report
    puts 'Checking url availability...'
    @urls.each do |url|
      print "\nAnalyzing #{url}..."
      code = fetch_code(url)
      add_code_report(code, url)
      puts 'Done!'
    end
    @report["200"]
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
    if @report[code]
      @report[code] << url
    else
      @report[code] = [url]
    end
  end
end
