require 'rest-client'

module Availability
  class UrlStatusChecker
    OK = %w[200 201 202 203 204 205 206 207 208 226]
    REDIRECTION = %w[300 301 302 303 304 305 306 307 308]
    OK_CODES = OK + REDIRECTION

    def initialize(urls, report)
      @urls = urls
      @report = report
    end

    def report
      puts "URL STATUS CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "==========================================================="
      if @urls.nil?
        puts 'No URLs to analyze'
        return
      end
      @urls.each do |url|
        print "Analyzing #{url}..."
        code = fetch_code(url)
        add_code_report(code, url)
        puts 'Done!'
      end    
      @report
    end

    private

    def fetch_code(url)
      response = RestClient.get(url)

      response.code
    rescue RestClient::ResourceNotFound
      404
    end

    def add_code_report(code, url)
      if @report[:url][url]
        @report[:url][url][:http_code] = code
      else
        url_status = { url: url, http_code: code }
        @report[:url][url] = url_status
      end
    end
  end
end
