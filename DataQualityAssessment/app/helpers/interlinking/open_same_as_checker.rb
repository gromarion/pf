module Interlinking
  class OpenSameAsChecker

    SAME_AS = "sameAs"

    def initialize(urls, report)
      @urls = urls
      @report = report
    end

    def report
      puts "OPEN SAME AS CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "============================================================="
      @urls.each do |url|
        print "Analyzing #{url}..."
        if url.include?(SAME_AS)
          add_same_as_report(url, "success")
        else
          add_same_as_report(url, "failure")
        end
        puts "Done!"
      end
      @report
    end

    private

    def add_same_as_report(url, result)
      if @report[:url][url]
        @report[:url][url][:same_as] = result
      else
        @report[:url][url] = { url: url, same_as: result }
      end
    end
  end
end
