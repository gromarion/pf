module Performance
  class ScalabilityChecker
    def initialize(urls, threads)
      @urls = urls
      @threads = threads
      @report = {}
    end

    def report
      @urls.each do |url|
        print "\nAnalyzing #{url}..."
        avg = avg_time(url)
        one_time = measure_request_time(url)
        puts "Done! #{url}: avg = #{avg}, one_time = #{one_time}"
      end
    end

    private

    def avg_time(url)
      threads = []
      total_time = 0
      @threads.times do
        threads << Thread.new { time = measure_request_time(url);total_time += time }
      end
      threads.each(&:join)
      total_time / @threads
    end

    def measure_request_time(url)
      uri = URI.parse(url)
      http = Net::HTTP.new(uri.host, uri.port)
      request = Net::HTTP::Get.new(uri.request_uri)
      start_time = Time.current
      http.request(request)

      Time.current - start_time
    end
  end
end
