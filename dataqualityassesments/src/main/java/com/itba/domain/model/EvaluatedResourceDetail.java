package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "evaluated_resource_details")
public class EvaluatedResourceDetail extends PersistentEntity {

    @ManyToOne
    private EvaluatedResource resource;

    @Column(name = "predicate")
    private String predicate;

    @Column(name = "object")
    private String object;

    // FIXME: cambiar esto para que instancie un objeto Error en lugar de apuntar al ID del error
    @Column(name = "error_id")
    private BigInteger errorId;

    @Column(name = "comment")
    private String comment;
    
    EvaluatedResourceDetail() {}

    public EvaluatedResourceDetail(EvaluatedResource resource, BigInteger errorId, String predicate, String object) {
        this.resource = resource;
        this.errorId = errorId;
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

	public BigInteger getErrorId() {
		return errorId;
	}

	public String getComment() {
		return comment;
	}
}
