package com.itba.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

@Entity
@Table(name = "evaluated_resource_detail")
public class EvaluatedResourceDetail extends PersistentEntity {

    @ManyToOne
    private EvaluatedResource resource;

    @Column(name = "predicate")
    private String predicate;

    @Column(name = "object")
    private String object;
    
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "error_id", nullable = false)
    private Error error;

    @Column(name = "comment")
    private String comment;
    
    EvaluatedResourceDetail() {}

    public EvaluatedResourceDetail(EvaluatedResource resource, Error error, String predicate, String object) {
        this.resource = resource;
        this.error = error;
        this.predicate = predicate;
        this.object = object;
    }
    
    public String getComments() {
        return comment;
    }

    public void setComments(String comment) {
        this.comment = comment;
    }

	public EvaluatedResource getResource() {
		return resource;
	}

	public String getPredicate() {
		return predicate;
	}

	public String getObject() {
		return object;
	}

	public Error getErrorId() {
		return error;
	}

	public String getComment() {
		return comment;
	}
}
