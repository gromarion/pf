package com.itba.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "evaluated_resource_detail")
public class EvaluatedResourceDetail extends PersistentEntity {

	
    @ManyToOne
    private @Getter EvaluatedResource resource;

    @Column(name = "predicate")
    private @Getter String predicate;

    @Column(name = "object")
    private @Getter String object;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "error_id", nullable = false)
    private @Getter Error error;
    
    @Column(name = "comment")
    private @Getter @Setter String comment;
    
    EvaluatedResourceDetail() {}

    public EvaluatedResourceDetail(EvaluatedResource resource, Error error, String predicate, String object) {
        this.resource = resource;
        this.error = error;
        this.predicate = predicate;
        this.object = object;
    }
}
