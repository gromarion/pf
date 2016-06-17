module Performance
  class ScalabilityChecker
    def initialize(urls, report, threads = 10)
      @urls = urls
      @threads = threads
      @report = report
    end

    def report
      @urls.each do |url|
        print "\nAnalyzing #{url}..."
        add_time_report(url, avg_time(url), :avg_time)
        add_time_report(url, measure_request_time(url), :one_time)
        puts "Done!"
      end
      @report
    end

    private

    def avg_time(url)
      threads = []
      total_time = 0
      @threads.times do
        threads << Thread.new { time = measure_request_time(url); total_time += time }
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

    def add_time_report(url, time, key)
      if @report[url]
        @report[url][key] = time
      else
        url_status = { url: url }
        url_status[key] = time
        @report[url] = url_status
      end
    end
  end
end
