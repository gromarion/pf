module Consistency
  class DisjointClassChecker
    def initialize(rdf)
      @rdf = rdf
      @report = { disjoint_classes: {}, subclasses: {}, anomalies: []}
    end

    def report
      puts "DISJOINT CLASS CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "==============================================================="
      @rdf.each do |triple|
        if triple.predicate.to_s.include?("disjointWith")
          @report[:disjoint_classes][triple.subject.to_s] = triple.object.to_s
          @report[:disjoint_classes][triple.object.to_s] = triple.subject.to_s
          if (
                @report[:subclasses][triple.subject.to_s] &&
                @report[:subclasses][triple.subject.to_s][triple.object.to_s]
             )
            @report[:anomalies] += [triple.subject.to_s, triple.object.to_s]
          end
        elsif triple.predicate.to_s.include?("subClassOf")
          @report[:subclasses][triple.subject.to_s] = triple.object.to_s
          if (
                @report[:disjoint_classes][triple.subject.to_s] &&
                @report[:disjoint_classes][triple.subject.to_s][triple.object.to_s]
              ) || (
                @report[:disjoint_classes][triple.object.to_s] &&
                @report[:disjoint_classes][triple.object.to_s][triple.subject.to_s]
            )
            @report[:anomalies] += [triple.subject.to_s, triple.object.to_s]
          end
        end
      end
      @report
    end
  end
end
