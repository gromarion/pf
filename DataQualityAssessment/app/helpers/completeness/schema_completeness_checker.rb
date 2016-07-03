module Completeness
  class SchemaCompletenessChecker
    def initialize(rdf)
      @rdf = rdf
      @classes = {}
      @used_class = nil
      @instances = []
      @report = { total_classes: 0, represented_classes: 0 }
    end

    def report
      puts "SCHEMA COMPLETENESS CHECKER REPORT RUNNING. THIS MAY TAKE A WHILE..."
      puts "===================================================================="
      @rdf.each do |triple|
        add_to_classes(triple) if triple.object.to_s.include? "Class"
        add_to_classes(triple) if triple.predicate.to_s.include? "subClassOf"
        add_to_instances(triple) if triple.predicate.to_s.include?("type")
      end
      analyze_completeness
      @report
    end

    private

    def add_to_classes(triple)
      return if triple.subject.is_a?(RDF::Node) || triple.object.is_a?(RDF::Node)
      if @classes[triple.subject.to_s]
        @classes[triple.subject.to_s][:subclass_of] = triple.object.to_s
      else
        @classes[triple.subject.to_s] = { represented: false, subclass_of: nil }
      end
    end

    def add_to_instances(triple)
      instance = triple.object.to_s
      subject = triple.subject.to_s
      if instance.include?("owl#NamedIndividual")
        @used_class = subject
        return
      end
      if (@used_class == subject && !@instances.include?(instance))
        @instances << instance 
        @used_class = nil
      end
    end

    def analyze_completeness
      @instances.each { |instance| represent(instance) }
      @report[:total_classes] = @classes.keys.size
      @classes.each do |k, v|
        @report[:represented_classes] += 1 if @classes[k][:represented]
      end
    end

    def represent(clazz)
      return if !@classes[clazz] || @classes[clazz][:represented]
      @classes[clazz][:represented] = true
      return if clazz.nil?
      represent(@classes[clazz][:subclass_of])
    end
  end
end
