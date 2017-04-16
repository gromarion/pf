package com.itba.domain.model;

import com.itba.domain.PersistentEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "evaluated_resource")
public class EvaluatedResource extends PersistentEntity {

    @ManyToOne
    private EvaluationSession session;

    @Column(name = "resource")
    private String resource;

    @Column(name = "comments")
    private String comments;

    @Column(name = "class")
    private String clazz;

    @Column(name = "correct")
    private Boolean correct;

    @OneToMany(mappedBy = "resource")
    private Set<EvaluatedResourceDetail> details;

    EvaluatedResource() {}

    public EvaluatedResource(EvaluationSession session, String resource) {
        this.session = session;
        this.resource = resource;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

	public EvaluationSession getSession() {
		return session;
	}

	public String getResource() {
		return resource;
	}

	public String getClazz() {
		return clazz;
	}

	public Boolean getCorrect() {
		return correct;
	}

	public Set<EvaluatedResourceDetail> getDetails() {
		return details;
	}
}
