module Licensing
  class LicensingChecker

    LICENSING = "http://purl.org/dc/terms/license"
    HUMAN_READABLE_LICENSE = "http://vocab.org/waiver/terms/normsnorms"

    def initialize(urls, report)
      @urls = urls
      @report = report
    end

    def report
      puts "LICENSING CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "=========================================================="
      @urls.each do |url|
        print "Analyzing #{url}..."
        if url.include?(HUMAN_READABLE_LICENSE)
          @report[:licensing][:human_readable_license] = true
        elsif url.include?(LICENSING)
          @report[:licensing][:license] = true
        end
        puts "Done!"
      end
      @report
    end
  end
end
