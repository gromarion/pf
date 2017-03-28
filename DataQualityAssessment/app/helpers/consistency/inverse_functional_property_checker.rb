module Consistency
  class InverseFunctionalPropertyChecker
    def initialize(rdf)
      @rdf = rdf
      @inverses = {}
      @report = { faulty_statements: [] }
      @last_detected = nil
    end

    def report
      puts "INVERSE FUNCTIONAL PROPERTY CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "============================================================================"
      @rdf.each do |triple|
        if triple.object.to_s.include?("InverseFunctionalProperty")
          @last_detected = triple.subject.to_s
          next
        end
        add_to_report(triple) 
      end
      binding.pry
      @report
    end

    def add_to_report(triple)
      object = triple.object.to_s
      if @inverses[object] && @inverses[object] != triple.subject.to_s
        @report[:faulty_statements] << object unless @report[:faulty_statements].include?(object)
      else
        @inverses[object] = triple.subject.to_s
      end
    end
  end
end
