class UrlStatusChecker
  def initialize(urls)
    @urls = urls
    @report = {}
  end

  def report
    @urls.each do |url|
      print "\nAnalyzing #{url}..."
      code = fetch_code(url)
      add_code_report(code)
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

  def add_code_report(code)
    if @report[code]
      @report[code] += 1
    else
      @report[code] = 1
    end
  end
end
