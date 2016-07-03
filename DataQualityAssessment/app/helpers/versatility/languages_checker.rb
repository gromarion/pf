module Versatility
  class LanguagesChecker
    def initialize(rdf, report)
      @rdf = rdf
      @resources = {}
    end

    def report
      puts "LANGUAGES CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "=========================================================="
      @rdf.each do |triple|
        obj = triple.object
        add_language(triple) if obj.is_a?(RDF::Literal) && obj.value.is_a?(String)
      end
      { versatility: @resources }
    end

    private

    def add_language(triple)
      return if triple.object.language.nil?
      if @resources[triple.subject]
        if @resources[triple.subject][triple.predicate]
          unless @resources[triple.subject][triple.predicate].include?(triple.object.language)
            @resources[triple.subject][triple.predicate] << triple.object.language
          end
        else
          @resources[triple.subject][triple.predicate] = [triple.object.language]
        end
      else
        @resources[triple.subject] = { triple.predicate => [triple.object.language] }
      end
    end
  end
end
