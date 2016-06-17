module Performance
  class LowLatencyChecker
    def initialize(urls, report, threads = 10)
      @urls = urls
      @threads = threads
      @report = report
    end

    def report
      puts "LOW LATENCY CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "============================================================"
      @urls.each do |url|
        print "\nAnalyzing #{url}..."
        add_latency_report(url, minimum_latency(url))
        puts "Done!"
      end
      @report
    end

    private

    def minimum_latency(url)
      threads = []
      times = []
      @threads.times { threads << Thread.new { time = measure_request_time(url); times << time } }
      threads.each(&:join)
      times.min
    end

    def measure_request_time(url)
      uri = URI.parse(url)
      http = Net::HTTP.new(uri.host, uri.port)
      request = Net::HTTP::Get.new(uri.request_uri)
      start_time = Time.current
      http.request(request)

      Time.current - start_time
    end

    def add_latency_report(url, time)
      if @report[:url][url]
        @report[:url][url][:minimum_latency] = time
      else
        @report[:url][url] = { url: url, minimum_latency: time }
      end
    end
  end
end
